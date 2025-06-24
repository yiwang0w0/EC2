package com.example.ecbackend.service.impl;

import com.example.ecbackend.common.PageResult;
import com.example.ecbackend.entity.Product;
import com.example.ecbackend.mapper.ProductMapper;
import com.example.ecbackend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public List<Product> getAll() {
        return productMapper.selectWithCategory();
    }

    @Override
    public Product getById(Integer id) {
        return productMapper.findById(id);
    }

    @Override
    public void addProduct(Product product) {
        productMapper.insert(product);
    }

    @Override
    public void updateProduct(Product product) {
        productMapper.update(product);
    }

    @Override
    public void deleteProduct(Integer id) {
        productMapper.deleteById(id);
    }

    @Override
    public PageResult<Product> getProductsByPage(int page, int size) {
        int offset = (page - 1) * size;
        List<Product> data = productMapper.selectByPage(offset, size);
        int total = productMapper.countAll();
        return new PageResult<>(data, total);
    }
}
