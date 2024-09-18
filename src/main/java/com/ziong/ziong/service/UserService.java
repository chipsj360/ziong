package com.ziong.ziong.service;

import com.ziong.ziong.model.User;
import com.ziong.ziong.model.dtos.UserDto;
import com.ziong.ziong.respository.RoleRepository;
import com.ziong.ziong.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public String adduser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singletonList(roleRepository.findByName("CUSTOMER")));
        userRepository.save(user);
      return "login";
    }
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    public User getUserByUsername(String username){
        return userRepository.findByUsername(username);
    }
    public boolean isEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }
    public boolean isUsernameExists(String userName) {
        return userRepository.existsByUserName(userName);
    }
}