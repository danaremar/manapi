package com.manapi.manapigateway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.TOO_MANY_REQUESTS)
public class QuotaExceededException extends Exception {
    
    public QuotaExceededException() {
        super("Cuota exceeded");
    }

    public QuotaExceededException(String message) {
        super(message);
    }
}
