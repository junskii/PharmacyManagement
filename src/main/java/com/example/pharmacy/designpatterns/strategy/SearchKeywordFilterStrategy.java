package com.example.pharmacy.designpatterns.strategy;

import com.example.pharmacy.models.Sale;
import com.example.pharmacy.models.SaleItem;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class SearchKeywordFilterStrategy implements SaleFilterStrategy {
    @Override
    public List<Sale> filter(List<Sale> sales, Map<String, Object> filterParams) {
        String searchKeyword = (String) filterParams.get("searchKeyword");

        if (searchKeyword == null || searchKeyword.trim().isEmpty()) {
            return sales; // No search keyword specified, return all sales
        }

        String lowerCaseKeyword = searchKeyword.toLowerCase();

        return sales.stream()
                .filter(sale -> {
                    // Check cashier name
                    if (sale.getCashierName() != null
                            && sale.getCashierName().toLowerCase().contains(lowerCaseKeyword)) {
                        return true;
                    }
                    // Check discount details
                    if (sale.getDiscountDetails() != null
                            && sale.getDiscountDetails().toLowerCase().contains(lowerCaseKeyword)) {
                        return true;
                    }
                    // Check medicine names in sale items
                    if (sale.getSaleItems() != null) {
                        for (SaleItem item : sale.getSaleItems()) {
                            if (item.getMedicineName() != null
                                    && item.getMedicineName().toLowerCase().contains(lowerCaseKeyword)) {
                                return true;
                            }
                        }
                    }
                    return false;
                })
                .collect(Collectors.toList());
    }
}