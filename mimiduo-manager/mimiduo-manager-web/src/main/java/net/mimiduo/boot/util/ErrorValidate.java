package net.mimiduo.boot.util;

import java.util.List;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class ErrorValidate {
	public static String convertErrorMessage(BindingResult result){
		String message = "输入有误<br>";
    	List<FieldError> errorList = result.getFieldErrors();
    	for(FieldError fieldError : errorList){
    		message = message + fieldError.getField()+" "+fieldError.getDefaultMessage();
    		int index = errorList.indexOf(message);
    		if(index != errorList.size()-1){
    			message = message +"<br>";
    		}
    	}
		return message;
	}
}
