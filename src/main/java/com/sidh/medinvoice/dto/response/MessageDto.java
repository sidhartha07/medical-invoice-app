package com.sidh.medinvoice.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@Builder
public class MessageDto implements Serializable {

    private String status;
    private String message;

    public MessageDto() {
    }

    public MessageDto(final String status, final String message) {
        this.status = status;
        this.message = message;
    }
}
