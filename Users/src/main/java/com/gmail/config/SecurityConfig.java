package com.gmail.config;

import com.gmail.controller.filter.JwtFilter;
import javax.servlet.http.HttpServletResponse;
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
        .antMatchers("/api/v1/users/registration", "/api/v1/users/login").permitAll()
        .antMatchers("/api/v1/users/me").authenticated()
        .antMatchers("/api/v1/users/**").hasRole("ADMIN")

        .anyRequest().authenticated();

    // Add JWT token filter
    http.addFilterBefore(
        filter,
        UsernamePasswordAuthenticationFilter.class
    );
  }
}