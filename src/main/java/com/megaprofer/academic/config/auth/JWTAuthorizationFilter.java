package com.megaprofer.academic.config.auth;


import com.auth0.jwt.interfaces.Claim;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private static final String FILTER_APPLIED = "__spring_security_scpf_applied";

    @Autowired
    private JWTService jwtService;

    public JWTAuthorizationFilter(MegaPosAuthenticationManager megaPosAuthenticationManager){
        super(megaPosAuthenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String tokenHeader = request.getHeader(JWTService.HEADER_STRING);
        if(tokenHeader == null){
            chain.doFilter(request, response);
            return;
        }
        Map<String, Claim> claims = jwtService.getClaims(tokenHeader);
        if(claims == null){
            throw new AuthorizationServiceException("Invalid Token");
        }
        String username = jwtService.getUsername(tokenHeader);
        Collection<? extends GrantedAuthority> roles = jwtService.getRoles(tokenHeader);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username,
                tokenHeader, roles);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        request.setAttribute(FILTER_APPLIED, true);
        chain.doFilter(request, response);
    }
}
