package com.sidh.medinvoice.exception;

import com.sidh.medinvoice.dto.response.MessageDto;
import com.sidh.medinvoice.dto.response.ResponseMsgDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InvalidRequestException extends RuntimeException {
    private static final long serialVersionUID = 30L;

    private final HttpStatus status;
    private final transient Object error;
    private final MessageDto messageDto;

    public InvalidRequestException(String message) {
        super(message);
        status = HttpStatus.BAD_REQUEST;
        this.error = message;
        this.messageDto = null;
    }

    public InvalidRequestException(HttpStatus status, String message) {
        super(message);
        this.status = status;
        this.error = message;
        this.messageDto = null;
    }

    public InvalidRequestException(HttpStatus status, Exception e, MessageDto messageDto) {
        super(e);
        this.status = status;
        this.error = e;
        this.messageDto = messageDto;
    }

    public InvalidRequestException(HttpStatus status, Error e, ResponseMsgDto responseMsgDto) {
        super(responseMsgDto.getException());
        this.status = status;
        this.error = e;
        this.messageDto = null;
    }

    public InvalidRequestException(HttpStatus status, MessageDto messageDto) {
        super(messageDto.getMessage());
        this.status = status;
        this.error = messageDto;
        this.messageDto = messageDto;

    }
}
