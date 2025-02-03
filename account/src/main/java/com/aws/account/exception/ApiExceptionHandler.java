package com.aws.account.exception;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import com.aws.account.ViewModel.Error.ErrorVm;

import lombok.extern.slf4j.Slf4j;

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

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorVm> handleNotFoundException(Exception ex, WebRequest request) {
    return handleBadRequest(ex, request);
  }

  @ExceptionHandler(AccountIsNotActive.class)
  public ResponseEntity<ErrorVm> handleAccountIsNotActiveException(Exception ex, WebRequest request) {
    return handleBadRequest(ex, request);
  }

  private ResponseEntity<ErrorVm> handleBadRequest(Exception ex, WebRequest request) {
    HttpStatus status = HttpStatus.BAD_REQUEST;
    String message = ex.getMessage();

    return buildErrorResponse(status, message, null, ex, request, 400);
  }

  private ResponseEntity<ErrorVm> buildErrorResponse(HttpStatus status, String message, List<String> errors,
      Exception ex, WebRequest request, int statusCode) {
    ErrorVm errorVm = new ErrorVm(status.toString(), status.getReasonPhrase(), message, errors);

    if (request != null) {
      log.error(ERROR_LOG_FORMAT, this.getServletPath(request), statusCode, message);
    }

    return ResponseEntity.status(status).body(errorVm);
  }

  private String getServletPath(WebRequest request) {
    ServletWebRequest servletRequest = (ServletWebRequest) request;
    return servletRequest.getRequest().getServletPath();
  }
}