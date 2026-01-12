package com.example.pharmacy.mapper;

import com.example.pharmacy.models.Role;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface RoleMapper {
    @Select("SELECT * FROM roles")
    List<Role> findAll();

    @Select("SELECT * FROM roles WHERE id = #{id}")
    Role findById(Integer id);

    @Select("SELECT * FROM roles WHERE name = #{name}")
    Role findByName(String name);
}