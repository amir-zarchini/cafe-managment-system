package com.example.cafemanagmentsystem.repository;

import com.example.cafemanagmentsystem.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BillRepository extends JpaRepository<Bill, Integer>
{
	//get all bill by ADMIN
	List<Bill> getAllBills();

	List<Bill> getBillByUserName(@Param("username")String username);
}
