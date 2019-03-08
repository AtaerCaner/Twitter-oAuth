package com.sahibinden.ataercanercelik.data.exception.error412;

public class CustomerAlreadyExistException extends Exception {

  public CustomerAlreadyExistException() {
    super();
  }

  public CustomerAlreadyExistException(final String message) {
    super(message);
  }

  public CustomerAlreadyExistException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public CustomerAlreadyExistException(final Throwable cause) {
    super(cause);
  }
}
