package com.ziong.ziong.controller;

import com.ziong.ziong.model.Product;
import com.ziong.ziong.model.User;
import com.ziong.ziong.model.dtos.UserDto;
import com.ziong.ziong.service.ProductService;
import com.ziong.ziong.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class CoreController {
    @Autowired
    private UserService userService;

    @Autowired
    ProductService productService;

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }
    @GetMapping("/signup")
    public String addUser(Model model){
      model.addAttribute("user",new User());
      return"signup";
    }

    @PostMapping("/process-register")
    public String processUser(@ModelAttribute("user") User user){
    userService.adduser(user);
      return "login";
    }

    @GetMapping("/dashboard")
    public String adminDasboard(Model model){


        return "dashboard";
    }

}
