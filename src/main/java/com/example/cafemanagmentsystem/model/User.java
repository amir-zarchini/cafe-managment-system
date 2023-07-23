package com.example.cafemanagmentsystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.context.annotation.Configuration;

import javax.persistence.*;


//custom query to find user by email id
@NamedQuery(name = "User.findByEmailId",query = "select u from User u where u.email=:email")

//custom query to get all users
@NamedQuery(name = "User.getAllUser",query = "select new com.example.cafemanagmentsystem.wrapper.UserWrapper(u.id,u.name,u.email,u.contactNumber,u.status) from User u where u.role='user'")

//custom query to update a user
@NamedQuery(name = "User.updateStatus",query = "update User u Set u.status=:status where u.id=:id")

//custom query to get all admin
@NamedQuery(name = "User.getAllAdmin",query = "select u.email from User u where u.role='admin'")



@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@Table(name = "user")
@Configuration
public class User implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String name;
	private String contactNumber;
	private String email;
	private String password;
	private String status;
	private String role;
	
}
