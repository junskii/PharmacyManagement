package com.example.pharmacy.designpatterns.template;

import com.example.pharmacy.models.Sale;
import com.example.pharmacy.service.MedicineService;
import com.example.pharmacy.mapper.SaleItemMapper;

public abstract class SaleProcessor {

    protected MedicineService medicineService;
    protected SaleItemMapper saleItemMapper;

    public SaleProcessor(MedicineService medicineService, SaleItemMapper saleItemMapper) {
        this.medicineService = medicineService;
        this.saleItemMapper = saleItemMapper;
    }

    // The template method that defines the algorithm structure
    public final void processSaleTemplate(Sale sale) {
        // Step 1: Pre-process (can be overridden, or is a hook)
        preProcessSale(sale);

        // Step 2: Validate the sale (abstract, must be implemented by subclasses)
        validateSale(sale);

        // Step 3: Apply discounts (hook method, can be overridden)
        applyDiscounts(sale);

        // Step 4: Process sale items and deduct stock (concrete, fixed)
        processSaleItemsAndDeductStock(sale);

        // Step 5: Post-process (can be overridden, or is a hook)
        postProcessSale(sale);
    }

    // Abstract methods to be implemented by concrete subclasses
    protected abstract void validateSale(Sale sale);

    protected abstract void processSaleItemsAndDeductStock(Sale sale);

    // Hook methods (can be overridden, but have default empty implementation)
    protected void preProcessSale(Sale sale) {
        // Default empty implementation
    }

    protected void applyDiscounts(Sale sale) {
        // Default empty implementation
    }

    protected void postProcessSale(Sale sale) {
        // Default empty implementation
    }
}