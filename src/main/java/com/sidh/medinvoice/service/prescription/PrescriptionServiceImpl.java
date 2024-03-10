package com.sidh.medinvoice.service.prescription;

import com.sidh.medinvoice.dto.response.InvoiceDto;
import com.sidh.medinvoice.dto.response.MessageDto;
import com.sidh.medinvoice.dto.response.PrescriptionDto;
import com.sidh.medinvoice.dto.response.UserPresRespDto;
import com.sidh.medinvoice.exception.InvalidRequestException;
import com.sidh.medinvoice.model.invoice.Invoice;
import com.sidh.medinvoice.model.prescription.Prescription;
import com.sidh.medinvoice.model.user.User;
import com.sidh.medinvoice.model.user.UserRep;
import com.sidh.medinvoice.repository.invoice.InvoiceRepository;
import com.sidh.medinvoice.repository.prescription.PrescriptionRepository;
import com.sidh.medinvoice.repository.user.UserRepository;
import com.sidh.medinvoice.service.cloudinary.CloudinaryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PrescriptionServiceImpl implements PrescriptionService {
    private static final Logger logger = LoggerFactory.getLogger(PrescriptionServiceImpl.class);
    @Autowired
    private PrescriptionRepository prescriptionRepository;
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CloudinaryService cloudinaryService;

    @Override
    @Transactional
    public String insert(String userId, String currentLocation, MultipartFile image) {
        if (!StringUtils.hasText(userId) || image == null || !StringUtils.hasText(currentLocation)) {
            MessageDto messageDto = mapToMessageDto("400", "Invalid request, Please provide mandatory fields");
            throw new InvalidRequestException(HttpStatus.BAD_REQUEST, messageDto);
        }
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            MessageDto messageDto = mapToMessageDto("404", "User not found for this Id");
            throw new InvalidRequestException(HttpStatus.NOT_FOUND, messageDto);
        }
        String[] geoValues = currentLocation.split(",");
        Double latitude = Double.valueOf(geoValues[0]);
        Double longitude = Double.valueOf(geoValues[1]);
        String imgUrl = "";
        try {
            imgUrl = cloudinaryService.uploadImage(image);
        } catch (IOException e) {
            MessageDto messageDto = mapToMessageDto("400", "Invalid request, image is not present");
            throw new InvalidRequestException(HttpStatus.BAD_REQUEST, messageDto);
        }
        String prescriptionId = "";
        if (StringUtils.hasText(imgUrl)) {
            try {
                prescriptionId = prescriptionRepository.insert(userId, imgUrl, latitude, longitude);
            } catch (RuntimeException ex) {
                MessageDto messageDto = mapToMessageDto("500", "Internal Server Error");
                throw new InvalidRequestException(HttpStatus.INTERNAL_SERVER_ERROR, messageDto);
            }
        }
        List<User> reps = userRepository.findRepsNearUser(latitude, longitude);
        if (!CollectionUtils.isEmpty(reps)) {
            logger.info("reps : {}", reps);
            List<UserRep> userReps = mapToUserRepEntity(reps, userId, prescriptionId);
            int rslt = prescriptionRepository.saveRepsForUser(userReps);
            logger.info("{} reps saved for user with userId {}", rslt, userId);
        } else {
            logger.info("No REPs found near this location");
        }
        return imgUrl;
    }

    private List<UserRep> mapToUserRepEntity(List<User> reps, String userId, String prescriptionId) {
        return reps.stream().map(rep -> UserRep.builder()
                .userId(userId)
                .prescriptionId(prescriptionId)
                .medicalRepId(rep.getUserId())
                .build()).toList();
    }

    @Override
    public UserPresRespDto findPrescriptionsByUserId(String userId) {
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            MessageDto messageDto = mapToMessageDto("404", "User not found for this Id");
            throw new InvalidRequestException(HttpStatus.NOT_FOUND, messageDto);
        }
        List<Prescription> prescriptions = prescriptionRepository.getPrescriptionByUserId(userId);
        List<PrescriptionDto> prescriptionDtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(prescriptions)) {
            for (Prescription prescription : prescriptions) {
                PrescriptionDto prescriptionDto = PrescriptionDto.builder().build();
                List<Invoice> invoices = invoiceRepository.getInvoiceByPrescriptionId(prescription.getPrescriptionId());
                if (!CollectionUtils.isEmpty(invoices)) {
                    prescriptionDto.setInvoices(mapToInvoiceDtos(invoices));
                }
                prescriptionDto.setPrescriptionId(prescription.getPrescriptionId());
                prescriptionDto.setImageUrl(prescription.getPrescriptionImgUrl());
                prescriptionDtos.add(prescriptionDto);
            }
        }
        return UserPresRespDto.builder()
                .status("200")
                .message("Prescriptions fetched successfully")
                .prescriptions(prescriptionDtos)
                .build();
    }

    private List<InvoiceDto> mapToInvoiceDtos(List<Invoice> invoices) {
        List<InvoiceDto> invoiceDtos = new ArrayList<>();
        for (Invoice invoice : invoices) {
            InvoiceDto invoiceDto = InvoiceDto.builder()
                    .invoiceId(invoice.getInvoiceId())
                    .invoiceNo(invoice.getInvoiceNo())
                    .invoiceJson(invoice.getInvoiceJson())
                    .build();
            invoiceDtos.add(invoiceDto);
        }
        return invoiceDtos;
    }

    private static MessageDto mapToMessageDto(String status, String message) {
        return MessageDto.builder()
                .status(status)
                .message(message)
                .build();
    }
}
