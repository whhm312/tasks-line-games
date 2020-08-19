package me.line.games.common.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import me.line.games.common.vo.CommonResponse;
import me.line.games.common.vo.ErrorResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(FailedCreateContentException.class)
	protected ResponseEntity<CommonResponse> handleFailedCreateContentException(FailedCreateContentException e) {
		final CommonResponse response = new CommonResponse();
		response.setCode("E0001");
		response.setMessage(e.getMessage());
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ExceptionHandler(ResourceNoContentException.class)
	protected void handleResourceNoContentException(ResourceNoContentException e) {
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ExceptionHandler(EmptyResultDataAccessException.class)
	protected void handleEmptyResultDataAccessException(EmptyResultDataAccessException e) {
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		ErrorResponse reponse = new ErrorResponse();
		List<String> errors = new ArrayList<>();

		BindingResult bindingResult = e.getBindingResult();
		StringBuilder builder = new StringBuilder();
		for (FieldError fieldError : bindingResult.getFieldErrors()) {
			builder.append("[");
			builder.append(fieldError.getField());
			builder.append("] ");
			builder.append(fieldError.getDefaultMessage());
			errors.add(builder.toString());
			builder.setLength(0);
		}
		reponse.setErrors(errors);

		return new ResponseEntity<>(reponse, HttpStatus.BAD_REQUEST);
	}
}
