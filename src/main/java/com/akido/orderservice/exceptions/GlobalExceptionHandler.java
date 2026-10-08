package com.akido.orderservice.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({UserNotFoundException.class, OrderNotFoundException.class})
    public ResponseEntity<String> handleNotFound(RuntimeException exception,
                                                 HttpServletRequest request){
        request.setAttribute("errorMessage", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> handleConflict(RuntimeException exception,
                                                 HttpServletRequest request){
        request.setAttribute("errorMessage", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(exception.getMessage());
    }

    @ExceptionHandler(NoPermissionException.class)
    public ResponseEntity<String> handleForbidden(RuntimeException exception,
                                                 HttpServletRequest request){
        request.setAttribute("errorMessage", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(exception.getMessage());
    }
}
