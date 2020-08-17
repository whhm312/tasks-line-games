package me.line.games.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class FailedCreateContentException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public FailedCreateContentException() {
		super();
	}

	public FailedCreateContentException(String message, Throwable cause) {
		super(message, cause);
	}

	public FailedCreateContentException(String message) {
		super(message);
	}

	public FailedCreateContentException(Throwable cause) {
		super(cause);
	}
}
