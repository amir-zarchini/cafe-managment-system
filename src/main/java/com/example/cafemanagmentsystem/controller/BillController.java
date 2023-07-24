package com.example.cafemanagmentsystem.controller;

import com.example.cafemanagmentsystem.constents.CafeConstants;
import com.example.cafemanagmentsystem.model.Bill;
import com.example.cafemanagmentsystem.service.BillService;
import com.example.cafemanagmentsystem.utils.CafeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bill")
@RequiredArgsConstructor
public class BillController {

	private final BillService billService;

	@PostMapping("/generateReport")
	public ResponseEntity<String> generateReport(@RequestBody Map<String, Object> requestMap)
	{return this.billService.generateReport(requestMap);}

	@GetMapping("/getBills")
	public ResponseEntity<List<Bill>> getBills()
	{return billService.getBills();}

}
