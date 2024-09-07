package com.ziong.ziong.respository;

import com.ziong.ziong.model.Category;
import com.ziong.ziong.model.dtos.CategoryDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
    @Query("select c from Category c where c.is_activated = true and c.is_deleted = false")
    List<Category> findAllByActivated();

    @Query("select new com.ziong.ziong.model.dtos.CategoryDto(c.id, c.name, count(p.category.id)) from Category c inner join Product p on p.category.id = c.id " + " where c.is_activated = true and c.is_deleted = false group by c.id,c.name")
    List<CategoryDto> getCategoryAndProduct();
}
