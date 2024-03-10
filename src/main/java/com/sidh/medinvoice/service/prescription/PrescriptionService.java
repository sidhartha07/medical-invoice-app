package com.sidh.medinvoice.service.prescription;

import com.sidh.medinvoice.dto.response.UserPresRespDto;
import org.springframework.web.multipart.MultipartFile;

public interface PrescriptionService {
    String insert(String userId, String currentLocation, MultipartFile image);
    UserPresRespDto findPrescriptionsByUserId(String userId);
}
