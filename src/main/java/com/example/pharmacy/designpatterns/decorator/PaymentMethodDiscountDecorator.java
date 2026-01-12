package com.example.pharmacy.designpatterns.decorator;

import com.example.pharmacy.models.Sale;

public class PaymentMethodDiscountDecorator extends DiscountDecorator {
    private final Sale sale;

    public PaymentMethodDiscountDecorator(Discountable discountable, Sale sale) {
        super(discountable);
        this.sale = sale;
    }

    @Override
    public double calculateFinalAmount(Sale sale) {
        double amount = super.discountable.calculateFinalAmount(sale);

        if (sale.getPaymentMethod() != null) {
            String paymentMethod = sale.getPaymentMethod().toLowerCase();

            switch (paymentMethod) {
                case "alipay":
                    amount *= 0.97; // 3% discount for Alipay
                    sale.setDiscountDetails(appendDiscountDetails(sale.getDiscountDetails(), "Alipay 3%"));
                    break;
                case "credit card":
                    amount *= 0.90; // 10% discount for Credit Card
                    sale.setDiscountDetails(appendDiscountDetails(sale.getDiscountDetails(), "Credit Card 10%"));
                    break;
                // Apple Pay, Cash, Debit (sebelumnya Card) tidak memiliki diskon spesifik di
                // sini
                default:
                    // No specific discount for other payment methods (Cash, Apple Pay, Debit, etc.)
                    break;
            }
        }
        return amount;
    }

    private String appendDiscountDetails(String existingDetails, String newDetail) {
        System.out.println(
                "PaymentMethodDiscountDecorator - Existing Details: " + existingDetails + ", New Detail: " + newDetail);
        if (existingDetails == null || existingDetails.isEmpty()) {
            return newDetail;
        } else {
            return existingDetails + ", " + newDetail;
        }
    }
}