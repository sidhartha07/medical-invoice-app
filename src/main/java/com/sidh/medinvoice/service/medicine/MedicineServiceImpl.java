package com.sidh.medinvoice.service.medicine;

import com.sidh.medinvoice.dto.response.MedicineDto;
import com.sidh.medinvoice.dto.response.MedicineRespDto;
import com.sidh.medinvoice.dto.response.MessageDto;
import com.sidh.medinvoice.exception.InvalidRequestException;
import com.sidh.medinvoice.model.medicine.Medicine;
import com.sidh.medinvoice.repository.medicine.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
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
