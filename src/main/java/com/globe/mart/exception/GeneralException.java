package com.globe.mart.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;
import java.util.LinkedHashMap;

@ControllerAdvice
public class GeneralException{

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<Object> handleException(CustomException c){
		LinkedHashMap<String,String> reqm = new LinkedHashMap<String,String>();
		String code = c.getCode();
		String emsg = c.getMsg();
		ResponseEntity<Object> resp = null;
		switch (code){
			case "400":
				reqm.put("timestamp",new Date().toString());
				reqm.put("code",code);
				reqm.put("status","error");
				reqm.put("msg",emsg);
				reqm.put("more","https://en.wikipedia.org/wiki/List_of_HTTP_status_codes");
				resp = new ResponseEntity<Object>(reqm,HttpStatus.BAD_REQUEST);
				break;

			case "404":
				reqm.put("timestamp",new Date().toString());
				reqm.put("code",code);
				reqm.put("status","error");
				reqm.put("msg",emsg);
				reqm.put("more","https://en.wikipedia.org/wiki/List_of_HTTP_status_codes");
				resp = new ResponseEntity<Object>(reqm,HttpStatus.BAD_REQUEST);
				break;

			case "409":
				reqm.put("timestamp",new Date().toString());
				reqm.put("code",code);
				reqm.put("status","error");
				reqm.put("msg",emsg);
				reqm.put("more","https://en.wikipedia.org/wiki/List_of_HTTP_status_codes");
				resp = new ResponseEntity<Object>(reqm,HttpStatus.CONFLICT);
				break;
		}
		return resp;
	}
}
