package com.ziong.ziong.service;

import com.ziong.ziong.exceptions.CustomerNotFoundException;
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

    public UserDto getUser(String username) {

        UserDto userDto = new UserDto();
        User user = userRepository.findByUsername(username);
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setUserName(user.getUserName());
        userDto.setPassword(user.getPassword());
        userDto.setEmail(user.getEmail());

        return userDto;

    }
    public User changePass(UserDto customerDto) {
        User user = userRepository.findByUsername(customerDto.getUserName());
        user.setPassword(customerDto.getPassword());
        return userRepository.save(user);
    }

    public User update(UserDto dto, UserDto customerDto) {
        User user = userRepository.findByUsername(dto.getUserName());
        user.setFirstName(customerDto.getFirstName());
        user.setLastName(customerDto.getLastName());
        user.setEmail(customerDto.getEmail());
        return userRepository.save(user);
    }

    public void updatePassword(User user, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);

        user.setResetPasswordToken(null);
        userRepository.save(user);
    }
    public void updateResetPasswordToken(String token, String email) throws CustomerNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            user.setResetPasswordToken(token);
            userRepository.save(user);
        } else {
            throw new CustomerNotFoundException("Could not find any customer with the email " + email);
        }
    }
    public User getByResetPasswordToken(String token) {
        return userRepository.findByResetPasswordToken(token);
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