package com.mytaxi.spring.config;

import com.mytaxi.web.filters.JWTAuthenticationFilter;
import com.mytaxi.web.filters.JWTAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * This class sets up the default scurity configuration for the Drivers REST API
 */
@EnableWebSecurity
public class MyTaxiWebSecurity extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    public MyTaxiWebSecurity() {
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                    .cors() //activate cors
                .and()
                    .csrf().disable() // disable CSRF
                .authorizeRequests()
                    .antMatchers( "/swagger**", "/v2/api-docs**").permitAll()
                .and()//disable security for login
                .authorizeRequests()
                    .antMatchers(HttpMethod.POST, "/login")
                    .permitAll()
                .anyRequest() //every other request mus be authenticated
                    .authenticated()
                .and()
                    .addFilter(new JWTAuthenticationFilter(authenticationManager()))
                    .addFilter(new JWTAuthorizationFilter(authenticationManager()));
    }

    /**
     * Basic hardcoded implementation, only for testing purposes
     * @param auth
     * @throws Exception
     */
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth
                .inMemoryAuthentication().passwordEncoder(bCryptPasswordEncoder())
                .withUser("miztli")
                .password("$2a$10$CztggtNTgHAHk/oLHeQtfeuvjVjWIcVb0ezQMkpk0kRtczFekrrlu")
                .roles("ADMIN");
    }
}
