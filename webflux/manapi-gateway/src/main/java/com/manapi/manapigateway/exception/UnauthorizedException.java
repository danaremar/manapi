package com.manapi.manapigateway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.manapi.manapigateway.configuration.ManapiMessages;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends Exception {

    public UnauthorizedException() {
        super(ManapiMessages.NOT_AUTHORIZED);
    }

    public UnauthorizedException(String message) {
        super(message);
    }
    
}
