package com.chambule.controle_gastos.controller.exception;
import com.chambule.controle_gastos.services.exception.DataBase;
import com.chambule.controle_gastos.services.exception.DuplicateEmail;
import com.chambule.controle_gastos.services.exception.ResourceNotFound;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.time.Instant;

@ControllerAdvice
public class Handler {

    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<StandardError> resource(ResourceNotFound resource, HttpServletRequest request){
        String error = "Resource Not found";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError standardError = new StandardError(Instant.now(), status.value(), error, resource.getMessage(),request.getRequestURI());
        return ResponseEntity.status(status).body(standardError);
    }

    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<StandardError> DuplicateEmail(DuplicateEmail email, HttpServletRequest request){
        String error = "Esse email ja se encontra cadastrado";
        HttpStatus status = HttpStatus.CONFLICT;
        StandardError standardError = new StandardError(Instant.now(), status.value(), error, email.getMessage(),request.getRequestURI());
        return ResponseEntity.status(status).body(standardError);
    }

    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<StandardError> DataBase(DataBase email, HttpServletRequest request){
        String error = "This release is associated with a category";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError standardError = new StandardError(Instant.now(), status.value(), error, email.getMessage(),request.getRequestURI());
        return ResponseEntity.status(status).body(standardError);
    }

}
