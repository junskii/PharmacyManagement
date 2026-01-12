package com.example.pharmacy.designpatterns.command;

import com.example.pharmacy.models.Sale;
import com.example.pharmacy.service.SaleService;

public class ProcessSaleCommand implements Command {
    private final SaleService saleService;
    private final Sale sale;

    public ProcessSaleCommand(SaleService saleService, Sale sale) {
        this.saleService = saleService;
        this.sale = sale;
    }

    @Override
    public void execute() {
        saleService.processSale(sale);
    }
}