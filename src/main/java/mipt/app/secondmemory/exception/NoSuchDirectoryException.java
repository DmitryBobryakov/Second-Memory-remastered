package mipt.app.secondmemory.exception;

import lombok.experimental.StandardException;

@StandardException
public class NoSuchDirectoryException extends Exception {
  public NoSuchDirectoryException(String message) {
    super(message);
  }
}
