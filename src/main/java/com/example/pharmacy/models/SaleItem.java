package com.example.pharmacy.models;

import lombok.Data;

@Data
public class SaleItem {
    private Integer id;
    private Integer saleId;
    private Integer medicineId;
    private String medicineName;
    private Integer quantity;
    private Double unitPrice;
    private Double subtotal;
}