package com.ziong.ziong.service;

import com.ziong.ziong.model.Category;
import com.ziong.ziong.model.Product;
import com.ziong.ziong.model.User;
import com.ziong.ziong.model.dtos.ProductDto;
import com.ziong.ziong.respository.CategoryRepository;
import com.ziong.ziong.respository.ProductRepository;
import com.ziong.ziong.utils.ImageUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

@Service
public class ProductService {
    @Autowired
    private ProductRepository repo;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    private ImageUpload imageUpload;

    @Autowired
    private UserService userService;
    public void  saveProductToDB(MultipartFile file, String name, String description
            , double price, int currentQuantity , Category category, String username)
    {
        Product p = new Product();
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        if(fileName.contains(".."))
        {
            System.out.println("not a a valid file");
        }
        try {
            p.setImage(Base64.getEncoder().encodeToString(file.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        p.setDescription(description);

        p.setName(name);
        p.setCostPrice(price);
        p.setCurrentQuantity(currentQuantity);
        p.setCategory(category);
        p.set_activated(true);
        p.set_deleted(false);
        User user = userService.getUserByUsername(username);
        p.setUser(user);

        repo.save(p);
    }

    public Product update (ProductDto productDto) {
        try {
            Product product = repo.getById(productDto.getId());

            product.setName(productDto.getName());
            product.setDescription(productDto.getDescription());
            product.setSalePrice(productDto.getSalePrice());
            product.setCostPrice(productDto.getCostPrice());
            product.setCurrentQuantity(productDto.getCurrentQuantity());
            product.setCategory(productDto.getCategory());
            return repo.save(product);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    public ProductDto getById(Long id) {
        Product product = repo.getById(id);
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setCurrentQuantity(product.getCurrentQuantity());
        productDto.setCategory(product.getCategory());
        productDto.setSalePrice(product.getSalePrice());
        productDto.setCostPrice(product.getCostPrice());
        productDto.setImage(product.getImage());
        productDto.setDeleted(product.is_deleted());
        productDto.setActivated(product.is_activated());
        return productDto;
    }
    public List<Product> getAllProduct()
    {
        return repo.findAll();
    }
    public List<Product> getProductsInCategory(Long categoryId) {
        return repo.getProductsInCategory(categoryId);
    }
    public Product getProductById(Long id) {
        return repo.getReferenceById(id);
    }
}
