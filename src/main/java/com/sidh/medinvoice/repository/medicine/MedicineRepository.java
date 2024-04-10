package com.sidh.medinvoice.repository.medicine;

import com.sidh.medinvoice.model.medicine.Medicine;

import java.util.List;

public interface MedicineRepository {
    List<Medicine> findMedicinesByUserId(String userId);
    void addMedicine(Medicine medicine);
    Medicine findMedicineByMedicineId(String medicineId);
    void updateMedicine(Medicine medicine);
}
