package mipt.app.secondmemory.controller;

import mipt.app.secondmemory.exception.FileMemoryOverflowException;
import mipt.app.secondmemory.exception.FileNotFoundException;
import mipt.app.secondmemory.exception.FileServerException;
import mipt.app.secondmemory.exception.TagNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<String> handleFileNotFound(FileNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(FileMemoryOverflowException.class)
    public ResponseEntity<String> handleFileMemoryOverflow(FileMemoryOverflowException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }
    @ExceptionHandler(TagNotFoundException.class)
    public ResponseEntity<String> handleTagMismatch(TagNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }
    @ExceptionHandler(FileServerException.class)
    public ResponseEntity<String> handleFileNotSuccessUpload(FileServerException exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
    }
}