package com.example.cafemanagmentsystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

//custom query
@NamedQuery(name = "Product.getAllProduct",query = "select new com.example.cafemanagmentsystem.wrapper.ProductWrapper(p.id,p.name,p.description,p.price,p.status,p.category.id,p.category.name) from Product p")

@NamedQuery(name = "Product.updateProductStatus",query = "update Product p set p.status=:status where p.id=:id")

@NamedQuery(name = "Product.getProductByCategory",query = "select new com.example.cafemanagmentsystem.wrapper.ProductWrapper(p.id,p.name) from Product p where p.category.id=:id and p.status='true'")

@NamedQuery(name = "Product.getProductById", query = "select new com.example.cafemanagmentsystem.wrapper.ProductWrapper(p.id,p.name,p.description,p.price) from Product p where p.id=:id")

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product")
public class Product implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_fk",nullable = false)
	private Category category;

	private String name;
	private String description;
	private Integer price;
	private String status;
}
