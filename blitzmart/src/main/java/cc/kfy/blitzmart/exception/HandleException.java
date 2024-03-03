package cc.kfy.blitzmart.exception;

import cc.kfy.blitzmart.domain.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
@Slf4j
public class HandleException extends ResponseEntityExceptionHandler implements ErrorController {
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception exception, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
		log.error(exception.getMessage());
		return new ResponseEntity<>(
						HttpResponse.builder()
										.timeStamp(now().toString())
										.reason(exception.getMessage())
										.developerMessage(exception.getMessage())
										.status(resolve(statusCode.value()))
										.statusCode(statusCode.value())
										.build(), statusCode);
		//return super.handleExceptionInternal(ex, body, headers, statusCode, request);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
		log.error(exception.getMessage());
		List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
		String fieldMessage = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(", "));
		return new ResponseEntity<>(
						HttpResponse.builder()
										.timeStamp(now().toString())
										.reason(fieldMessage)
										.developerMessage(exception.getMessage())
										.status(resolve(statusCode.value()))
										.statusCode(statusCode.value())
										.build(), statusCode);
//		return super.handleMethodArgumentNotValid(ex, headers, status, request);
	}

	@ExceptionHandler(SQLIntegrityConstraintViolationException.class)
	public ResponseEntity<Object> sqlIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException exception) {
		log.error(exception.getMessage());
		return new ResponseEntity<>(
						HttpResponse.builder()
										.timeStamp(now().toString())
										.reason(exception.getMessage().contains("Duplicate entry") ? "Information already exist." : exception.getMessage())
										.developerMessage(exception.getMessage())
										.status(BAD_REQUEST)
										.statusCode(BAD_REQUEST.value())
										.build(), BAD_REQUEST);
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<Object> badCredentialsException(BadCredentialsException exception) {
		log.error(exception.getMessage());
		return new ResponseEntity<>(
						HttpResponse.builder()
										.timeStamp(now().toString())
										.reason(exception.getMessage() + ", Incorrect email or password")
										.developerMessage(exception.getMessage())
										.status(BAD_REQUEST)
										.statusCode(BAD_REQUEST.value())
										.build(), BAD_REQUEST);
	}

	@ExceptionHandler(ApiException.class)
	public ResponseEntity<Object> apiException(ApiException exception) {
		log.error(exception.getMessage());
		return new ResponseEntity<>(
						HttpResponse.builder()
										.timeStamp(now().toString())
										.reason(exception.getMessage())
										.developerMessage(exception.getMessage())
										.status(BAD_REQUEST)
										.statusCode(BAD_REQUEST.value())
										.build(), BAD_REQUEST);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<Object> accessDeniedException(AccessDeniedException exception) {
		log.error(exception.getMessage());
		return new ResponseEntity<>(
						HttpResponse.builder()
										.timeStamp(now().toString())
										.reason("Access denied. You don't have access")
										.developerMessage(exception.getMessage())
										.status(FORBIDDEN)
										.statusCode(FORBIDDEN.value())
										.build(), FORBIDDEN);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> exception(Exception exception) {
		log.error(exception.getMessage());
		return new ResponseEntity<>(
						HttpResponse.builder()
										.timeStamp(now().toString())
										.reason(exception.getMessage() != null ?
														(exception.getMessage().contains("expected 1, actual 0") ?
																		"Record not found" : exception.getMessage())
														: "Some error occurred")
										.developerMessage(exception.getMessage())
										.status(INTERNAL_SERVER_ERROR)
										.statusCode(INTERNAL_SERVER_ERROR.value())
										.build(), INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(EmptyResultDataAccessException.class)
	public ResponseEntity<Object> emptyResultDataAccessException(EmptyResultDataAccessException exception) {
		log.error(exception.getMessage());
		return new ResponseEntity<>(
						HttpResponse.builder()
										.timeStamp(now().toString())
										.reason(exception.getMessage().contains("expected 1, actual 0") ? "Record not found" : exception.getMessage())
										.developerMessage(exception.getMessage())
										.status(BAD_REQUEST)
										.statusCode(BAD_REQUEST.value())
										.build(), BAD_REQUEST);
	}

}