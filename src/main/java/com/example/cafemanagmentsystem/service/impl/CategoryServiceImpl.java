package com.example.cafemanagmentsystem.service.impl;

import com.example.cafemanagmentsystem.constents.CafeConstants;
import com.example.cafemanagmentsystem.jwt.JwtFilter;
import com.example.cafemanagmentsystem.model.Category;
import com.example.cafemanagmentsystem.repository.CategoryRepository;
import com.example.cafemanagmentsystem.service.CategoryService;
import com.example.cafemanagmentsystem.utils.CafeUtils;
import com.google.common.base.Strings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService
{
	private final CategoryRepository categoryRepository;
	private final JwtFilter jwtFilter;

	@Override
	public ResponseEntity<String> addNewCategory(Map<String, String> requestMap)
	{
		try
		{
			if(jwtFilter.isAdmin())
			{
				if(validateCategoryMap(requestMap,false))
				{
					categoryRepository.save(getCategoryFromMap(requestMap, false));
					return CafeUtils.getResponseEntity("Category Added Succcesfully.", HttpStatus.OK);
				}
			}
			else
			{
				return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private boolean validateCategoryMap(Map<String, String> requestMap, boolean validateId)
	{
		if(requestMap.containsKey("name"))
		{
			if(requestMap.containsKey("id") && validateId)
			{
				return true;
			}
			else return !validateId;
		}
		return false;
	}

	private Category getCategoryFromMap(Map<String, String> requestMap, Boolean isAdd)
	{
		Category category=new Category();
		if(isAdd)
		{
			category.setId(Integer.parseInt(requestMap.get("id")));
		}
		category.setName(requestMap.get("name"));
		return category;
	}

	@Override
	public ResponseEntity<List<Category>> getAllCategory(String filterValue)
	{
		try
		{
			if(!Strings.isNullOrEmpty(filterValue) && filterValue.equalsIgnoreCase("true"))
			{
				log.info("Inside if");
				return new ResponseEntity<>(categoryRepository.getAllCategory(),HttpStatus.OK);
			}
			return new ResponseEntity<>(categoryRepository.findAll(),HttpStatus.OK);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> updateCategory(Map<String, String> requestMap)
	{
		try
		{
			if(jwtFilter.isAdmin())
			{
				if(validateCategoryMap(requestMap, true))
				{
					Optional<Category> optional = categoryRepository.findById(Integer.parseInt(requestMap.get("id")));
					if(optional.isPresent())
					{
						categoryRepository.save(getCategoryFromMap(requestMap, true));
						return CafeUtils.getResponseEntity("Category Updated Successfullt.", HttpStatus.OK);
					}
					else
					{
						return CafeUtils.getResponseEntity("Category id does not exist.", HttpStatus.OK);
					}
				}
				return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
			}
			return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
