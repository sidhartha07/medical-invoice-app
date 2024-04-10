package com.sidh.medinvoice.service.medicine;

import com.sidh.medinvoice.dto.request.MedicineInsertReqDto;
import com.sidh.medinvoice.dto.response.MedicineDto;
import com.sidh.medinvoice.dto.response.MedicineRespDto;

public interface MedicineService {
    MedicineRespDto findMedicinesByUserId(String userId);
    void addMedicine(MedicineInsertReqDto request);
    MedicineDto findMedicineByMedicineId(String medicineId);
    void updateMedicine(MedicineDto medicineDto);
}
