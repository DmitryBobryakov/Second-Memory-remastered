package mipt.app.secondmemory.controller;

import mipt.app.secondmemory.exception.directory.NoSuchBucketException;
import mipt.app.secondmemory.exception.directory.NoSuchDirectoryException;
import mipt.app.secondmemory.exception.file.DatabaseException;
import mipt.app.secondmemory.exception.file.FileMemoryOverflowException;
import mipt.app.secondmemory.exception.file.FileNotFoundException;
import mipt.app.secondmemory.exception.file.FileAlreadyExistsException;
import mipt.app.secondmemory.exception.session.SessionNotFoundException;
import mipt.app.secondmemory.exception.tag.TagNotFoundException;
import mipt.app.secondmemory.exception.user.AuthenticationDataMismatchException;
import mipt.app.secondmemory.exception.user.UserAlreadyExistsException;
import mipt.app.secondmemory.exception.user.UserNotFoundException;
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

  @ExceptionHandler(FileMemoryOverflowException.class)
  public ResponseEntity<String> handleFileMemoryOverflow(FileMemoryOverflowException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
  }

  @ExceptionHandler(TagNotFoundException.class)
  public ResponseEntity<String> handleTagMismatch(TagNotFoundException exception) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
  }

  @ExceptionHandler(FileNotFoundException.class)
  public ResponseEntity<String> handleFileNotFound(FileNotFoundException exception) {
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

  @ExceptionHandler(SessionNotFoundException.class)
  public ResponseEntity<String> handleSessionNotFoundException(SessionNotFoundException exception) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
  }

  @ExceptionHandler(FileAlreadyExistsException.class)
  public ResponseEntity<String> handleFileAlreadyExistsException(
      FileAlreadyExistsException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
  }
}
