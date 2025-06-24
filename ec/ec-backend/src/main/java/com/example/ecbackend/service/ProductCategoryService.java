package com.example.ecbackend.service;

import com.example.ecbackend.entity.ProductCategory;

import java.util.List;

public interface ProductCategoryService {
    List<ProductCategory> getAll();
    List<ProductCategory> getByParentId(Integer parentId);
    void addCategory(ProductCategory category);
    void updateName(Integer id, String name);
    void deleteById(Integer id);
    int countProductsByCategory(Integer id);
}
