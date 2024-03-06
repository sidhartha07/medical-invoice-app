package com.sidh.medinvoice.service.cloudinary;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CloudinaryService {
    String uploadImage(MultipartFile image) throws IOException;
}
