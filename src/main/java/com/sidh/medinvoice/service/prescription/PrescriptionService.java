package com.sidh.medinvoice.service.prescription;

import org.springframework.web.multipart.MultipartFile;

public interface PrescriptionService {
    void insert(String userId, String currentLocation, MultipartFile image);
}