package com.ziong.ziong.respository;

import com.ziong.ziong.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {
}
