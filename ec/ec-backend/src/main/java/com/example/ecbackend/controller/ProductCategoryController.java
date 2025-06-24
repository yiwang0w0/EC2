package com.example.ecbackend.controller;

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
    public List<ProductCategory> getAll() {
        return categoryService.getAll();
    }
    @GetMapping("/can-delete/{id}")
    public Map<String, Object> canDelete(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        int count = categoryService.countProductsByCategory(id); // 查询该分类下商品数量
        if (count > 0) {
            result.put("code", 1);
            result.put("message", "该分类下仍有商品，无法删除");
        } else {
            result.put("code", 0);
        }
        return result;
    }
    
    @PostMapping
    public Map<String, Object> addCategory(@RequestBody ProductCategory category) {
        Map<String, Object> result = new HashMap<>();
        try {
            System.out.println("🟢 添加分类请求:");
            System.out.println("分类名: " + category.getName());
            System.out.println("父类ID: " + category.getParentId());

            categoryService.addCategory(category);

            result.put("code", 0);
            result.put("message", "添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 1);
            result.put("message", "添加失败");
        }
        return result;
    }
    @GetMapping("/parent/{parentId}")
    public List<ProductCategory> getByParentId(@PathVariable Integer parentId) {
        return categoryService.getByParentId(parentId);
    }
    @PutMapping("/{id}")
    public Map<String, Object> updateCategoryName(@PathVariable Integer id, @RequestBody Map<String, String> body) {
        Map<String, Object> result = new HashMap<>();
        try {
            String name = body.get("name");
            categoryService.updateName(id, name);
            result.put("code", 0);
            result.put("message", "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 1);
            result.put("message", "修改失败");
        }
        return result;
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> deleteCategory(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        try {
            categoryService.deleteById(id);
            result.put("code", 0);
            result.put("message", "删除成功");
        } catch (DataIntegrityViolationException e) {
            result.put("code", 1);
            result.put("message", "删除失败：该分类下仍有商品，请先删除商品");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 2);
            result.put("message", "删除失败：服务器错误");
        }
        return result;
    }
}
