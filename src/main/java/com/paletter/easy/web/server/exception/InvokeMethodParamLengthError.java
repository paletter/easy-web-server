package com.paletter.easy.web.server.exception;

public class InvokeMethodParamLengthError extends RuntimeException {

	private static final long serialVersionUID = -6819309448441198944L;

	public InvokeMethodParamLengthError() {
		super();
	}

	public InvokeMethodParamLengthError(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public InvokeMethodParamLengthError(String message, Throwable cause) {
		super(message, cause);
	}

	public InvokeMethodParamLengthError(Throwable cause) {
		super(cause);
	}

	public InvokeMethodParamLengthError(String message) {
        super(message);
    }
}
