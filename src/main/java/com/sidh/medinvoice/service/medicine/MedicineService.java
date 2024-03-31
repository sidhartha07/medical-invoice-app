package com.sidh.medinvoice.service.medicine;

import com.sidh.medinvoice.dto.response.MedicineRespDto;

public interface MedicineService {
    MedicineRespDto findMedicinesByUserId(String userId);
}
