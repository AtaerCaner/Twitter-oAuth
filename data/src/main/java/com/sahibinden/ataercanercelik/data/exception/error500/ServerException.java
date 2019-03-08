package com.sahibinden.ataercanercelik.data.exception.error500;

public class ServerException extends Exception {

  public ServerException() {
    super();
  }

  public ServerException(final String message) {
    super(message);
  }

  public ServerException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public ServerException(final Throwable cause) {
    super(cause);
  }
}
