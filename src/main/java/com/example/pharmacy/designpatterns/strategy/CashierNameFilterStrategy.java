package com.example.pharmacy.designpatterns.strategy;

import com.example.pharmacy.models.Sale;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class CashierNameFilterStrategy implements SaleFilterStrategy {
    @Override
    public List<Sale> filter(List<Sale> sales, Map<String, Object> filterParams) {
        String cashierName = (String) filterParams.get("cashierName");

        if (cashierName == null || cashierName.trim().isEmpty()) {
            return sales; // No cashier name specified, return all sales
        }

        return sales.stream()
                .filter(sale -> sale.getCashierName() != null &&
                        sale.getCashierName().toLowerCase().contains(cashierName.toLowerCase()))
                .collect(Collectors.toList());
    }
}