package org.example.exceptions;

public class UnauthorizedException extends RuntimeException {

    private String username;

    public UnauthorizedException(String username) {
        super(String.format("The password and user type combination for user %s is not valid!", username));
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
