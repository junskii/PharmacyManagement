package com.example.pharmacy.designpatterns.strategy;

import com.example.pharmacy.models.Sale;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class MinAmountFilterStrategy implements SaleFilterStrategy {
    @Override
    public List<Sale> filter(List<Sale> sales, Map<String, Object> filterParams) {
        Double minAmount = (Double) filterParams.get("minAmount");

        if (minAmount == null) {
            return sales; // No min amount specified, return all sales
        }

        return sales.stream()
                .filter(sale -> sale.getTotalAmount() >= minAmount)
                .collect(Collectors.toList());
    }
}