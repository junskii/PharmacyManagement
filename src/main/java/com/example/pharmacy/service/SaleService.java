package com.example.pharmacy.service;

import com.example.pharmacy.mapper.SaleMapper;
import com.example.pharmacy.mapper.SaleItemMapper;
import com.example.pharmacy.mapper.MedicineMapper;
import com.example.pharmacy.models.Sale;
import com.example.pharmacy.models.SaleItem;
import com.example.pharmacy.models.Medicine;
import com.example.pharmacy.designpatterns.template.SaleProcessor;
import com.example.pharmacy.designpatterns.template.DefaultSaleProcessor;
import com.example.pharmacy.designpatterns.strategy.SaleFilterStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class SaleService {

    @Autowired
    private SaleMapper saleMapper;
    @Autowired
    private SaleItemMapper saleItemMapper;
    @Autowired
    private MedicineService medicineService;
    @Autowired
    private List<SaleFilterStrategy> filterStrategies;

    private final SaleProcessor saleProcessor;

    public SaleService(SaleProcessor saleProcessor) {
        this.saleProcessor = saleProcessor;
    }

    @Autowired
    public void setSaleMapper(SaleMapper saleMapper) {
        this.saleMapper = saleMapper;
    }

    @Autowired
    public void setSaleItemMapper(SaleItemMapper saleItemMapper) {
        this.saleItemMapper = saleItemMapper;
    }

    @Autowired
    public void setMedicineService(MedicineService medicineService) {
        this.medicineService = medicineService;
    }

    @Transactional
    public void processSale(Sale sale) {
        saleMapper.insert(sale);

        saleProcessor.processSaleTemplate(sale); // Delegates to processor

        saleMapper.update(sale);
    }

    public List<Sale> getAllSales() {
        return saleMapper.findAll();
    }

    public Sale getSaleById(Integer id) {
        return saleMapper.findById(id);
    }

    public List<Sale> getSalesByFilters(Map<String, Object> filters) {
        return saleMapper.findSalesWithFilters(filters);
    }

    public int countSalesByFilters(Map<String, Object> filters) {
        return saleMapper.countSalesWithFilters(filters);
    }

    public List<Sale> getFilteredSales(Map<String, Object> filterParams) {
        List<Sale> sales = saleMapper.findAll();
        for (SaleFilterStrategy strategy : filterStrategies) {
            sales = strategy.filter(sales, filterParams);
        }
        return sales;
    }
}