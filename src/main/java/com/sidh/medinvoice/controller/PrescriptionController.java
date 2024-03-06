package com.sidh.medinvoice.controller;

import com.sidh.medinvoice.dto.response.MessageDto;
import com.sidh.medinvoice.exception.InvalidRequestException;
import com.sidh.medinvoice.service.prescription.PrescriptionService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/prescription")
public class PrescriptionController {
    private static final Logger logger = LoggerFactory.getLogger(PrescriptionController.class);
    @Autowired
    private PrescriptionService prescriptionService;

    @PostMapping(value = "/insert", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "200", description = "Prescription save successful",
            content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                      "status": "200",
                      "message": "Prescription saved successfully"
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Mandatory fields are missing",
            content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                      "status": "400",
                      "message": "Invalid request, Please provide mandatory fields"
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Image is missing",
            content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                      "status": "400",
                      "message": "Invalid request, image is not present"
                    }
                    """)))
    @ApiResponse(responseCode = "404", description = "User not found",
            content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                      "status": "404",
                      "message": "User not found for this Id"
                    }
                    """)))
    @ApiResponse(responseCode = "500", description = "Failed to update current location",
            content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                      "status": "500",
                      "message": "Unable to update current location"
                    }
                    """)))
    @ApiResponse(responseCode = "500", description = "Max upload size exceeded",
            content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                      "status": "500",
                      "message": "Max upload size exceeded. Max size is 5MB"
                    }
                    """)))
    @ApiResponse(responseCode = "500", description = "Internal Server Error",
            content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                      "status": "500",
                      "message": "Internal Server Error"
                    }
                    """)))
    @ApiResponse(responseCode = "500", description = "Mandatory fields are missing",
            content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                      "status": "500",
                      "message": "Please provide the mandatory fields"
                    }
                    """)))
    public ResponseEntity<Object> insert(
            @RequestPart(value = "userId") String userId,
            @RequestPart(value = "currentLocation") String currentLocation,
            @RequestPart(value = "image") MultipartFile image){
        prescriptionService.insert(userId, currentLocation, image);
        MessageDto response = MessageDto.builder()
                .status("200")
                .message("Prescription saved successfully")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @ExceptionHandler(value = {InvalidRequestException.class})
    @ResponseBody
    public ResponseEntity<Object> handleExceptions(InvalidRequestException ex) {
        logger.warn("Prescription related operation failed: Error - {}", ex.getError());
        return ResponseEntity.status(ex.getStatus().value())
                .header("produces", MediaType.APPLICATION_JSON_VALUE)
                .body(ex.getMessageDto());
    }
}
