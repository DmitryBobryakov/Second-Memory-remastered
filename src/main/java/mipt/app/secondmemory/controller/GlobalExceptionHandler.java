package mipt.app.secondmemory.controller;

import mipt.app.secondmemory.exception.DatabaseException;
import mipt.app.secondmemory.exception.NoSuchBucketException;
import mipt.app.secondmemory.exception.NoSuchDirectoryException;
import mipt.app.secondmemory.exception.NoSuchFileException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(NoSuchFileException.class)
  public ResponseEntity<String> handleNoSuchFileException(NoSuchFileException exception) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
  }

  @ExceptionHandler(DatabaseException.class)
  public ResponseEntity<String> handleDatabaseException(DatabaseException exception) {
    return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(exception.getMessage());
  }

  @ExceptionHandler(NoSuchDirectoryException.class)
  public ResponseEntity<String> handleNoSuchDirectoryException(NoSuchDirectoryException exception) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
  }

  @ExceptionHandler(NoSuchBucketException.class)
  public ResponseEntity<String> handleNoSuchBucketException(NoSuchBucketException exception) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
  }
}