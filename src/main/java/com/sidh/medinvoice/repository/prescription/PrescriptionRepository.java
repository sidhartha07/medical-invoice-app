package com.sidh.medinvoice.repository.prescription;

import com.sidh.medinvoice.model.prescription.Prescription;
import com.sidh.medinvoice.model.user.UserRep;

import java.util.List;

public interface PrescriptionRepository {
    String insert(String userId, String imageUrl, Double latitude, Double longitude);
    List<Prescription> getPrescriptionByUserId(String userId);
    List<Prescription> getPrescriptionByMedicalRepId(String medicalRepId);
    int saveRepsForUser(List<UserRep> reps);

}
