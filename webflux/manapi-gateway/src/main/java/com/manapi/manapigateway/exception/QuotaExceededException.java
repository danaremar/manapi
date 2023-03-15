package com.manapi.manapigateway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.manapi.manapigateway.configuration.ManapiMessages;

@ResponseStatus(value = HttpStatus.TOO_MANY_REQUESTS)
public class QuotaExceededException extends Exception {
    
    public QuotaExceededException() {
        super(ManapiMessages.QUOTA_EXCEEDED);
    }

    public QuotaExceededException(String message) {
        super(message);
    }
}
