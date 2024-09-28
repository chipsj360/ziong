package com.ziong.ziong.controller;

import com.ziong.ziong.model.ShoppingCart;
import com.ziong.ziong.model.User;
import com.ziong.ziong.service.ProductService;
import com.ziong.ziong.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class OrderController {
    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @GetMapping("/check-out")
    public String checkout(Model model, Principal principal){
        if(principal == null){
            return "redirect:/login";
        }
        String username = principal.getName();
        User user= userService.findByUsername(username);

        if (user.getEmail().trim().isEmpty()
                || user.getUserName().trim().isEmpty()){

            model.addAttribute("user", user);
            model.addAttribute("error", "You must fill the information after checkout!");
            return "account";
        } else {
            model.addAttribute("user", user);
            ShoppingCart cart = user.getShoppingCart();/*we are getting the shopping cart id that is associated with
            the logged in user */
            if(cart == null || cart.getCartItem().isEmpty()){
                model.addAttribute("check", "Please Add some things to your cart!");
                model.addAttribute("totalItems", 0);
                model.addAttribute("subTotal", 0);
            }else{
                model.addAttribute("totalItems", cart.getTotalItems());
                model.addAttribute("subTotal", cart.getTotalPrices());
                model.addAttribute("quantity",cart.getCartItem());
            }
            model.addAttribute("cart", cart);
        }

        return "checkout";
    }

}
