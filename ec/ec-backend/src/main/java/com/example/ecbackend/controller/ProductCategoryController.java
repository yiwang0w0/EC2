package com.example.ecbackend.controller;

import com.example.ecbackend.common.Response;
import com.example.ecbackend.entity.ProductCategory;
import com.example.ecbackend.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
public class ProductCategoryController {

    @Autowired
    private ProductCategoryService categoryService;

    @GetMapping
    public Response<List<ProductCategory>> getAll() {
        return Response.success(categoryService.getAll());
    }
    @GetMapping("/can-delete/{id}")
    public Response<String> canDelete(@PathVariable Integer id) {
        int count = categoryService.countProductsByCategory(id); // 查询该分类下商品数量
        if (count > 0) {
            return Response.fail(1, "该分类下仍有商品，无法删除");
        }
        return Response.success(null);
    }
    
    @PostMapping
    public Response<String> addCategory(@RequestBody ProductCategory category) {
        try {
            System.out.println("🟢 添加分类请求:");
            System.out.println("分类名: " + category.getName());
            System.out.println("父类ID: " + category.getParentId());

            categoryService.addCategory(category);
            return Response.success(null);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.fail(1, "添加失败");
        }
    }
    @GetMapping("/parent/{parentId}")
    public Response<List<ProductCategory>> getByParentId(@PathVariable Integer parentId) {
        return Response.success(categoryService.getByParentId(parentId));
    }
    @PutMapping("/{id}")
    public Response<String> updateCategoryName(@PathVariable Integer id, @RequestBody Map<String, String> body) {
        try {
            String name = body.get("name");
            categoryService.updateName(id, name);
            return Response.success(null);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.fail(1, "修改失败");
        }
    }

    @DeleteMapping("/{id}")
    public Response<String> deleteCategory(@PathVariable Integer id) {
        try {
            categoryService.deleteById(id);
            return Response.success(null);
        } catch (DataIntegrityViolationException e) {
            return Response.fail(1, "删除失败：该分类下仍有商品，请先删除商品");
        } catch (Exception e) {
            e.printStackTrace();
            return Response.fail(2, "删除失败：服务器错误");
        }
    }
}
