package com.example.pharmacy.models;

import lombok.Data;
import java.time.LocalDate;

@Data
public class StockNotification {
    private Integer id;
    private Integer medicineId;
    private String message;
    private String type; // e.g., "LOW_STOCK", "EXPIRATION"
    private LocalDate notificationDate;
    private Boolean isRead;
}