package com.domain.exception;

public interface ErrorBundle {
  Exception getException();

  String getErrorMessage();
}
