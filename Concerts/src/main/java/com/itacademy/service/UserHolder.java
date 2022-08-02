package com.itacademy.service;

import com.itacademy.dto.user.User;
import com.itacademy.dto.user.UserStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserHolder {

    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return false;
        }
        return true;
    }

    public boolean hasRoleAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean hasAdminRole = authentication.getAuthorities().stream()
            .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));

        return hasAdminRole;
    }

    public User getUser(){
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public boolean hasStatusActivated() {
        User user = getUser();
        if(user.getUserStatus().equals(UserStatus.ACTIVATED)) {
            return true;
        } else return false;
    }

}
