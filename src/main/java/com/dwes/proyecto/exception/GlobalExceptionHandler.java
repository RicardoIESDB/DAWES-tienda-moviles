package com.dwes.proyecto.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> serializacionError(
            BadCredentialsException ex, HttpServletRequest request) {

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", java.time.LocalDateTime.now());
        response.put("status", 401);
        response.put("error", "BadCredentialsException: Error en usuario o contraseña");
        response.put("message", ex.getMessage());
        response.put("path", request.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> serializacionError(
            HttpMessageNotReadableException ex, HttpServletRequest request) {

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", java.time.LocalDateTime.now());
        response.put("status", 400);
        response.put("error", "HttpMessageNotReadableException: Error al serializar los datos del body");
        response.put("message", ex.getMessage());
        response.put("path", request.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> manejarValidacion(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", java.time.LocalDateTime.now());
        response.put("status", 400);
        response.put("error", "MethodArgumentNotValidException: Error de validación en valores de los campos");
        response.put("path", request.getRequestURI());

        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errores.put(error.getField(), error.getDefaultMessage())
        );

        response.put("validaciones", errores);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDuplicate(
            DataIntegrityViolationException ex, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", java.time.LocalDateTime.now());
        response.put("status", 409);
        response.put("error", "DataIntegrityViolationException: Duplicate entry");
        response.put("message", ex.getMessage());
        response.put("path", request.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<?> handleNotFound(
            EmptyResultDataAccessException ex, HttpServletRequest request) {

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", java.time.LocalDateTime.now());
        response.put("status", 404);
        response.put("error", "EmptyResultDataAccessException: Data Not Found");
        response.put("message", ex.getMessage());
        response.put("path", request.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFound(
            ResourceNotFoundException ex, HttpServletRequest request) {

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", java.time.LocalDateTime.now());
        response.put("status", 404);
        response.put("error", "ResourceNotFoundException: Recurso Not Found");
        response.put("message", ex.getMessage());
        response.put("path", request.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(
            NoHandlerFoundException ex, HttpServletRequest request) {

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", java.time.LocalDateTime.now());
        response.put("status", 404);
        response.put("error", "NoHandlerFoundException: URL Not Found");
        response.put("message", "Endpoint not found: " + request.getRequestURI());
        response.put("path", request.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNoResourceFound(
            HttpServletRequest request) {

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", java.time.LocalDateTime.now());
        response.put("status", 404);
        response.put("error", "NoResourceFoundException: Not Found");
        response.put("message", "Endpoint not found: " + request.getRequestURI());
        response.put("path", request.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex, HttpServletRequest request) {

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", java.time.LocalDateTime.now());
        response.put("status", 400);
        response.put("error", "Bad Request");
        response.put("message", "Invalid parameter value for '" + ex.getName() + "'");
        response.put("path", request.getRequestURI());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, Object>> handleMissingParams(
            MissingServletRequestParameterException ex, HttpServletRequest request) {

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", java.time.LocalDateTime.now());
        response.put("status", 400);
        response.put("error", "Bad Request");
        response.put("message", "Missing required parameter: '" + ex.getParameterName() + "'");
        response.put("path", request.getRequestURI());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleAllExceptions(
            Exception ex, HttpServletRequest request) {

        String message = ex.getMessage();
        String path = request.getRequestURI();

        if (isActually404Error(message, path)) {
            Map<String, Object> response = new HashMap<>();
            response.put("timestamp", java.time.LocalDateTime.now());
            response.put("status", 404);
            response.put("error", "Not Found");
            response.put("message", "Exception: Endpoint not found: " + path);
            response.put("path", path);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", java.time.LocalDateTime.now());
        response.put("status", 500);
        response.put("error", "Internal Server Error");
        response.put("message", message);
        response.put("path", path);

        if (isDevelopment()) {
            response.put("exception", ex.getClass().getName());
            response.put("stackTrace", getFirstLinesOfStackTrace(ex));
        }

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean isActually404Error(String message, String path) {
        if (message == null) return false;
        String lowerMessage = message.toLowerCase();
        return lowerMessage.contains("no static resource")
                || lowerMessage.contains("no handler found")
                || lowerMessage.contains("no mapping for")
                || lowerMessage.contains("resource not found")
                || lowerMessage.contains("404")
                || (path.startsWith("/api/")
                && !lowerMessage.contains("internal server error")
                && !lowerMessage.contains("database")
                && !lowerMessage.contains("sql")
                && !lowerMessage.contains("connection"));
    }

    private boolean isDevelopment() {
        return true;
    }

    private List<String> getFirstLinesOfStackTrace(Exception ex) {
        return Arrays.stream(ex.getStackTrace())
                .limit(5)
                .map(StackTraceElement::toString)
                .collect(Collectors.toList());
    }
}