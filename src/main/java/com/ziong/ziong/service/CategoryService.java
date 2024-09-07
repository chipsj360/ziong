package com.ziong.ziong.service;

import com.ziong.ziong.model.Category;
import com.ziong.ziong.model.dtos.CategoryDto;
import com.ziong.ziong.respository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repo;


    public Category save(Category category) {
        Category categorySave = new Category(category.getName());
        return repo.save(categorySave);
    }

    public Category findById(Long id) {
        return repo.getReferenceById(id);
    }

    public Category update(Category category) {
        Category categoryUpdate = null;
        try {
            categoryUpdate= repo.findById(category.getId()).get();
            categoryUpdate.setName(category.getName());
            categoryUpdate.set_activated(category.is_activated());
            categoryUpdate.set_deleted(category.is_deleted());
        }catch (Exception e){
            e.printStackTrace();
        }
        return repo.save(categoryUpdate);
    }

    public List<Category> findAllByActivated() {
        return repo.findAllByActivated();
    }
    public List<Category> findAll() {
        return repo.findAll();
    }

    public List<CategoryDto> getCategoryAndProduct() {
        return repo.getCategoryAndProduct();
    }
}
