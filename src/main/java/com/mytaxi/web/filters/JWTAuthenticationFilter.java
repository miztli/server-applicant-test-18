package com.mytaxi.web.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mytaxi.domainobject.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static com.mytaxi.util.SecurityContants.*;

/**
 * This class provides JWT Authentication
 */
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
            throws AuthenticationException {
        //VERY basic setup, just por testing
            User principal;

            try {
                principal = new ObjectMapper().readValue(request.getInputStream(), User.class);
            } catch (IOException e) {
                throw new BadCredentialsException("Authentication object couldn't be deserialized");
            }
            return authenticationManager
                        .authenticate(
                                new UsernamePasswordAuthenticationToken(
                                        principal.getUsername(),
                                        principal.getPassword(),
                                        new ArrayList<>()));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

        String token = Jwts.builder().setIssuedAt(new Date()).setIssuer(ISSUER)
                .setSubject(SUBJECT)
                .setExpiration(new Date(System.currentTimeMillis() + 1800000)) //30 min
                .signWith(SignatureAlgorithm.HS512, SECURE_WORD).compact();
        response.addHeader(AUTHORIZATION_HEADER, TYPE_BEARER + " " + token);
    }
}
