package com.example.pharmacy.service;

import com.example.pharmacy.designpatterns.observer.Observer;
import com.example.pharmacy.mapper.MedicineMapper;
import com.example.pharmacy.mapper.StockNotificationMapper;
import com.example.pharmacy.models.Medicine;
import com.example.pharmacy.models.StockNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class StockNotificationService implements Observer<Medicine> {

    @Autowired
    private MedicineMapper medicineMapper;
    @Autowired
    private StockNotificationMapper stockNotificationMapper;

    private static final int LOW_STOCK_THRESHOLD = 10; // Example threshold
    private static final int EXPIRATION_THRESHOLD_MONTHS = 2; // Medicines expiring within 2 months

    @Override
    public void update(Medicine medicine) {
        // Handle LOW_STOCK notifications
        String lowStockMessage = String.format("Medicine '%s' is low on stock (current: %d).", medicine.getName(),
                medicine.getStock());
        boolean isCurrentlyLowStock = medicine.getStock() <= LOW_STOCK_THRESHOLD;

        StockNotification latestUnreadLowStock = stockNotificationMapper
                .findUnreadByMedicineIdAndType(medicine.getId(), "LOW_STOCK");
        StockNotification latestReadLowStock = stockNotificationMapper.findReadByMedicineIdAndType(medicine.getId(),
                "LOW_STOCK");

        if (isCurrentlyLowStock) {
            if (latestUnreadLowStock == null) { // No unread notification
                if (latestReadLowStock != null) { // An old read notification exists
                    // Reactivate it
                    latestReadLowStock.setMessage(lowStockMessage);
                    latestReadLowStock.setNotificationDate(LocalDate.now());
                    latestReadLowStock.setIsRead(false);
                    stockNotificationMapper.update(latestReadLowStock);
                } else { // No existing notification at all
                    createNotification(medicine.getId(), lowStockMessage, "LOW_STOCK");
                }
            } else if (!latestUnreadLowStock.getMessage().equals(lowStockMessage)) {
                // Unread exists but message needs update
                latestUnreadLowStock.setMessage(lowStockMessage);
                latestUnreadLowStock.setNotificationDate(LocalDate.now());
                stockNotificationMapper.update(latestUnreadLowStock);
            }
            // If latestUnreadLowStock exists and message is same, do nothing.
        } else { // Stock is sufficient
            // If there's any low stock notification (read or unread), delete it
            if (latestUnreadLowStock != null || latestReadLowStock != null) {
                stockNotificationMapper.deleteByMedicineIdAndType(medicine.getId(), "LOW_STOCK");
            }
        }

        // Handle EXPIRATION notifications
        String expirationMessage = String.format("Medicine '%s' is nearing expiration (expiry: %s).",
                medicine.getName(), medicine.getExpiryDate().toString());
        boolean isCurrentlyNearingExpiration = medicine.getExpiryDate() != null
                && medicine.getExpiryDate().minusMonths(EXPIRATION_THRESHOLD_MONTHS).isBefore(LocalDate.now());

        StockNotification latestUnreadExpiration = stockNotificationMapper
                .findUnreadByMedicineIdAndType(medicine.getId(), "EXPIRATION");
        StockNotification latestReadExpiration = stockNotificationMapper
                .findReadByMedicineIdAndType(medicine.getId(), "EXPIRATION");

        if (isCurrentlyNearingExpiration) {
            if (latestUnreadExpiration == null) {
                if (latestReadExpiration != null) {
                    latestReadExpiration.setMessage(expirationMessage);
                    latestReadExpiration.setNotificationDate(LocalDate.now());
                    latestReadExpiration.setIsRead(false);
                    stockNotificationMapper.update(latestReadExpiration);
                } else {
                    createNotification(medicine.getId(), expirationMessage, "EXPIRATION");
                }
            } else if (!latestUnreadExpiration.getMessage().equals(expirationMessage)) {
                latestUnreadExpiration.setMessage(expirationMessage);
                latestUnreadExpiration.setNotificationDate(LocalDate.now());
                stockNotificationMapper.update(latestUnreadExpiration);
            }
        } else { // Not nearing expiration
            if (latestUnreadExpiration != null || latestReadExpiration != null) {
                stockNotificationMapper.deleteByMedicineIdAndType(medicine.getId(), "EXPIRATION");
            }
        }
    }

    private void createNotification(Integer medicineId, String message, String type) {
        StockNotification notification = new StockNotification();
        notification.setMedicineId(medicineId);
        notification.setMessage(message);
        notification.setType(type);
        notification.setNotificationDate(LocalDate.now());
        notification.setIsRead(false);
        stockNotificationMapper.insert(notification);
    }

    public List<StockNotification> getUnreadNotifications() {
        return stockNotificationMapper.findAll().stream()
                .filter(notification -> !notification.getIsRead())
                .toList();
    }

    public void markNotificationAsRead(Integer notificationId) {
        StockNotification notification = stockNotificationMapper.findById(notificationId);
        if (notification != null) {
            notification.setIsRead(true);
            stockNotificationMapper.updateIsRead(notification);
        }
    }
}