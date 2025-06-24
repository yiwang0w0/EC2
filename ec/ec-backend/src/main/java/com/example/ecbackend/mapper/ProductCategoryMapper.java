package com.example.ecbackend.mapper;

import com.example.ecbackend.entity.ProductCategory;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProductCategoryMapper {
    List<ProductCategory> findAll();
    List<ProductCategory> findByParentId(Integer parentId);
    void insert(ProductCategory category);
    void deleteById(Integer id);
    void updateNameById(@Param("id") Integer id, @Param("name") String name);

}
