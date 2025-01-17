package com.itacademy.service;

import com.itacademy.dto.user.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class UserHolder {

    public UserDetails getUser(){
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
