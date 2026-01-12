package com.example.pharmacy.designpatterns.template;

import com.example.pharmacy.models.Sale;
import com.example.pharmacy.models.SaleItem;
import com.example.pharmacy.models.Medicine;
import com.example.pharmacy.models.User;
import com.example.pharmacy.service.MedicineService;
import com.example.pharmacy.mapper.SaleItemMapper;
import com.example.pharmacy.mapper.UserMapper;
import com.example.pharmacy.designpatterns.decorator.Discountable;
import com.example.pharmacy.designpatterns.decorator.BaseSaleDiscountable;
import com.example.pharmacy.designpatterns.decorator.PromoDiscountDecorator;
import com.example.pharmacy.designpatterns.decorator.PaymentMethodDiscountDecorator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component // Mark as a Spring component
public class DefaultSaleProcessor extends SaleProcessor {

    @Autowired
    private UserMapper userMapper;

    public DefaultSaleProcessor(MedicineService medicineService, SaleItemMapper saleItemMapper) {
        super(medicineService, saleItemMapper);
    }

    @Override
    protected void validateSale(Sale sale) {
        if (sale.getSaleItems() == null || sale.getSaleItems().isEmpty()) {
            throw new RuntimeException("Sale must contain at least one item.");
        }
        for (SaleItem item : sale.getSaleItems()) {
            Medicine medicine = medicineService.getById(item.getMedicineId());
            if (medicine == null) {
                throw new RuntimeException("Medicine with ID " + item.getMedicineId() + " not found.");
            }
            if (medicine.getStock() < item.getQuantity()) {
                throw new RuntimeException("Insufficient stock for medicine: " + medicine.getName() + ". Available: "
                        + medicine.getStock() + ", Requested: " + item.getQuantity());
            }
        }
    }

    @Override
    protected void processSaleItemsAndDeductStock(Sale sale) {
        sale.setSaleDate(LocalDateTime.now()); // Set sale date here as part of processing

        for (SaleItem item : sale.getSaleItems()) {
            item.setSaleId(sale.getId());
            saleItemMapper.insert(item);

            // Deduct stock
            Medicine medicine = medicineService.getById(item.getMedicineId());
            medicine.setStock(medicine.getStock() - item.getQuantity());
            medicineService.update(medicine);
        }
    }

    @Override
    protected void applyDiscounts(Sale sale) {
        // Start with the base amount calculation
        Discountable discountCalculator = new BaseSaleDiscountable();
        sale.setDiscountDetails(""); // NEW: Initialize discountDetails to empty string

        // Apply Payment Method Discount
        if (sale.getPaymentMethod() != null && !sale.getPaymentMethod().isEmpty()) {
            discountCalculator = new PaymentMethodDiscountDecorator(discountCalculator, sale);

        }

        // Apply Promo Discount if frontendPromoPercentage is provided and valid
        if (sale.getFrontendPromoPercentage() != null &&
                sale.getFrontendPromoPercentage() > 0 &&
                sale.getFrontendPromoPercentage() < 1) { // Asumsi frontend mengirim persentase sebagai desimal (e.g.,
                                                         // 0.10)
            double percentage = sale.getFrontendPromoPercentage();
            String promoDesc = "Promo " + (int) (percentage * 100) + "%"; // NEW: Generate concise promo description
            System.out.println("DefaultSaleProcessor - Promo Description generated: " + promoDesc);
            discountCalculator = new PromoDiscountDecorator(discountCalculator, percentage, promoDesc);
        }

        // Calculate the final amount after all decorators are applied
        double finalAmount = discountCalculator.calculateFinalAmount(sale);
        sale.setTotalAmount(finalAmount);
        System.out.println("Final Discount Details before saving: " + sale.getDiscountDetails());
    }
}