package com.example.pharmacy.mapper;

import com.example.pharmacy.models.SaleItem;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SaleItemMapper {

    @Insert("INSERT INTO sale_items (sale_id, medicine_id, quantity, unit_price, subtotal) VALUES (#{saleId}, #{medicineId}, #{quantity}, #{unitPrice}, #{subtotal})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(SaleItem saleItem);

    @Select("SELECT si.id, si.sale_id, si.medicine_id, si.quantity, si.unit_price, si.subtotal, m.name as medicineName "
            +
            "FROM sale_items si JOIN medicines m ON si.medicine_id = m.id WHERE si.sale_id = #{saleId}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "saleId", column = "sale_id"),
            @Result(property = "medicineId", column = "medicine_id"),
            @Result(property = "medicineName", column = "medicineName"),
            @Result(property = "quantity", column = "quantity"),
            @Result(property = "unitPrice", column = "unit_price"),
            @Result(property = "subtotal", column = "subtotal")
    })
    List<SaleItem> findBySaleId(Integer saleId);
}