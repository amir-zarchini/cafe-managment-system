package com.example.cafemanagmentsystem.service;

import com.example.cafemanagmentsystem.model.Bill;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface BillService 
{
	ResponseEntity<String> generateReport(Map<String, Object> requestMap);
	ResponseEntity<List<Bill>> getBills();
}
