package com.megaprofer.academic.config.auth;

import com.auth0.jwt.interfaces.Claim;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Base64Utils;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;


public interface JWTService {
	String SECRET = Base64Utils.encodeToString("c3833e86-dfe0-44e0-9939-a8955fcb7223".getBytes());
	long EXPIRATION_DATE = 57_600_000;
	String TOKEN_PREFIX = "Bearer ";
	String HEADER_STRING = "Authorization";

	String create(Authentication auth) throws IOException;

	String getUsername(String token);

	Map<String, Claim> getClaims(String token);

	Collection<? extends GrantedAuthority> getRoles(String token) throws IOException;
}
