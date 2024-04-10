package com.sidh.medinvoice.controller;

import com.sidh.medinvoice.dto.request.MedicineInsertReqDto;
import com.sidh.medinvoice.dto.response.MedicineDto;
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

@RestController
@RequestMapping("/api/v1/medicines")
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

    @PostMapping("/insert")
    public ResponseEntity<Object> insertMedicine(@RequestBody MedicineInsertReqDto request) {
        if (!StringUtils.hasText(request.getUserId()) ||
                !StringUtils.hasText(request.getName()) ||
                !StringUtils.hasText(request.getDescription()) ||
                request.getMrp() == null ||
                request.getMrp() == 0 ||
                request.getSellingPrice() == null ||
                request.getSellingPrice() == 0 ||
                request.getQuantity() == null ||
                request.getQuantity() == 0) {
            MessageDto messageDto = MessageDto.builder()
                    .status("400")
                    .message("Invalid request. Please provide mandatory fields")
                    .build();
            throw new InvalidRequestException(HttpStatus.BAD_REQUEST, messageDto);
        }
        medicineService.addMedicine(request);
        MessageDto messageDto = MessageDto.builder()
                .status("200")
                .message("Medicine has been inserted successfully")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(messageDto);
    }

    @GetMapping("/find/{medicineId}")
    public ResponseEntity<Object> findMedicineByMedicineId(@PathVariable String medicineId) {
        if(!StringUtils.hasText(medicineId)) {
            MessageDto messageDto = MessageDto.builder()
                    .status("400")
                    .message("Invalid request. Please provide mandatory fields")
                    .build();
            throw new InvalidRequestException(HttpStatus.BAD_REQUEST, messageDto);
        }
        MedicineDto response = medicineService.findMedicineByMedicineId(medicineId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/update")
    public ResponseEntity<Object> updateMedicine(@RequestBody MedicineDto request) {
        if (!StringUtils.hasText(request.getMedicineId()) ||
                !StringUtils.hasText(request.getUserId()) ||
                !StringUtils.hasText(request.getName()) ||
                !StringUtils.hasText(request.getDescription()) ||
                request.getMrp() == null ||
                request.getMrp() == 0 ||
                request.getSellingPrice() == null ||
                request.getSellingPrice() == 0 ||
                request.getQuantity() == null ||
                request.getQuantity() == 0) {
            MessageDto messageDto = MessageDto.builder()
                    .status("400")
                    .message("Invalid request. Please provide mandatory fields")
                    .build();
            throw new InvalidRequestException(HttpStatus.BAD_REQUEST, messageDto);
        }
        medicineService.updateMedicine(request);
        MessageDto messageDto = MessageDto.builder()
                .status("200")
                .message("Medicine has been updated successfully")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(messageDto);
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
