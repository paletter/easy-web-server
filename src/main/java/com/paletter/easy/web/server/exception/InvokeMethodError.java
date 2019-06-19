package com.paletter.easy.web.server.exception;

public class InvokeMethodError extends RuntimeException {

	private static final long serialVersionUID = -6819309448441198944L;

	public InvokeMethodError() {
		super();
	}

	public InvokeMethodError(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public InvokeMethodError(String message, Throwable cause) {
		super(message, cause);
	}

	public InvokeMethodError(Throwable cause) {
		super(cause);
	}

	public InvokeMethodError(String message) {
        super(message);
    }
}
