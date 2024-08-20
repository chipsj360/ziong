package com.ziong.ziong.service;

import com.ziong.ziong.model.CustomUserDetails;
import com.ziong.ziong.model.User;
import com.ziong.ziong.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository repo;
    @Override
    public UserDetails loadUserByUsername(String fullName) throws UsernameNotFoundException {
        User user;
        if (fullName.contains("@")) {
            user = repo.findByEmail(fullName);
        } else {
            user = repo.findByUsername(fullName);
        }

        if(user==null){
            throw new UsernameNotFoundException("user not found ");
        }
        return new CustomUserDetails(user);
    }
}
