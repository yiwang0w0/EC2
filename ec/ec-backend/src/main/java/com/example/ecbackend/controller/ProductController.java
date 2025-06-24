package com.example.ecbackend.controller;

import com.example.ecbackend.common.PageResult;
import com.example.ecbackend.common.Response;
import com.example.ecbackend.entity.Product;
import com.example.ecbackend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public Response<PageResult<Product>> getAllProducts(@RequestParam(defaultValue = "1") int page,
                                                        @RequestParam(defaultValue = "10") int size) {
        return Response.success(productService.getProductsByPage(page, size));
    }

    @GetMapping("/{id}")
    public Response<Product> getProductById(@PathVariable Integer id) {
        return Response.success(productService.getById(id));
    }

    @PostMapping
    public Response<Void> addProduct(@RequestBody Product product) {
        productService.addProduct(product);
        return Response.success(null);
    }

    @PutMapping("/{id}")
    public Response<Void> updateProduct(@PathVariable Integer id, @RequestBody Product product) {
        product.setId(id);
        productService.updateProduct(product);
        return Response.success(null);
    }

    @DeleteMapping("/{id}")
    public Response<Void> deleteProduct(@PathVariable Integer id) {
        productService.deleteProduct(id);
        return Response.success(null);
    }
}
