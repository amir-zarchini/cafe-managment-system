package com.example.cafemanagmentsystem.utils;

import com.google.common.base.Strings;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CafeUtils 
{
	private CafeUtils()
	{
		
	}
	
	public static ResponseEntity<String> getResponseEntity(String responseMessage,HttpStatus httpStatus)
	{
		return new ResponseEntity<>("{\"message\":\""+responseMessage+"\"}",httpStatus);
	}
	
	public static String getUUID()
	{
		Date date = new Date();
		long time = date.getTime();
		return "BILL-" + time;
	}
	
	//get JSON array from string
	public static JSONArray getJsonArrayFromString(String data) throws JSONException
	{
		return new JSONArray(data);
	}
	
	//get map from JSON
	public static Map<String, Object> getMapFromJson(String data)
	{
		if(!Strings.isNullOrEmpty(data))
		{
			return new Gson().fromJson(data, new TypeToken<Map<String, Object>>(){}.getType());
		}
		return new HashMap<>();
	}
}
