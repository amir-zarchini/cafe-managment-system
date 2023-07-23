package com.example.cafemanagmentsystem.controller;

import com.example.cafemanagmentsystem.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

	private final CategoryService categoryService;

	@PostMapping("/add")
	public ResponseEntity<String> addNewCategory(@RequestBody Map<String, String> requestMap)
	{return categoryService.addNewCategory(requestMap);}

	@GetMapping("/get")
	public ResponseEntity<?> getAllCategory(@RequestBody(required = false) String filterValue)
	{return categoryService.getAllCategory(filterValue);}

	@PostMapping("/update")
	public ResponseEntity<String> updateCategory(@RequestBody Map<String, String> requestMap)
	{return categoryService.updateCategory(requestMap);}
}
