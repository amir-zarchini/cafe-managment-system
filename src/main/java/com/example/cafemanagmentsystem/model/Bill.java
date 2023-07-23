package com.example.cafemanagmentsystem.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

//custom query
@NamedQuery(name = "Bill.getAllBills",query = "select b from Bill b order by b.id desc")

@NamedQuery(name = "Bill.getBillByUserName",query = "select b from Bill b where b.createdBy=:username order by b.id desc")

@Entity
@Data
@NoArgsConstructor
@Table(name = "bill")
public class Bill implements Serializable 
{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	private String uuid;
	private String name;
	private String email;
	private String contactNumber;
	private String paymentMethod;
	private Integer total;
	private String createdBy;

	@Column(name = "productdetails",columnDefinition = "json")
	private String productDetail;

}
