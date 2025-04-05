package dev.felix2000jp.springboottemplate.security.domain.exceptions;

public class AppuserAlreadyExistsException extends RuntimeException {

    public AppuserAlreadyExistsException() {
        super("SecurityUser already exists");
    }

}
