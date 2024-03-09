package com.sidh.medinvoice.repository.prescription;

public interface PrescriptionRepository {
    void insert(String userId, String imageUrl, Double latitude, Double longitude);
}
