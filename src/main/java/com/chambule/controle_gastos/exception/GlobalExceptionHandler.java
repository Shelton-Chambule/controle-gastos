package com.chambule.controle_gastos.exception;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.time.Instant;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<StandardError> resource(ResourceNotFound resource, HttpServletRequest request) {
        log.warn("Error 404 on {}: {}", request.getRequestURI(), resource.getMessage());
        String error = "Resource Not found";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError standardError = new StandardError(Instant.now(), status.value(), error, resource.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(standardError);
    }

    @ExceptionHandler(DuplicateEmail.class)
    public ResponseEntity<StandardError> duplicateEmail(DuplicateEmail email, HttpServletRequest request) {
        log.warn("Error 409 on {}: {}", request.getRequestURI(), email.getMessage());
        String error = "Email register";
        HttpStatus status = HttpStatus.CONFLICT;
        StandardError standardError = new StandardError(Instant.now(), status.value(), error, email.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(standardError);
    }

    @ExceptionHandler(CategoryAlreadyExistsException.class)
    public ResponseEntity<StandardError> duplicateCategory(CategoryAlreadyExistsException category, HttpServletRequest request) {
        log.warn("Error 409 on {}: {}", request.getRequestURI(), category.getMessage());
        String error = "Category already exists";
        HttpStatus status = HttpStatus.CONFLICT;
        StandardError standardError = new StandardError(Instant.now(), status.value(), error, category.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(standardError);
    }

    @ExceptionHandler(DataBase.class)
    public ResponseEntity<StandardError> dataBaseViolation(DataBase dataBase, HttpServletRequest request) {
        log.warn("Error 400 on {}: {}", request.getRequestURI(), dataBase.getMessage());
        String error = "This release is associated with a category";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError standardError = new StandardError(Instant.now(), status.value(), error, dataBase.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(standardError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> methodArgument(MethodArgumentNotValidException validException, HttpServletRequest request) {
        log.warn("Error 422 on {}: {}", request.getRequestURI(), validException.getMessage());
        String error = "Validation error";
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        StandardError standardError = new StandardError(Instant.now(), status.value(), error, validException.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(standardError);
    }

    @ExceptionHandler(CategoryNotFound.class)
    public ResponseEntity<StandardError> categoryNotFound(CategoryNotFound categoryNotFound, HttpServletRequest request) {
        log.warn("Error 404 on {}: {}", request.getRequestURI(), categoryNotFound.getMessage());
        String error = "Category not found";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError standardError = new StandardError(Instant.now(), status.value(), error, categoryNotFound.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(standardError);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<StandardError> accessDenied(AccessDeniedException validException, HttpServletRequest request) {
        log.warn("Error 403 on {}: {}", request.getRequestURI(), validException.getMessage());
        String error = "Access Denied";
        HttpStatus status = HttpStatus.FORBIDDEN;
        StandardError standardError = new StandardError(Instant.now(), status.value(), error, validException.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(standardError);
    }
}