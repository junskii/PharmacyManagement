package com.example.pharmacy.designpatterns.decorator;

import com.example.pharmacy.models.Sale;
import com.example.pharmacy.models.SaleItem;
import java.util.List;

public class BaseSaleDiscountable implements Discountable {

    @Override
    public double calculateFinalAmount(Sale sale) {
        double total = 0;
        if (sale.getSaleItems() != null) {
            for (SaleItem item : sale.getSaleItems()) {
                total += item.getSubtotal();
            }
        }
        return total;
    }
}