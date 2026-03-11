package com.kcn.clients_api.exception;

import java.util.List;

public class InvalidClientDataException extends RuntimeException {

    private final List<String> errors;

    public InvalidClientDataException(List<String> errors) {
        super("Invalid client data");
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}

