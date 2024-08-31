package com.ziong.ziong.controller;

import com.ziong.ziong.model.Category;
import com.ziong.ziong.model.dtos.ProductDto;
import com.ziong.ziong.service.CategoryService;
import com.ziong.ziong.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@Controller
public class ProductController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;
    @GetMapping("/upload-products")
    public String addProducts(Model model, Principal principal){
        if(principal == null){
            return "redirect:/login";
        }
        List<Category> categories = categoryService.findAllByActivated();
        model.addAttribute("categories", categories);
        model.addAttribute("product", new ProductDto());
        return "products";
    }

    @PostMapping("/process_product")
    public String saveProduct(@RequestParam("file") MultipartFile file,
                              @RequestParam("pname") String name,
                              @RequestParam("price") double price,
                              @RequestParam("desc") String desc,
                              @RequestParam("quantity") int currentQuantity,
                              @RequestParam("categories")Category category,
                              Principal principal)

    {
        String username = principal.getName();
        productService.saveProductToDB(file, name, desc, price,currentQuantity,category, username);
        return "redirect:/dashboard";
    }
}
