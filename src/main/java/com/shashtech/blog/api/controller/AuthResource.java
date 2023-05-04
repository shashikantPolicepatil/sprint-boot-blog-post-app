package com.shashtech.blog.api.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shashtech.blog.api.entity.ERole;
import com.shashtech.blog.api.entity.RefreshToken;
import com.shashtech.blog.api.entity.Role;
import com.shashtech.blog.api.entity.User;
import com.shashtech.blog.api.exception.TokenRefreshException;
import com.shashtech.blog.api.repository.RoleRepository;
import com.shashtech.blog.api.repository.UserRepository;
import com.shashtech.blog.api.request.AuthRequest;
import com.shashtech.blog.api.request.LoginRequest;
import com.shashtech.blog.api.request.SignupRequest;
import com.shashtech.blog.api.request.TokenRefreshRequest;
import com.shashtech.blog.api.response.JwtResponse;
import com.shashtech.blog.api.response.MessageResponse;
import com.shashtech.blog.api.response.TokenRefreshResponse;
import com.shashtech.blog.api.service.RefreshTokenServiceImpl;
import com.shashtech.blog.api.service.UserDetailsServiceImpl;
import com.shashtech.blog.api.util.CommonUtil;
import com.shashtech.blog.api.util.JwtUtilsWithRefreshToken;

@RestController
@RequestMapping("/api/auth")
public class AuthResource {
	
	@Autowired
	private JwtUtilsWithRefreshToken jwtUtil;
	
	@Autowired
	private RefreshTokenServiceImpl tokenRefreshService;
	
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private PasswordEncoder encoder;

	@PostMapping("/authenticate")
	public String geneToken(@RequestBody AuthRequest authRequest) throws Exception {
		try {
			authManager.authenticate(
					new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
		} catch(Exception ex) {
			throw new Exception("Invalid username or password");
		}
		return "";
		//return jwtUtil.generateToken(authRequest.getUsername());
	}
	
	@PostMapping("/signin")
	public ResponseEntity<?> authUser(@Valid @RequestBody LoginRequest loginReq) {
		Authentication authenticate = authManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginReq.getUsername(), loginReq.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authenticate);
		UserDetailsServiceImpl userDetails = (UserDetailsServiceImpl) authenticate.getPrincipal();
		String generateJwtToken = jwtUtil.generateJwtToken(userDetails);
		List<String> roles = userDetails.getAuthorities().stream().map(item->item.getAuthority()).collect(Collectors.toList());
		RefreshToken tokenRefresh = tokenRefreshService.createRefreshToken(userDetails.getId());
		return ResponseEntity.ok(new JwtResponse(generateJwtToken, "Bearer", tokenRefresh.getRefreshToken(), userDetails.getEmail(), 
				userDetails.getId(),roles));
	}
	
	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupReq) {
		 if (userRepo.findByUserName(signupReq.getUserName()).isPresent()) {
		      return ResponseEntity.badRequest().body(new MessageResponse("Username is already taken!", HttpStatus.BAD_REQUEST.value(),
		    		  HttpStatus.BAD_REQUEST.name(), null));
		    }

		    if (userRepo.findByEmail(signupReq.getEmail()).isPresent()) {
		      return ResponseEntity.badRequest().body(new MessageResponse("Email is already in use!", HttpStatus.BAD_REQUEST.value(),
		    		  HttpStatus.BAD_REQUEST.name(), null));
		    }
		    Set<Role> userRole = new HashSet<>();
		    if(signupReq.getRole()==null) {
		    	Optional<Role> findByName = roleRepo.findByName(ERole.ROLE_ADMIN);
		    	userRole.add(findByName.get());
		    } else {
		    	signupReq.getRole().forEach(role->{
		    		switch(role) {
		    		case "admin":
		    			Role roleAdmin = roleRepo.findByName(ERole.ROLE_ADMIN).get();
		    			userRole.add(roleAdmin);
		    			break;
		    		case "mod":
		    			Role roleMod = roleRepo.findByName(ERole.ROLE_MODERATOR).get();
		    			userRole.add(roleMod);
		    			break;
		    		default:
		    			Role roleUser = roleRepo.findByName(ERole.ROLE_USER).get();
		    			userRole.add(roleUser);
		    		}
		    	});
		    }
		     
		    User user = new User(0,signupReq.getFname(),signupReq.getLname(),
		    		signupReq.getUserName(),encoder.encode(signupReq.getPassword()),signupReq.getEmail(),
		    		userRole);
		    user = userRepo.save(user);
		    
		return new ResponseEntity<>(new MessageResponse("Registration is successful",HttpStatus.CREATED.value(),
				"Success",null),HttpStatus.CREATED);
	}
	
	@PostMapping("/refreshtoken")
	public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequest tokenRefresh) {
		return tokenRefreshService.findTokenRefreshToken(tokenRefresh.getRefreshToken())
		.map(tokenRefreshService::verifyRefreshToken)
		.map(RefreshToken::getUser)
		.map(user->{
			String generateTokenFromUsername = jwtUtil.generateTokenFromUsername(user.getUserName());
			return ResponseEntity.ok(new TokenRefreshResponse(generateTokenFromUsername, tokenRefresh.getRefreshToken(), "Bearer"));
		}).orElseThrow(()->new TokenRefreshException(tokenRefresh.getRefreshToken(),"Refresh token is not in database!"));
	}
	
	@PostMapping("/signout")
	  public ResponseEntity<?> logoutUser() {
	    UserDetailsServiceImpl userDetails = CommonUtil.getUserFromContext();
	    Integer userId = userDetails.getId();
	    tokenRefreshService.deleteByUserId(userId);
	    return ResponseEntity.ok(new MessageResponse("Log out successful!", HttpStatus.OK.value(),
		    		  HttpStatus.OK.name(), null));
	  }
}
