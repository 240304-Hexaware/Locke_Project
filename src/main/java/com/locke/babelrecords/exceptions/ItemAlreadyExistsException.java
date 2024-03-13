package com.locke.babelrecords.exceptions;

import javax.naming.AuthenticationException;

public class ItemAlreadyExistsException extends AuthenticationException {
    public ItemAlreadyExistsException(final String msg) {
        super(msg);
    }
}
