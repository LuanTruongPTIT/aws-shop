package com.aws.identity.exception;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import com.aws.identity.view.Error.ErrorVm;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class ApiExceptionHandler {
  private static final String ERROR_LOG_FORMAT = "Error: URI: {}, ErrorCode: {}, Message: {}";

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ErrorVm> handleBadRequestException(BadRequestException ex, WebRequest request) {
    String message = ex.getMessage();
    log.warn(ERROR_LOG_FORMAT, this.getServletPath(request), 400, message);
    ErrorVm errorVm = new ErrorVm(HttpStatus.BAD_REQUEST.toString(), "BadRequest", message);
    return ResponseEntity.badRequest().body(errorVm);
  }

  private String getServletPath(WebRequest request) {
    ServletWebRequest servletRequest = (ServletWebRequest) request;
    return servletRequest.getRequest().getServletPath();
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorVm> handleNotFoundException(Exception ex, WebRequest request) {
    return handleBadRequest(ex, request);
  }

  @ExceptionHandler(PasswordNotMatch.class)

  public ResponseEntity<ErrorVm> handlePasswordNotMatch() {
    return buildErrorResponse(HttpStatus.BAD_REQUEST, "Password does not match", null, null, null, 400);
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
}
