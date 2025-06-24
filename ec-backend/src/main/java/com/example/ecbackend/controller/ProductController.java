package com.example.ecbackend.controller;

import com.example.ecbackend.entity.Product;
import com.example.ecbackend.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductMapper productMapper;

    @GetMapping
    public List<Product> getAllProducts() {
        return productMapper.findAll();
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Integer id) {
        return productMapper.findById(id);
    }

    @PostMapping
    public void addProduct(@RequestBody Product product) {
        productMapper.insert(product);
    }

    @PutMapping("/{id}")
    public void updateProduct(@PathVariable Integer id, @RequestBody Product product) {
        product.setId(id);
        productMapper.update(product);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Integer id) {
        productMapper.deleteById(id);
    }
}
