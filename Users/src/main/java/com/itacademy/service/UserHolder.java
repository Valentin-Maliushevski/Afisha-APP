package com.itacademy.service;

import com.itacademy.dao.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserHolder {

    public User getUser(){
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
