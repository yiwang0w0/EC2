package com.example.ecbackend.entity;

import lombok.Data;

@Data
public class ProductCategory {
    private Integer id;
    private String name;
    private Integer parentId;
}
