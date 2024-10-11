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

import java.util.Base64;
import java.util.List;

@Controller
public class CoreController {
    @Autowired
    private UserService userService;

    @Autowired
    ProductService productService;

    @GetMapping("/")
    public String index(Model model){
        List<Product>products= productService.getAllProduct();
        products.forEach(product -> {
            if (product.getImage() != null) {
                String base64Image = Base64.getEncoder().encodeToString(product.getImage());
                product.setBase64Image(base64Image);
            }
        });

        model.addAttribute("products", products);
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
    public String processUser(@ModelAttribute("user") User user,Model model){
        if(userService.isEmailExists(user.getEmail())){
            model.addAttribute("emailExists", true);
            return "signup";
        }else if(userService.isUsernameExists(user.getUserName())){
            model.addAttribute("usernameExists", true);
            return "signup";
        }

        else{
            return userService.adduser(user);
        }

    }

    @GetMapping("/dashboard")
    public String adminDasboard(Model model){

        List<Product> products = productService.getAllProduct();
        products.forEach(product -> {
            if (product.getImage() != null) {
                String base64Image = Base64.getEncoder().encodeToString(product.getImage());
                product.setBase64Image(base64Image); // You will need to add a field to store this
            }
        });
        model.addAttribute("products", products);
        return "dashboard";
    }

    @GetMapping("/view-products")
    public String viewProducts(Model model){

        List<Product> products = productService.getAllProduct();
        products.forEach(product -> {
            if (product.getImage() != null) {
                String base64Image = Base64.getEncoder().encodeToString(product.getImage());
                product.setBase64Image(base64Image); // You will need to add a field to store this
            }
        });
        model.addAttribute("products", products);
        return "dashboard";
    }

}
