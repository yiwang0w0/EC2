package com.example.ecbackend.entity;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class Product {
    private Integer id;
    private String name;
    private BigDecimal price;
    private String imageUrl;
    private Integer stock;
    private Integer categoryId;  // 分类ID
}