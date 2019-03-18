package br.com.test.service.exception;

import org.springframework.http.HttpStatus;

public class AlreadyExistException extends BusinessException {
  public AlreadyExistException(String errorCode) {
    super(errorCode, HttpStatus.BAD_REQUEST, null, null);
  }
}
