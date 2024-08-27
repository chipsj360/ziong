package com.ziong.ziong.service;

import com.ziong.ziong.model.Category;
import com.ziong.ziong.respository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repo;



    public List<Category> findAll() {
        return repo.findAll();
    }
}
