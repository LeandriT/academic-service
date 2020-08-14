package com.megaprofer.academic.config.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class JWTServiceImpl implements JWTService {

    @Override
    public String create(Authentication auth) throws IOException {

        Map<String, Object> headerClaims = new HashMap();
        headerClaims.put("roles", auth.getAuthorities());

        return JWT.create()
                .withHeader( headerClaims)
                .withSubject(auth.getPrincipal().toString())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_DATE))
                .sign(Algorithm.HMAC256(SECRET));
    }

    @Override
    public String getUsername(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getSubject();
    }

    @Override
    public Map<String, Claim> getClaims(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getClaims();
        } catch (JWTVerificationException exception) {
            return null;
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getRoles(String token) throws IOException {
        DecodedJWT jwt = JWT.decode(token);
        Claim claimRoles = jwt.getHeaderClaim("roles");
        Set headerClaims = claimRoles.as(Set.class);
        Collection<GrantedAuthority> result= new HashSet<>();
        headerClaims.forEach(claim -> {
            Map<String, Object> roleMap = (Map<String, Object>) claim;
            roleMap.entrySet().forEach(stringObjectEntry -> {
                GrantedAuthority simpleGrantedAuthority =
                        new SimpleGrantedAuthority(stringObjectEntry.getValue().toString());
                result.add(simpleGrantedAuthority);
            });

        });
        return result;
    }

}