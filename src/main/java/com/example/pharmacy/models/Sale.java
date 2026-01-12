package com.example.pharmacy.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sale {
    private Integer id;
    private Integer userId;
    private LocalDateTime saleDate;
    private Double totalAmount;
    private String discountDetails;
    private Double frontendPromoPercentage;
    private String paymentMethod;
    private String cashierName;
    private List<SaleItem> saleItems;

    public String getCashierName() {
        return cashierName;
    }

    public void setCashierName(String cashierName) {
        this.cashierName = cashierName;
    }
}