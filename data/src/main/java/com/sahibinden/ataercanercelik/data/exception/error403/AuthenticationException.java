package com.sahibinden.ataercanercelik.data.exception.error403;

public class AuthenticationException extends Exception {

  public AuthenticationException() {
    super();
  }

  public AuthenticationException(final String message) {
    super(message);
  }

  public AuthenticationException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public AuthenticationException(final Throwable cause) {
    super(cause);
  }
}
