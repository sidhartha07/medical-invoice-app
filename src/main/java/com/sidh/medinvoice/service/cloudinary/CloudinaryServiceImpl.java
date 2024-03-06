package com.sidh.medinvoice.service.cloudinary;

import com.cloudinary.Cloudinary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {
    private static final Logger logger = LoggerFactory.getLogger(CloudinaryServiceImpl.class);
    @Autowired
    private Cloudinary cloudinary;
    @Override
    public String uploadImage(MultipartFile image) throws IOException {
        try {
            Map<Object, Object> options = new HashMap<>();
            options.put("folder", "med-inv");
            Map uploadedImage = cloudinary.uploader().upload(image.getBytes(), options);
            String publicId = (String) uploadedImage.get("public_id");
            return cloudinary.url().secure(true).generate(publicId);
        } catch (IOException ex) {
            logger.warn("Image upload failed with Error - {}", ex.getMessage());
            throw ex;
        }
    }
}
