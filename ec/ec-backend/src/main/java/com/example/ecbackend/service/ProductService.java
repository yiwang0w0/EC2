package com.example.ecbackend.service;

import com.example.ecbackend.common.PageResult;
import com.example.ecbackend.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> getAll();
    Product getById(Integer id);
    void addProduct(Product product);
    void updateProduct(Product product);
    void deleteProduct(Integer id);
    PageResult<Product> getProductsByPage(int page, int size);
}
