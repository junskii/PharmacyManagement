package com.example.pharmacy.mapper;

import com.example.pharmacy.models.Sale;
import com.example.pharmacy.models.SaleItem;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface SaleMapper {

        @Insert("INSERT INTO sales (user_id, sale_date, total_amount, discount_details, payment_method) VALUES (#{userId}, #{saleDate}, #{totalAmount}, #{discountDetails}, #{paymentMethod})")
        @Options(useGeneratedKeys = true, keyProperty = "id")
        void insert(Sale sale);

        List<Sale> findSalesWithFilters(Map<String, Object> params);

        int countSalesWithFilters(Map<String, Object> params);

        @Select("SELECT s.id, s.user_id, s.sale_date, s.total_amount, s.discount_details, s.payment_method, u.username as cashierName "
                        +
                        "FROM sales s LEFT JOIN users u ON s.user_id = u.id WHERE s.id = #{id}")
        @Results({
                        @Result(property = "id", column = "id"),
                        @Result(property = "userId", column = "user_id"),
                        @Result(property = "saleDate", column = "sale_date"),
                        @Result(property = "totalAmount", column = "total_amount"),
                        @Result(property = "discountDetails", column = "discount_details"),
                        @Result(property = "paymentMethod", column = "payment_method"),
                        @Result(property = "cashierName", column = "cashierName"),
                        @Result(property = "saleItems", column = "id", javaType = List.class, many = @Many(select = "com.example.pharmacy.mapper.SaleItemMapper.findBySaleId"))
        })
        Sale findById(Integer id);

        List<Sale> findAll();

        @Update("UPDATE sales SET total_amount = #{totalAmount}, discount_details = #{discountDetails}, payment_method = #{paymentMethod} WHERE id = #{id}")
        void update(Sale sale);
}