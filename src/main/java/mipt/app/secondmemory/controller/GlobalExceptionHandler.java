package mipt.app.secondmemory.controller;

import mipt.app.secondmemory.exception.AuthenticationDataMismatchException;
import mipt.app.secondmemory.exception.DatabaseException;
import mipt.app.secondmemory.exception.FileMemoryOverflowException;
import mipt.app.secondmemory.exception.FileNotFoundException;
import mipt.app.secondmemory.exception.FileServerException;
import mipt.app.secondmemory.exception.NoSuchBucketException;
import mipt.app.secondmemory.exception.NoSuchDirectoryException;
import mipt.app.secondmemory.exception.NoSuchFileException;
import mipt.app.secondmemory.exception.TagNotFoundException;
import mipt.app.secondmemory.exception.UserAlreadyExistsException;
import mipt.app.secondmemory.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<String> handleUserNotFound(UserNotFoundException exception) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
  }

  @ExceptionHandler(AuthenticationDataMismatchException.class)
  public ResponseEntity<String> handleDataMismatch(AuthenticationDataMismatchException exception) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getMessage());
  }

  @ExceptionHandler(UserAlreadyExistsException.class)
  public ResponseEntity<String> handleUserAlreadyExists(UserAlreadyExistsException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
  }

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
