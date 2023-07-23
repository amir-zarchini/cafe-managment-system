package com.example.cafemanagmentsystem.repository;

import com.example.cafemanagmentsystem.model.User;
import com.example.cafemanagmentsystem.wrapper.UserWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> 
{
	User findByEmailId(@Param("email") String email);
	List<UserWrapper> getAllUser();
	List<String> getAllAdmin();
	
	@Transactional
	@Modifying
	Integer updateStatus(@Param("status")String status,@Param("id")Integer id);
	
	//find by email(we don't need to write query for this JPA will provide)
	User findByEmail(String email);
}
