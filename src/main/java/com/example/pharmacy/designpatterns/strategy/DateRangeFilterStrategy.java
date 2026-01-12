package com.example.pharmacy.designpatterns.strategy;

import com.example.pharmacy.models.Sale;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DateRangeFilterStrategy implements SaleFilterStrategy {
    @Override
    public List<Sale> filter(List<Sale> sales, Map<String, Object> filterParams) {
        LocalDate startDate = (LocalDate) filterParams.get("startDate");
        LocalDate endDate = (LocalDate) filterParams.get("endDate");

        if (startDate == null && endDate == null) {
            return sales; // No date range specified, return all sales
        }

        LocalDateTime startDateTime = (startDate != null) ? startDate.atStartOfDay() : null;
        LocalDateTime endDateTime = (endDate != null) ? endDate.atTime(LocalTime.MAX) : null;

        return sales.stream()
                .filter(sale -> {
                    LocalDateTime saleDate = sale.getSaleDate();
                    if (saleDate == null)
                        return false;

                    boolean afterStart = (startDateTime == null) || !saleDate.isBefore(startDateTime);
                    boolean beforeEnd = (endDateTime == null) || !saleDate.isAfter(endDateTime);

                    return afterStart && beforeEnd;
                })
                .collect(Collectors.toList());
    }
}