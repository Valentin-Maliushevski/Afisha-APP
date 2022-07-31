package com.itacademy.config;

import com.itacademy.controller.filter.JwtFilter;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtFilter filter;

    public SecurityConfig(JwtFilter filter) {
        this.filter = filter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Enable CORS and disable CSRF
        http = http.cors().and().csrf().disable();

        // Set session management to stateless
        http = http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and();

        // Set unauthorized requests exception handler
        http = http
                .exceptionHandling()
                .authenticationEntryPoint(
                        (request, response, ex) -> {
                            response.sendError(
                                    HttpServletResponse.SC_UNAUTHORIZED,
                                    ex.getMessage()
                            );
                        }
                )
                .and();

        // Set permissions on endpoints
        http.authorizeRequests()
            // private endpoints
            .antMatchers(HttpMethod.POST, "/api/v1/classifier/country").hasRole("ADMIN")
            .antMatchers(HttpMethod.POST, "/api/v1/classifier/concert/category").hasRole("ADMIN")
            // public endpoints
            .antMatchers( HttpMethod.GET,"/api/v1/classifier/country", "/api/v1/classifier/**").permitAll()
            .antMatchers(HttpMethod.GET,"/api/v1/classifier/concert/category", "/api/v1/classifier/concert/**").permitAll()

            .anyRequest().authenticated();

        // Add JWT token filter
        http.addFilterBefore(
                filter,
                UsernamePasswordAuthenticationFilter.class
        );
    }
}