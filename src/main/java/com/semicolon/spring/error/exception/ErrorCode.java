package com.semicolon.spring.error.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {
    //Common
    BAD_REQUEST(400, "Bad Request."),
    NOT_FOUND(404, "Not Found."),
    USER_NOT_FOUND(404, "User Not Found."),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error."),

    //Auth
    INVALID_TOKEN(401, "Invalid Token."),

    //Feed
    FEED_NOT_FOUND(404, "Feed Not Found."),

    //File
    FILE_SAVE_FAIL(400, "File Save Fail."),
    BAD_FILE_EXTENSION(400, "Bad File Extension."),
    FILE_NOT_FOUND(404, "File Not Found."),

    //Club
    BAD_RECRUITMENT_TIME(400, "Bad Recruitment Time."),
    APPLICATION_NOT_FOUND(400, "Application Not Found."),
    ALREADY_CLUB_MEMBER(400, "Already Club Member."),
    ALREADY_CLUB_MANAGER(400, "Already Club Manager."),
    DONT_KICK_YOUR_SELF(400, "Call to 1393."),
    NOT_CLUB_MEMBER(403, "Not Club Member."),
    NOT_CLUB_HEAD(403, "Not Club Head."),
    NOT_CLUB_MANAGER(403, "Not Club Manager."),
    CLUB_NOT_FOUND(404, "Club Not Found."),
    CLUB_MANAGER_NOT_FOUND(404, "Club Manager Not Found."),
    CLUB_MEMBER_NOT_FOUND(404, "Club Member Not Found."),

    //Erp
    BAD_SUPPLY_LINK(400, "Bad Supply Link.");

    private final int status;
    private final String message;
}
