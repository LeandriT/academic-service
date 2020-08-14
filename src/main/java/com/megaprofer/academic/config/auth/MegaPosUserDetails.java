package com.megaprofer.academic.config.auth;

import com.megaprofer.academic.authentication.entity.User;
import com.megaprofer.academic.authentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.HashSet;

public class MegaPosUserDetails implements UserDetailsService {

	@Autowired
	UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsernameWithRoles(username);
		if(user != null) {
			Collection<GrantedAuthority> grantedAuthoritys= new HashSet<GrantedAuthority>();
			user.getRoles().forEach(role -> {
				role.getPermissions().forEach(permission -> {
					grantedAuthoritys.add(new SimpleGrantedAuthority(permission.getDomainAction()));
				});
			});
			return new org.springframework.security.core.userdetails.User(user.getUsername(),
					user.getPassword(), grantedAuthoritys);
		}
		return null;
	}

}
