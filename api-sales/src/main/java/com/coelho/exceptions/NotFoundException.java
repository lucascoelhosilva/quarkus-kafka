package com.coelho.exceptions;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.WebApplicationException;

public class NotFoundException extends WebApplicationException {

    private static final long serialVersionUID = 1L;

    public NotFoundException(String message) {
        super(message, HttpServletResponse.SC_NOT_FOUND);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause, HttpServletResponse.SC_NOT_FOUND);
    }

}