package com.shashtech.blog.api.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.shashtech.blog.api.service.CustomUserDetailsService;
import com.shashtech.blog.api.util.JwtUtil;
import com.shashtech.blog.api.util.JwtUtilsWithRefreshToken;

@Component
public class JwtFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtilsWithRefreshToken jwtUtil;
	
	@Autowired
	private CustomUserDetailsService customUserDetails;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authHeader = request.getHeader("Authorization");
		String token = null;
		String userName = null;
		if(authHeader!=null && authHeader.startsWith("Bearer")) {
			token = authHeader.substring(7);
			try {
				userName = jwtUtil.getUserNameFromToken(token);
			} catch (Exception e) {
				throw new ServletException("Invalid token");
			}
		}
		
		if(userName!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
			UserDetails loadUserByUsername = customUserDetails.loadUserByUsername(userName);
			try {
				if(jwtUtil.validateJwtToken(token)) {
					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(loadUserByUsername, null, loadUserByUsername.getAuthorities());
					usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				}
			} catch (Exception e) {
				throw new ServletException("Invalid token");
			}
		}
		filterChain.doFilter(request, response);
	}

}
