package mipt.app.secondmemory.exception;

import lombok.experimental.StandardException;

@StandardException
public class DatabaseException extends Exception {
  public DatabaseException(String message) {
    super(message);
  }
}
