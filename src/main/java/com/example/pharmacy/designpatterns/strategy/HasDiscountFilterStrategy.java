package com.example.pharmacy.designpatterns.strategy;

import com.example.pharmacy.models.Sale;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class HasDiscountFilterStrategy implements SaleFilterStrategy {
    @Override
    public List<Sale> filter(List<Sale> sales, Map<String, Object> filterParams) {
        Boolean hasDiscount = (Boolean) filterParams.get("hasDiscount");

        if (hasDiscount == null) {
            return sales; // No hasDiscount specified, return all sales
        }

        return sales.stream()
                .filter(sale -> hasDiscount
                        .equals(sale.getDiscountDetails() != null && !sale.getDiscountDetails().isEmpty()))
                .collect(Collectors.toList());
    }
}