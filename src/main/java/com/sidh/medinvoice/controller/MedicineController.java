package com.sidh.medinvoice.controller;

import com.sidh.medinvoice.dto.response.MedicineRespDto;
import com.sidh.medinvoice.dto.response.MessageDto;
import com.sidh.medinvoice.exception.InvalidRequestException;
import com.sidh.medinvoice.service.medicine.MedicineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController("/api/v1/medicines")
public class MedicineController {
    private static final Logger logger = LoggerFactory.getLogger(MedicineController.class);
    @Autowired
    private MedicineService medicineService;

    @GetMapping("/{userId}")
    public ResponseEntity<Object> findMedicines(@PathVariable String userId) {
        if(!StringUtils.hasText(userId)) {
            MessageDto messageDto = MessageDto.builder()
                    .status("400")
                    .message("Invalid request. Please provide mandatory fields")
                    .build();
            throw new InvalidRequestException(HttpStatus.BAD_REQUEST, messageDto);
        }
        MedicineRespDto response = medicineService.findMedicinesByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @ExceptionHandler(value = {InvalidRequestException.class})
    @ResponseBody
    public ResponseEntity<Object> handleExceptions(InvalidRequestException ex) {
        logger.warn("Medicine related operation failed: Error - {}", ex.getError());
        return ResponseEntity.status(ex.getStatus().value())
                .header("produces", MediaType.APPLICATION_JSON_VALUE)
                .body(ex.getMessageDto());
    }
}
