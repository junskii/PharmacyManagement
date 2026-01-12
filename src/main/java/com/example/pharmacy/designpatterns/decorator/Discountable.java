package com.example.pharmacy.designpatterns.decorator;

import com.example.pharmacy.models.Sale;

public interface Discountable {
    double calculateFinalAmount(Sale sale);
}