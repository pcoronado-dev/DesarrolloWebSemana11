package com.tecsup.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> manejarValidacion(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        Map<String, String> errores = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errores.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.badRequest().body(ErrorResponse.of(
                400, "Bad Request", "Datos inválidos", request.getRequestURI(), errores));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> manejarNoEncontrado(
            ResourceNotFoundException ex, HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.of(
                404, "Not Found", ex.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> manejarDuplicado(
            DuplicateResourceException ex, HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponse.of(
                409, "Conflict", ex.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> manejarJsonInvalido(
            HttpMessageNotReadableException ex, HttpServletRequest request) {

        return ResponseEntity.badRequest().body(ErrorResponse.of(
                400, "Bad Request",
                "El cuerpo de la petición no es válido o contiene valores no permitidos",
                request.getRequestURI()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> manejarIntegridad(
            DataIntegrityViolationException ex, HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponse.of(
                409, "Conflict",
                "No se pudo completar la operación por una restricción de integridad de datos",
                request.getRequestURI()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> manejarArgumento(
            IllegalArgumentException ex, HttpServletRequest request) {

        return ResponseEntity.badRequest().body(ErrorResponse.of(
                400, "Bad Request", ex.getMessage(), request.getRequestURI()));
    }
}
