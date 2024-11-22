package com.ziong.ziong.controller;

import com.ziong.ziong.model.CartItem;
import com.ziong.ziong.model.Product;
import com.ziong.ziong.model.ShoppingCart;
import com.ziong.ziong.model.User;
import com.ziong.ziong.respository.CartItemRepository;
import com.ziong.ziong.respository.ShoppingCartRepository;
import com.ziong.ziong.service.EmailService;
import com.ziong.ziong.service.ProductService;
import com.ziong.ziong.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;

@Controller
public class OrderController {
    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    ShoppingCartRepository shoppingCartRepository;

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
        model.addAttribute("currentDate", LocalDate.now());

        return "checkout";
    }



    @GetMapping("/order")
    public String order(Model model, Principal principal, RedirectAttributes redirectAttributes) {
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
                emailContent.append("\nPhone Number " +user.getContact());
                // Send email to the company and cc the user
                String fromEmail = user.getEmail();
                String companyEmail = "info@ziongltd.com"; // Company email address
                emailService.sendOrderConfirmation(fromEmail, companyEmail, "New Order from " + user.getFirstName(), emailContent.toString());

                // Remove items from the shopping cart
                cartItemRepository.deleteAll(cart.getCartItem());

                // Clear the cart items set in memory
                cart.getCartItem().clear();

                // Reset the cart totals after clearing


                // Save the updated cart
                shoppingCartRepository.delete(cart);
                redirectAttributes.addFlashAttribute("success", "Order placed successfully!");

            }


        }

        return "redirect:/products";
    }



    @RequestMapping(value="/buy/{id}" , method = {RequestMethod.PUT , RequestMethod.GET})
    public String buy(@PathVariable("id")Long id, Principal principal, Model model){
        if(principal==null){
            return "redirect:/login";
        }
        String username= principal.getName();
        User user=userService.findByUsername(username);

        Product product = productService.getProductById(id);
        model.addAttribute("user", user);
        model.addAttribute("product", product);
        model.addAttribute("currentDate", LocalDate.now());
        return "checkout2";
    }


    @RequestMapping(value="/save-direct-order/{id}", method = {RequestMethod.PUT, RequestMethod.GET})
    public String directOrder(@PathVariable("id") Long id, Principal principal, Model model, RedirectAttributes redirectAttributes) {
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
            // Retrieve the product details
            Product product = productService.getProductById(id);

            if (product == null) {
                model.addAttribute("error", "Product not found!");
                return "redirect:/products";
            }

            // Prepare email details for direct order
            StringBuilder emailContent = new StringBuilder();
            emailContent.append("Order Confirmation for " + user.getFirstName() + "\n\n");
            emailContent.append("Item:\n");
            emailContent.append(product.getName() + " - Price: K" + product.getCostPrice() + "\n");

            emailContent.append("\nTotal: K" + product.getCostPrice());
            emailContent.append("\n Phone Number " +user.getContact());
            // Send email to the company and cc the user
            String fromEmail = user.getEmail();
            String companyEmail = "info@ziongltd.com"; // Company email address
            emailService.sendOrderConfirmation(fromEmail, companyEmail, "New Order from " + user.getFirstName(), emailContent.toString());

            // Add user and product information to the model for the view
            model.addAttribute("user", user);
            model.addAttribute("product", product);
            redirectAttributes.addFlashAttribute("success", "Order placed successfully!");
        }

        return "redirect:/products"; // Return the checkout view or another appropriate view
    }

}
