package com.example.pharmacy.service;

import com.example.pharmacy.mapper.SaleMapper;
import com.example.pharmacy.mapper.MedicineMapper;
import com.example.pharmacy.mapper.UserMapper;
import com.example.pharmacy.mapper.StockNotificationMapper;
import com.example.pharmacy.models.Medicine;
import com.example.pharmacy.models.Sale;
import com.example.pharmacy.models.StockNotification;
import com.example.pharmacy.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {

    @Autowired
    private SaleMapper saleMapper;
    @Autowired
    private MedicineMapper medicineMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StockNotificationMapper stockNotificationMapper;

    public Map<String, Object> getDashboardSummary() {
        Map<String, Object> summary = new HashMap<>();

        // Sales data
        List<Sale> allSales = saleMapper.findAll();
        double totalSalesAmountToday = allSales.stream()
                .filter(sale -> sale.getSaleDate() != null && sale.getSaleDate().toLocalDate().isEqual(LocalDate.now()))
                .mapToDouble(Sale::getTotalAmount)
                .sum();
        summary.put("totalSalesAmountToday", totalSalesAmountToday);
        summary.put("totalSalesCount", allSales.size());

        // Medicine data
        List<Medicine> allMedicines = medicineMapper.findAll();
        summary.put("totalMedicines", allMedicines.size());

        // User data
        List<User> allUsers = userMapper.findAll();
        summary.put("totalUsers", allUsers.size());

        // Stock notifications
        List<StockNotification> recentNotifications = stockNotificationMapper.findAll();
        summary.put("recentNotifications", recentNotifications);

        return summary;
    }
}