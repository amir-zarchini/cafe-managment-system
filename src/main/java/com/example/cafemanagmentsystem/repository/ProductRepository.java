package com.example.cafemanagmentsystem.repository;

import com.example.cafemanagmentsystem.model.Product;
import com.example.cafemanagmentsystem.wrapper.ProductWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer>
{
	List<ProductWrapper>getAllProduct();
	
	@Modifying
	@Transactional
	Integer updateProductStatus(@Param("status")String status,@Param("id")Integer id);
	
	List<ProductWrapper> getProductByCategory(@Param("id")Integer id);
	
	ProductWrapper getProductById(@Param("id")Integer id);
}