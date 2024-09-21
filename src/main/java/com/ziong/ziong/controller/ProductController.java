package com.ziong.ziong.controller;

import com.ziong.ziong.model.Category;
import com.ziong.ziong.model.Product;
import com.ziong.ziong.model.dtos.CategoryDto;
import com.ziong.ziong.model.dtos.ProductDto;
import com.ziong.ziong.service.CategoryService;
import com.ziong.ziong.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Base64;
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
        try {
            String username = principal.getName();
            productService.saveProductToDB(file, name, desc, price, currentQuantity, category, username);
            return "redirect:/dashboard";
        } catch (Exception e) {
            // Log the error details
            System.err.println("Error saving product: " + e.getMessage());
            return "redirect:/error"; // Assuming you have an error view mapped
        }
    }

    @GetMapping("/update-product/{id}")
    public String updateProductForm(@PathVariable("id") Long id, Model model, Principal principal){
        if(principal == null){
            return "redirect:/login";
        }
        model.addAttribute("title", "Update products");
        List<Category> categories = categoryService.findAllByActivated();
        ProductDto productDto = productService.getById(id);
        model.addAttribute("categories", categories);
        model.addAttribute("productDto", productDto);
        return "update_product";
    }


    @PostMapping("/update-product/{id}")
    public String processUpdate(@PathVariable("id") Long id,
                                @ModelAttribute("productDto") ProductDto productDto,
                                RedirectAttributes attributes
    ) {
        try {
            productService.update(productDto);
            attributes.addFlashAttribute("success", "Update successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("error", "Failed to update!");
        }
        return "redirect:/dashboard";
    }

    @GetMapping("/products-in-category/{id}")
    public String getProductsInCategory(@PathVariable("id") Long categoryId, Model model) {
        Category category = categoryService.findById(categoryId);
        List<CategoryDto> categories = categoryService.getCategoryAndProduct();
        List<Product> products = productService.getProductsInCategory(categoryId);

        // Convert product images to Base64 strings
        products.forEach(product -> {
            if (product.getImage() != null) {
                String base64Image = Base64.getEncoder().encodeToString(product.getImage());
                product.setBase64Image(base64Image); // You will need to add a field to store this
            }
        });

        model.addAttribute("category", category);
        model.addAttribute("categories", categories);
        model.addAttribute("products", products);
        return "products-in-category";
    }



    @GetMapping("/products")
    public String displayProducts(Model model){
        List<Product> products = productService.getAllProduct();
        List<CategoryDto> categories = categoryService.getCategoryAndProduct();
        products.forEach(product -> {
            if (product.getImage() != null) {
                String base64Image = Base64.getEncoder().encodeToString(product.getImage());
                product.setBase64Image(base64Image); // You will need to add a field to store this
            }
        });
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        return "products-in-category";
    }

    @GetMapping("/product_detail/{productId}")
    public String showProductDetails(@PathVariable("productId")Long id,
                                     Model model){
        Product products=productService.getProductById(id);

            if (products.getImage() != null) {
                String base64Image = Base64.getEncoder().encodeToString(products.getImage());
                products.setBase64Image(base64Image); // You will need to add a field to store this
            }

        model.addAttribute("product",products);
        return "product_details";
    }
    @GetMapping("/delete-product/{id}")
    public String deleteProduct(@PathVariable("id") Long id)
    {

        productService.deleteProductById(id);
        return "redirect:/dashboard";
    }


}
