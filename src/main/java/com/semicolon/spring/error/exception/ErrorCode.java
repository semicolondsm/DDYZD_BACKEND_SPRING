package com.semicolon.spring.error.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {
    //Common
    BAD_REQUEST(400, "BadRequest."),
    USER_NOT_FOUND(404, "User Not Found."),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error."),

    //Auth
    NO_AUTHORITY(403, "No Authority."),
    INVALID_TOKEN(401, "Invalid Token."),

    //Main
    CLUB_NOT_FOUND(404, "Club Not Found."),
    NOT_CLUB_MEMBER(403, "Not Club Member."),
    NOT_CLUB_HEAD(403, "Not Club Head."),
    FEED_NOT_FOUND(404, "Feed Not Found."),

    //File
    FILE_SAVE_FAIL(400, "File Save Fail."),
    FILE_NOT_FOUND(404, "File Not Fail."),
    BAD_FILE_EXTENSION(400, "Bad File Extension");

    private final int status;
    private final String message;
}
