package com.example.cafemanagmentsystem.controller;

import com.example.cafemanagmentsystem.service.ProductService;
import com.example.cafemanagmentsystem.wrapper.ProductWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

	private final ProductService productService;
	
	@PostMapping("/add")
	public ResponseEntity<String> addNewProduct(@RequestBody Map<String, String> requestMap)
	{return productService.addNewProduct(requestMap);}

	@GetMapping("/get")
	public ResponseEntity<List<ProductWrapper>> getAllProduct()
	{return productService.getAllProduct();}

	@PostMapping("/update")
	public ResponseEntity<String> updateProduct(@RequestBody Map<String, String> requestMap)
	{return productService.updateProduct(requestMap);}

	@PostMapping("/delete/{id}")
	public ResponseEntity<String> deleteProduct(@PathVariable("id") Integer id)
	{return productService.deleteProduct(id);}

	@PostMapping("/updateStatus")
	public ResponseEntity<String> updateProductStatus(@RequestBody Map<String, String> requestMap)
	{return productService.updateProductStatus(requestMap);}

	@GetMapping("/getByCategory/{id}")
	public ResponseEntity<List<ProductWrapper>> getByCategory(@PathVariable Integer id)
	{return productService.getByCategory(id);}

	@GetMapping("/getById/{id}")
	public ResponseEntity<ProductWrapper> getProductById(@PathVariable Integer id)
	{return productService.getProductById(id);}
	
}
