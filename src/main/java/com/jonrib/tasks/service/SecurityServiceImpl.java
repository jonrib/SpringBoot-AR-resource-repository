package com.jonrib.tasks.service;

import javax.servlet.http.Cookie;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.jonrib.tasks.jwt.JwtTokenUtil;

import io.jsonwebtoken.ExpiredJwtException;

@Service
public class SecurityServiceImpl implements SecurityService{
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;
    
    @Autowired
	private JwtTokenUtil jwtTokenUtil;
    
    private static final Logger logger = LoggerFactory.getLogger(SecurityServiceImpl.class);
    
    public SecurityServiceImpl() { }

    public SecurityServiceImpl(UserDetailsServiceImpl userDetails, AuthenticationManager authenticationManager2, JwtTokenUtil jwtTokenUtil) {
		this.authenticationManager = authenticationManager2;
		this.userDetailsService = userDetails;
		this.jwtTokenUtil = jwtTokenUtil;
	}

	@Override
    public String findLoggedInUsername(String token) {
		if (token != null && token.startsWith("Bearer ")) {
			String jwtToken = token.substring(7);
			try {
				String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
				return username;
			} catch (IllegalArgumentException e) {
				System.out.println("Get logged in Unable to get JWT Token");
			} catch (ExpiredJwtException e) {
				System.out.println("Get logged in JWT Token has expired");
			}
		} else {
			logger.warn("Get logged in JWT Token does not begin with JWT String.");
		}

        return "anonymousUser";
    }

    @Override
    public String autoLogin(String username, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());        
        authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        if (usernamePasswordAuthenticationToken.isAuthenticated()) {
            //SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            //logger.debug(String.format("Auto login %s successfully!", username));
        	return jwtTokenUtil.generateToken(userDetails);
        }
        
		return "";
    }
}
