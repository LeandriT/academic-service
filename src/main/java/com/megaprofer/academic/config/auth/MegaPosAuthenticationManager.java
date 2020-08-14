package com.megaprofer.academic.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class MegaPosAuthenticationManager implements AuthenticationManager {

	@Autowired
	private MegaPosUserDetails megaPosUserDetails;
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		UserDetails userDetails = megaPosUserDetails.loadUserByUsername(authentication.getPrincipal().toString());
		if(userDetails != null && passwordEncoder.matches(authentication.getCredentials().toString(), userDetails.getPassword())) {
			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());
			return usernamePasswordAuthenticationToken;
		}
		throw new UsernameNotFoundException("Incorrect user or password");
	}

}
