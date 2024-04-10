package com.sidh.medinvoice.service.medicine;

import com.sidh.medinvoice.dto.request.MedicineInsertReqDto;
import com.sidh.medinvoice.dto.response.MedicineDto;
import com.sidh.medinvoice.dto.response.MedicineRespDto;
import com.sidh.medinvoice.dto.response.MessageDto;
import com.sidh.medinvoice.exception.InvalidRequestException;
import com.sidh.medinvoice.model.medicine.Medicine;
import com.sidh.medinvoice.repository.medicine.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class MedicineServiceImpl implements MedicineService {
    @Autowired
    private MedicineRepository medicineRepository;

    @Override
    public MedicineRespDto findMedicinesByUserId(String userId) {
        List<Medicine> medicines = medicineRepository.findMedicinesByUserId(userId);
        if (medicines == null) {
            MessageDto messageDto = MessageDto.builder()
                    .status("404")
                    .message("No Medicines found for this userId")
                    .build();
            throw new InvalidRequestException(HttpStatus.NOT_FOUND, messageDto);
        }
        List<MedicineDto> medicineDtos = mapToDtos(medicines);
        return MedicineRespDto.builder()
                .status("200")
                .message("Medicines fetched successfully")
                .medicines(medicineDtos)
                .build();
    }

    @Override
    @Transactional
    public void addMedicine(MedicineInsertReqDto request) {
        Medicine medicine = Medicine.builder()
                .userId(request.getUserId())
                .name(request.getName())
                .description(request.getDescription())
                .mrp(request.getMrp())
                .sellingPrice(request.getSellingPrice())
                .quantity(request.getQuantity())
                .build();
        try {
            medicineRepository.addMedicine(medicine);
        } catch (RuntimeException ex) {
            MessageDto messageDto = MessageDto.builder()
                    .status("500")
                    .message("Something went wrong, Internal Server Error")
                    .build();
            throw new InvalidRequestException(HttpStatus.INTERNAL_SERVER_ERROR, messageDto);
        }
    }

    @Override
    public MedicineDto findMedicineByMedicineId(String medicineId) {
        Medicine medicine = medicineRepository.findMedicineByMedicineId(medicineId);
        if (medicine == null) {
            MessageDto messageDto = MessageDto.builder()
                    .status("404")
                    .message("Medicine not found for this Id")
                    .build();
            throw new InvalidRequestException(HttpStatus.NOT_FOUND, messageDto);
        }
        return MedicineDto.builder()
                .medicineId(medicine.getMedicineId())
                .userId(medicine.getUserId())
                .name(medicine.getName())
                .description(medicine.getDescription())
                .mrp(medicine.getMrp())
                .sellingPrice(medicine.getSellingPrice())
                .quantity(medicine.getQuantity())
                .build();
    }

    @Override
    @Transactional
    public void updateMedicine(MedicineDto medicineDto) {
        Medicine result = medicineRepository.findMedicineByMedicineId(medicineDto.getMedicineId());
        if (result == null) {
            MessageDto messageDto = MessageDto.builder()
                    .status("404")
                    .message("Medicine not found for this Id")
                    .build();
            throw new InvalidRequestException(HttpStatus.NOT_FOUND, messageDto);
        }
        Medicine medicine = Medicine.builder()
                .medicineId(medicineDto.getMedicineId())
                .userId(medicineDto.getUserId())
                .name(medicineDto.getName())
                .description(medicineDto.getDescription())
                .mrp(medicineDto.getMrp())
                .sellingPrice(medicineDto.getSellingPrice())
                .quantity(medicineDto.getQuantity())
                .build();
        try {
            medicineRepository.updateMedicine(medicine);
        } catch (RuntimeException ex) {
            throw ex;
        }
    }

    private List<MedicineDto> mapToDtos(List<Medicine> medicines) {
        List<MedicineDto> medicineDtos = new ArrayList<>();
        medicines.forEach(medicine -> {
            MedicineDto medicineDto = MedicineDto.builder()
                    .medicineId(medicine.getMedicineId())
                    .userId(medicine.getUserId())
                    .name(medicine.getName())
                    .description(medicine.getDescription())
                    .mrp(medicine.getMrp())
                    .sellingPrice(medicine.getSellingPrice())
                    .quantity(medicine.getQuantity())
                    .build();
            medicineDtos.add(medicineDto);
        });
        return medicineDtos;
    }
}
