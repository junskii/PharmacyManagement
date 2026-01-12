package com.example.pharmacy.designpatterns.strategy;

import com.example.pharmacy.models.Sale;
import java.util.List;
import java.util.Map;

public interface SaleFilterStrategy {
    List<Sale> filter(List<Sale> sales, Map<String, Object> filterParams);
}