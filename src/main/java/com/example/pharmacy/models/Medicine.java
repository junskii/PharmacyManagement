package com.example.pharmacy.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Medicine {
    private Integer id;
    private String name;
    private Integer stock;
    private BigDecimal price;
    private String description;
    private String image;
    private LocalDate expiryDate;
    private Timestamp createdAt;
    private String category;
}