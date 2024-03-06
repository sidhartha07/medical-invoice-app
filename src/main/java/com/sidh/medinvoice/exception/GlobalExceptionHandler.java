package com.sidh.medinvoice.exception;

import com.sidh.medinvoice.dto.response.MessageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler(value = {MaxUploadSizeExceededException.class})
    @ResponseBody
    public ResponseEntity<Object> handleMaxUploadSizeException(MaxUploadSizeExceededException ex) {
        logger.warn("Prescription related operation failed: Error - {}", ex.getMessage());
        MessageDto messageDto = MessageDto.builder()
                .status("500")
                .message("Max upload size exceeded. Max size is 5MB")
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("produces", MediaType.APPLICATION_JSON_VALUE)
                .body(messageDto);
    }

    @ExceptionHandler(value = {MissingServletRequestPartException.class})
    @ResponseBody
    public ResponseEntity<Object> handleMaxUploadSizeException(MissingServletRequestPartException ex) {
        logger.warn("Prescription related operation failed: Error - {}", ex.getMessage());
        MessageDto messageDto = MessageDto.builder()
                .status("500")
                .message("Please provide the mandatory fields")
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("produces", MediaType.APPLICATION_JSON_VALUE)
                .body(messageDto);
    }
}
