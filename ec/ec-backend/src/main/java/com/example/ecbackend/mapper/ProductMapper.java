package com.example.ecbackend.mapper;

import com.example.ecbackend.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProductMapper {
    List<Product> findAll();
    Product findById(Integer id);
    void insert(Product product);
    void update(Product product);
    void deleteById(Integer id);
    int countByCategoryId(Integer categoryId);
    List<Product> selectWithCategory();
    List<Product> selectByPage(@Param("offset") int offset, @Param("size") int size);
    int countAll();

}
