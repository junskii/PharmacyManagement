package com.example.pharmacy.designpatterns.decorator;

import com.example.pharmacy.models.Sale;

public class PromoDiscountDecorator extends DiscountDecorator {
    private final double promoPercentage;
    private final String promoDescription;

    public PromoDiscountDecorator(Discountable discountable, double promoPercentage, String promoDescription) {
        super(discountable);
        this.promoPercentage = promoPercentage;
        this.promoDescription = promoDescription;
    }

    @Override
    public double calculateFinalAmount(Sale sale) {
        double amount = super.discountable.calculateFinalAmount(sale);
        amount *= (1 - promoPercentage);
        sale.setDiscountDetails(appendDiscountDetails(sale.getDiscountDetails(), promoDescription));
        return amount;
    }

    private String appendDiscountDetails(String existingDetails, String newDetail) {
        System.out.println(
                "PromoDiscountDecorator - Existing Details: " + existingDetails + ", New Detail: " + newDetail);
        if (existingDetails == null || existingDetails.isEmpty()) {
            return newDetail;
        } else {
            return existingDetails + ", " + newDetail;
        }
    }
}