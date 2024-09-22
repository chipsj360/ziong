package com.ziong.ziong.controller;


import com.ziong.ziong.model.dtos.UserDto;
import com.ziong.ziong.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
public class UserController {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;






    @GetMapping("/account")
    public String profile(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        } else {
            String username = principal.getName();
            UserDto customer = userService.getUser(username);
            model.addAttribute("customer", customer);
//
            return "account";
        }
    }

    @PostMapping("/change-password")
    public String changePass(@RequestParam("oldPassword") String oldPassword,
                             @RequestParam("newPassword") String newPassword,
                             @RequestParam("repeatNewPassword") String repeatPassword,
                             RedirectAttributes attributes,
                             Model model,
                             Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        } else {
            UserDto userDto =userService.getUser(principal.getName());
            if (passwordEncoder.matches(oldPassword, userDto.getPassword())
                    && !passwordEncoder.matches(newPassword, oldPassword)
                    && !passwordEncoder.matches(newPassword, userDto.getPassword())
                    && repeatPassword.equals(newPassword) && newPassword.length() >= 5) {
                userDto.setPassword(passwordEncoder.encode(newPassword));
                userService.changePass(userDto);
                attributes.addFlashAttribute("success", "Your password has been changed successfully!");
                return "redirect:/account";
            } else {
                model.addAttribute("message", "Your password is wrong");
                return "redirect:/account";
            }
        }
    }

    @PostMapping("/update-profile")
    public String updateProfile( @ModelAttribute("customer") UserDto customerDto,
                                BindingResult result,
                                RedirectAttributes attributes,
                                Model model,
                                Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        } else {
            String username = principal.getName();
            UserDto customer = userService.getUser(username);


            if (result.hasErrors()) {
                return "account";
            }
            userService.update(customer,customerDto);
            UserDto customerUpdate = userService.getUser(principal.getName());
            attributes.addFlashAttribute("success", "Update successfully!");
            model.addAttribute("customer", customerUpdate);
            return "redirect:/account";
        }
    }
}

