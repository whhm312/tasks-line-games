package me.line.games.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import me.line.games.common.vo.CommonResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(FailedCreateContentException.class)
	protected ResponseEntity<CommonResponse> handleFailedCreateContentException(FailedCreateContentException e) {
		final CommonResponse response = new CommonResponse();
		response.setCode("E0001");
		response.setMessage(e.getMessage());
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(ResourceNoContentException.class)
	protected ResponseEntity<CommonResponse> handleResourceNoContentException(ResourceNoContentException e) {
		final CommonResponse response = new CommonResponse();
		response.setCode("E0002");
		response.setMessage(e.getMessage());
		return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
	}
}
