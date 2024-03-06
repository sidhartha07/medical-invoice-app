package com.sidh.medinvoice.service.prescription;

import com.sidh.medinvoice.dto.response.MessageDto;
import com.sidh.medinvoice.exception.InvalidRequestException;
import com.sidh.medinvoice.model.user.User;
import com.sidh.medinvoice.repository.prescription.PrescriptionRepository;
import com.sidh.medinvoice.repository.user.UserRepository;
import com.sidh.medinvoice.service.cloudinary.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class PrescriptionServiceImpl implements PrescriptionService {
    @Autowired
    private PrescriptionRepository prescriptionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CloudinaryService cloudinaryService;

    @Override
    @Transactional
    public void insert(String userId, String currentLocation, MultipartFile image) {
        if (!StringUtils.hasText(userId) || image.isEmpty() || !StringUtils.hasText(currentLocation)) {
            MessageDto messageDto = mapToMessageDto("400", "Invalid request, Please provide mandatory fields");
            throw new InvalidRequestException(HttpStatus.BAD_REQUEST, messageDto);
        }
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            MessageDto messageDto = mapToMessageDto("404", "User not found for this Id");
            throw new InvalidRequestException(HttpStatus.NOT_FOUND, messageDto);
        }
        int cnt = userRepository.updateCurrentLocation(userId, currentLocation);
        if (cnt != 1) {
            MessageDto messageDto = mapToMessageDto("500", "Unable to update current location");
            throw new InvalidRequestException(HttpStatus.BAD_REQUEST, messageDto);
        }
        String imgUrl = "";
        try {
            imgUrl = cloudinaryService.uploadImage(image);
        } catch (IOException e) {
            MessageDto messageDto = mapToMessageDto("400", "Invalid request, image is not present");
            throw new InvalidRequestException(HttpStatus.BAD_REQUEST, messageDto);
        }
        if (StringUtils.hasText(imgUrl)) {
            try {
                prescriptionRepository.insert(userId, imgUrl);
            } catch (RuntimeException ex) {
                MessageDto messageDto = mapToMessageDto("500", "Internal Server Error");
                throw new InvalidRequestException(HttpStatus.INTERNAL_SERVER_ERROR, messageDto);
            }
        }
    }

    private static MessageDto mapToMessageDto(String status, String message) {
        return MessageDto.builder()
                .status(status)
                .message(message)
                .build();
    }
}
