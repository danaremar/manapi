package com.manapi.manapigateway.configuration;

public class ManapiMessages {

    private ManapiMessages() {
    }

    // AUTH
    public static final String NOT_AUTHORIZED = "Not authorized";
    public static final String MUST_BE_LOGGED = "User must be logged to perform this action";
    public static final String TOKEN_EXPIRED = "Token expired";
    public static final String TOKEN_BAD_FORMAT = "Token given is unsupported";
    public static final String RATE_EXCEEDED = "Rate cuota was exceeded";

    // USER
    public static final String USER_NOT_FOUND_MESSAGE = "User doesn't found";
    public static final String USER_DUPLICATED_EMAIL_MESSAGE = "Email is duplicated";
    public static final String USER_USERNAME_DUPLICATED_MESSAGE = "Username is duplicated";
    public static final String USER_DISABLED_MESSAGE = "User has been disabled";
    public static final String USER_NOT_ALLOWED = "Your user isn't allowed to perform this operation";
    public static final String USER_PASSWORD_INCORRECT = "Your password is not correct";
    public static final String USER_OLD_PASSWORD_INCORRECT = "Old password doesn't match with actual";

    // IMAGE
    public static final String IMAGE_CANNOT_UPDATED = "Image cannot be updated";
    public static final String IMAGE_CANNOT_DELETED = "Image cannot be deleted";

}
