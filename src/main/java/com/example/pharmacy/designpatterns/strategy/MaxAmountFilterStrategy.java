package com.example.pharmacy.designpatterns.strategy;

import com.example.pharmacy.models.Sale;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class MaxAmountFilterStrategy implements SaleFilterStrategy {
    @Override
    public List<Sale> filter(List<Sale> sales, Map<String, Object> filterParams) {
        Double maxAmount = (Double) filterParams.get("maxAmount");

        if (maxAmount == null) {
            return sales; // No max amount specified, return all sales
        }

        return sales.stream()
                .filter(sale -> sale.getTotalAmount() <= maxAmount)
                .collect(Collectors.toList());
    }
}