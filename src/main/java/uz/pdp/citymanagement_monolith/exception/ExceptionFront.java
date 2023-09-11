package uz.pdp.citymanagement_monolith.exception;

import org.springframework.validation.ObjectError;

import java.util.List;

public class ExceptionFront extends RuntimeException{
    private final String message;


    public ExceptionFront(List<ObjectError> errorList){
        StringBuilder sb=new StringBuilder();

        for (ObjectError error:errorList){
            sb.append(error.getDefaultMessage()).append("\n");
        }
        this.message=sb.toString();
    }

    @Override
    public String getMessage() {
        return message;
    }
}
