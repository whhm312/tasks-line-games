package me.line.games.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NO_CONTENT)
public class ResourceNoContentException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ResourceNoContentException() {
		super();
	}

	public ResourceNoContentException(String message, Throwable cause) {
		super(message, cause);
	}

	public ResourceNoContentException(String message) {
		super(message);
	}

	public ResourceNoContentException(Throwable cause) {
		super(cause);
	}
}
