package com.ziong.ziong.controller;

import com.ziong.ziong.model.CartItem;
import com.ziong.ziong.model.ShoppingCart;
import com.ziong.ziong.model.User;
import com.ziong.ziong.service.EmailService;
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
    @Autowired
    private EmailService emailService;

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



    @GetMapping("/order")
    public String order(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        String username = principal.getName();
        User user = userService.findByUsername(username);

        if (user.getEmail().trim().isEmpty() || user.getUserName().trim().isEmpty()) {
            model.addAttribute("user", user);
            model.addAttribute("error", "You must fill the information after checkout!");
            return "account";
        } else {
            ShoppingCart cart = user.getShoppingCart();
            if (cart == null || cart.getCartItem().isEmpty()) {
                model.addAttribute("check", "Please Add some things to your cart!");
                model.addAttribute("totalItems", 0);
                model.addAttribute("subTotal", 0);
            } else {
                // Prepare email details
                StringBuilder emailContent = new StringBuilder();
                emailContent.append("Order Confirmation for " + user.getFirstName() + "\n\n");
                emailContent.append("Items:\n");

                for (CartItem item : cart.getCartItem()) {
                    emailContent.append(item.getProduct().getName() + " - Quantity: " + item.getQuantity() + " - Price: K" + item.getProduct().getCostPrice() + "\n");
                }

                emailContent.append("\nTotal: K" + cart.getTotalPrices());

                // Send email to the company and cc the user
                String fromEmail = user.getEmail();
                String companyEmail = "josephchipate@gmail.com"; // Company email address
                emailService.sendOrderConfirmation(fromEmail, companyEmail, "New Order from " + user.getFirstName(), emailContent.toString());

                model.addAttribute("totalItems", cart.getTotalItems());
                model.addAttribute("subTotal", cart.getTotalPrices());
                model.addAttribute("quantity", cart.getCartItem());
            }
            model.addAttribute("cart", cart);
        }
        return "redirect:/";
    }

}
