package com.example.ecbackend.service.impl;

import com.example.ecbackend.entity.ProductCategory;
import com.example.ecbackend.mapper.ProductCategoryMapper;
import com.example.ecbackend.mapper.ProductMapper;
import com.example.ecbackend.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

    @Autowired
    private ProductCategoryMapper categoryMapper;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public List<ProductCategory> getAll() {
        return categoryMapper.findAll();
    }

    @Override
    public List<ProductCategory> getByParentId(Integer parentId) {
        return categoryMapper.findByParentId(parentId);
    }

    @Override
    public void addCategory(ProductCategory category) {
        categoryMapper.insert(category);
    }

    @Override
    public void updateName(Integer id, String name) {
        categoryMapper.updateNameById(id, name);
    }

    @Override
    public void deleteById(Integer id) {
        categoryMapper.deleteById(id);
    }

    @Override
    public int countProductsByCategory(Integer id) {
        return productMapper.countByCategoryId(id);
    }
}
