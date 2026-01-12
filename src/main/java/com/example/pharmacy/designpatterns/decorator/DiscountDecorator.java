package com.example.pharmacy.designpatterns.decorator;

import com.example.pharmacy.models.Sale;

public abstract class DiscountDecorator implements Discountable {
    protected Discountable discountable;

    public DiscountDecorator(Discountable discountable) {
        this.discountable = discountable;
    }

    @Override
    public abstract double calculateFinalAmount(Sale sale);
}