package mipt.app.secondmemory.exception;

import lombok.experimental.StandardException;

@StandardException
public class NoSuchBucketException extends Exception {
  public NoSuchBucketException(String message) {
    super(message);
  }
}
