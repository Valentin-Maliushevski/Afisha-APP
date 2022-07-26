package com.gmail.controller.filter;

import static org.apache.logging.log4j.util.Strings.isEmpty;

import com.gmail.controller.json.utils.JwtTokenUtil;
import com.gmail.dto.user.Role;
import com.gmail.dto.user.User;
import com.gmail.dto.user.UserWithoutPassword;
import com.gmail.service.api.ICountryService;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final RestTemplate template;

    public JwtFilter(RestTemplateBuilder templateBuilder) {
        this.template = templateBuilder.build();;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (isEmpty(header) || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        // Get jwt token and validate
        final String token = header.split(" ")[1].trim();
        if (!JwtTokenUtil.validate(token)) {
            chain.doFilter(request, response);
            return;
        }

        String url = "http://localhost:8081/api/v1/users/me";
        String newToken = "Bearer " + JwtTokenUtil.generateAccessToken(JwtTokenUtil.getUsername(token));
        final HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, newToken);
        final HttpEntity<String> entity = new HttpEntity<String>(headers);
        User user = new User();
        try {
        ResponseEntity<UserWithoutPassword> response2 = template.exchange(url, HttpMethod.GET, entity, UserWithoutPassword.class);
        HttpStatus status = response2.getStatusCode();

        if(status.is2xxSuccessful()) {
            UserWithoutPassword userWithoutPassword = response2.getBody();
            user.setUsername(userWithoutPassword.getMail());
            user.setUuid(userWithoutPassword.getUuid());
            user.setUserStatus(userWithoutPassword.getUserStatus());
            user.setRoles(userWithoutPassword.getRole().stream()
                .map(p -> new Role(p.getName())).collect(Collectors.toSet()));
        }

        } catch (HttpClientErrorException e) {
              throw new RuntimeException("client Exception");
            }

        UsernamePasswordAuthenticationToken
                authentication = new UsernamePasswordAuthenticationToken(
                user, null,
                user == null ?
                        List.of() : user.getAuthorities()
        );

        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }
}