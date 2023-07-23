package com.example.cafemanagmentsystem.repository;

import com.example.cafemanagmentsystem.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer>
{
	List<Category> getAllCategory();
}
