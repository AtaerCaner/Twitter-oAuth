package com.sahibinden.ataercanercelik.data.exception.error412;

public class NoCustomerFoundException extends Exception {

  public NoCustomerFoundException() {
    super();
  }

  public NoCustomerFoundException(final String message) {
    super(message);
  }

  public NoCustomerFoundException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public NoCustomerFoundException(final Throwable cause) {
    super(cause);
  }
}
