package com.example.cafemanagmentsystem.service.impl;

import com.example.cafemanagmentsystem.constents.CafeConstants;
import com.example.cafemanagmentsystem.jwt.JwtFilter;
import com.example.cafemanagmentsystem.model.Bill;
import com.example.cafemanagmentsystem.repository.BillRepository;
import com.example.cafemanagmentsystem.service.BillService;
import com.example.cafemanagmentsystem.utils.CafeUtils;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class BillServiceImpl implements BillService
{
	private final BillRepository billRepository;
	private final JwtFilter jwtFilter;
	
	@Override
	public ResponseEntity<String> generateReport(Map<String, Object> requestMap) 
	{
		log.info("Inside generateReport");
		try 
		{
			String fileName;
			if(validateRequestMap(requestMap))
			{
				if(requestMap.containsKey("isGenerate") && !(Boolean) requestMap.get("isGenerate"))
				{
					fileName = (String) requestMap.get("uuid");
				}
				else
				{
					fileName = CafeUtils.getUUID();
					requestMap.put("uuid", fileName);
					insertBill(requestMap);
				}
				
				String data = "Name: " + requestMap.get("name") + "\n"+"Contact Number: " + requestMap.get("contactNumber") + 
						"\n"+"Email: "+requestMap.get("email") + "\n"+"Payment Method: "+requestMap.get("paymentMethod");
				
				//location where we want to store
				Document document = new Document();
				PdfWriter.getInstance(document, new FileOutputStream(CafeConstants.STORE_LOCATION+"\\"+fileName+".pdf"));
				
				//setting rectangular border in PDF
				document.open();
				setRectangleInPdf(document);
				
				//adding header in PDF
				Paragraph chunk = new Paragraph("Cafe Management System",getFont("Header"));
				chunk.setAlignment(Element.ALIGN_CENTER);
				document.add(chunk);
				
				//font that we will use in PDF header
				Paragraph paragraph = new Paragraph(data+"\n \n",getFont("Data"));
				document.add(paragraph);
				
				PdfPTable table = new PdfPTable(5);
				table.setWidthPercentage(100);
				addTableHeader(table);
				
				JSONArray jsonArray = CafeUtils.getJsonArrayFromString((String)requestMap.get("productDetails"));
				for(int i=0;i<jsonArray.length();i++)
				{
					addRows(table,CafeUtils.getMapFromJson(jsonArray.getString(i)));
				}
				document.add(table);
				
				//footer
				Paragraph footer = new Paragraph("Total : "+requestMap.get("totalAmount")+"\n"
						+"Thank You For Visiting.Please Vissit Again !!",getFont("Data"));
				document.add(footer);
				document.close();
				return new ResponseEntity<>("{\"uuid\":\""+fileName+"\"}",HttpStatus.OK);
			}
			return CafeUtils.getResponseEntity("Required data not found.", HttpStatus.BAD_REQUEST);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private void addRows(PdfPTable table, Map<String, Object> data) 
	{
		log.info("Inside addRows");
		table.addCell((String) data.get("name"));
		table.addCell((String) data.get("category"));
		table.addCell((String) data.get("quantity"));
		table.addCell(Double.toString((Double) data.get("price")));
		table.addCell(Double.toString((Double) data.get("total")));
	}


	private void addTableHeader(PdfPTable table) 
	{
		log.info("Inside addTableHeader");
		Stream.of("Name","Category","Quantity","Price","Sub Total")
			.forEach(columnTitle -> {
				PdfPCell header = new PdfPCell();
				header.setBackgroundColor(BaseColor.LIGHT_GRAY);
				header.setBorderWidth(2);
				header.setPhrase(new Phrase(columnTitle));
				header.setBackgroundColor(BaseColor.YELLOW);
				header.setHorizontalAlignment(Element.ALIGN_CENTER);
				header.setVerticalAlignment(Element.ALIGN_CENTER);
				table.addCell(header);
			});
	}

	private Font getFont(String type) 
	{
		log.info("Inside getFont");
		return switch (type) {
			case "Header" -> getHeader();
			case "Data" -> getData();
			default -> new Font();
		};
	}

	private Font getData() {
		Font dataFont = FontFactory.getFont(FontFactory.TIMES_ROMAN,11,BaseColor.BLACK);
		dataFont.setStyle(Font.BOLD);
		return dataFont;
	}

	private Font getHeader() {
		Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE,18,BaseColor.BLACK);
		headerFont.setStyle(Font.BOLD);
		return headerFont;
	}

	private void setRectangleInPdf(Document document) throws DocumentException
	{
		log.info("Inside setRectangleInPdf");
		Rectangle rect = new Rectangle(577,825,18,15);
		rect.enableBorderSide(1);
		rect.enableBorderSide(2);
		rect.enableBorderSide(4);
		rect.enableBorderSide(8);
		rect.setBorderColor(BaseColor.BLACK);
		rect.setBorderWidth(1);
		document.add(rect);
	}

	private void insertBill(Map<String, Object> requestMap)
	{
		try 
		{
			Bill bill = new Bill();
			bill.setUuid((String) requestMap.get("uuid"));
			bill.setName((String) requestMap.get("name"));
			bill.setEmail((String) requestMap.get("email"));
			bill.setContactNumber((String) requestMap.get("contactNumber"));
			bill.setPaymentMethod((String)requestMap.get("paymentMethod"));
			bill.setTotal(Integer.parseInt((String)requestMap.get("totalAmount")));
			bill.setCreatedBy(jwtFilter.getCurrentUser());
			billRepository.save(bill);
		}
		catch (Exception ex) 
		{
			ex.printStackTrace();
		}
	}

	private boolean validateRequestMap(Map<String, Object> requestMap) 
	{
		return requestMap.containsKey("name") && 
				requestMap.containsKey("contactNumber") && 
				requestMap.containsKey("email") && 
				requestMap.containsKey("paymentMethod") && 
				requestMap.containsKey("productDetails") &&
				requestMap.containsKey("totalAmount");
	}

	
	@Override
	public ResponseEntity<List<Bill>> getBills()
	{
		return new ResponseEntity<>(
				jwtFilter.isAdmin() ?
				billRepository.getAllBills() :
				billRepository.getBillByUserName(
						jwtFilter.getCurrentUser()
				)
				,HttpStatus.OK);
	}
}
