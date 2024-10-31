package com.aws.account.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import com.aws.account.ViewModel.Error.ErrorVm;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@ControllerAdvice
@Slf4j
public class ApiExceptionHandler {
  private static final String ERROR_LOG_FORMAT = "Error: URI: {}, ErrorCode: {}, Message: {}";

  @ExceptionHandler(CheckExistException.class)
  public ResponseEntity<ErrorVm> handleExistException(CheckExistException e, WebRequest request) {
    String message = e.getMessage();
    ErrorVm errorVm = new ErrorVm(HttpStatus.BAD_REQUEST.toString(), "Exist Exception", message);
    log.warn(ERROR_LOG_FORMAT, this.getServletPath(request), 400, message);
    return ResponseEntity.badRequest().body(errorVm);
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ErrorVm> handleBadRequestException(BadRequestException ex, WebRequest request) {
    String message = ex.getMessage();
    log.warn(ERROR_LOG_FORMAT, this.getServletPath(request), 400, message);
    ErrorVm errorVm = new ErrorVm(HttpStatus.BAD_REQUEST.toString(), "BadRequest", message);
    return ResponseEntity.badRequest().body(errorVm);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
      WebRequest request) {
    List<String> errors = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(error -> error.getField() + " " + error.getDefaultMessage())
        .toList();

    ErrorVm errorVm = new ErrorVm("400", "Bad Request", "Request information is not valid", errors);
    return ResponseEntity.badRequest().body(errorVm);
  }

  private String getServletPath(WebRequest request) {
    ServletWebRequest servletRequest = (ServletWebRequest) request;
    return servletRequest.getRequest().getServletPath();
  }
}