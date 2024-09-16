package br.edu.ifba.todolist.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.ArrayList;

@ControllerAdvice("/tasks")
public class TaskControllerAdvice {

    @ExceptionHandler(value = {TaskNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleNotFoundException(TaskNotFoundException ex) {
        ErrorResponse error = new ErrorResponse("Resource not found", ex.getMessage(), null);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> errorsField = new ArrayList<>();
        for (var error : ex.getFieldErrors()) {
            errorsField.add(String.format("%s: %s", error.getField(), error.getDefaultMessage()));
        }
        ErrorResponse error = new ErrorResponse("Validation error", ex.getMessage(), errorsField);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        ErrorResponse error = new ErrorResponse("Unspecified error", ex.getMessage(), null);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
