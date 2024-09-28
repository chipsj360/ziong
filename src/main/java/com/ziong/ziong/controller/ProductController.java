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
import java.util.ArrayList;
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
    public String displayProducts(Model model) {
        // Fetch all categories
        List<CategoryDto> categories = categoryService.getCategoryAndProduct();

        // Check if there are categories available
        if (categories != null && !categories.isEmpty()) {
            // Get the first category as the default category
            Long defaultCategoryId = categories.get(0).getCategoryId();

            // Fetch products in the default category
            List<Product> products = productService.getProductsInCategory(defaultCategoryId);

            // Convert product images to Base64 strings
            products.forEach(product -> {
                if (product.getImage() != null) {
                    String base64Image = Base64.getEncoder().encodeToString(product.getImage());
                    product.setBase64Image(base64Image);
                }
            });

            // Add attributes to the model
            model.addAttribute("products", products);
            model.addAttribute("categories", categories);
            model.addAttribute("defaultCategory", defaultCategoryId);
        } else {
            // Handle case where there are no categories
            model.addAttribute("products", new ArrayList<>());
            model.addAttribute("categories", new ArrayList<>());
            model.addAttribute("defaultCategory", null);
        }

        return "products-in-category";
    }

//    @GetMapping("/products")
//    public String displayProducts(Model model){
//        List<Product> products = productService.getAllProduct();
//        List<CategoryDto> categories = categoryService.getCategoryAndProduct();
//        products.forEach(product -> {
//            if (product.getImage() != null) {
//                String base64Image = Base64.getEncoder().encodeToString(product.getImage());
//                product.setBase64Image(base64Image); // You will need to add a field to store this
//            }
//        });
//        model.addAttribute("products", products);
//        model.addAttribute("categories", categories);
//        return "products-in-category";
//    }

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

    @RequestMapping(value = "/delete-product/{id}", method = {RequestMethod.PUT, RequestMethod.GET})
    public String deletedProduct(@PathVariable("id") Long id, RedirectAttributes attributes){
        try {
            productService.deleteById(id);
            attributes.addFlashAttribute("success", "Deleted successfully!");
        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("error", "Failed to deleted");
        }
        return "redirect:/dashboard";
    }

//    @GetMapping("/delete-product/{id}")
//    public String deleteProduct(@PathVariable("id") Long id)
//    {
//
//        productService.deleteProductById(id);
//        return "redirect:/dashboard";
//    }


}
