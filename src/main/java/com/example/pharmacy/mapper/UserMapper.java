package com.example.pharmacy.mapper;

import com.example.pharmacy.models.User;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface UserMapper {
        @Select("SELECT u.*, r.id as role_id, r.name as role_name FROM users u LEFT JOIN roles r ON u.role_id = r.id")
        @Results({
                        @Result(property = "id", column = "id"),
                        @Result(property = "username", column = "username"),
                        @Result(property = "password", column = "password"),
                        @Result(property = "role.id", column = "role_id"),
                        @Result(property = "role.name", column = "role_name")
        })
        List<User> findAll();

        @Select("SELECT u.*, r.id as role_id, r.name as role_name FROM users u LEFT JOIN roles r ON u.role_id = r.id WHERE u.id = #{id}")
        @Results({
                        @Result(property = "id", column = "id"),
                        @Result(property = "username", column = "username"),
                        @Result(property = "password", column = "password"),
                        @Result(property = "role.id", column = "role_id"),
                        @Result(property = "role.name", column = "role_name")
        })
        User findById(Integer id);

        @Select("SELECT u.*, r.id as role_id, r.name as role_name FROM users u LEFT JOIN roles r ON u.role_id = r.id WHERE u.username = #{username}")
        @Results({
                        @Result(property = "id", column = "id"),
                        @Result(property = "username", column = "username"),
                        @Result(property = "password", column = "password"),
                        @Result(property = "role.id", column = "role_id"),
                        @Result(property = "role.name", column = "role_name")
        })
        User findByUsername(String username);

        @Select({
                        "<script>",
                        "SELECT u.*, r.id as role_id, r.name as role_name ",
                        "FROM users u LEFT JOIN roles r ON u.role_id = r.id ",
                        "WHERE r.name IN ",
                        "<foreach item=\"roleName\" collection=\"roleNames\" open=\"(\" separator=\",\" close=\")\">",
                        "#{roleName}",
                        "</foreach>",
                        "</script>"
        })
        @Results({
                        @Result(property = "id", column = "id"),
                        @Result(property = "username", column = "username"),
                        @Result(property = "password", column = "password"),
                        @Result(property = "role.id", column = "role_id"),
                        @Result(property = "role.name", column = "role_name")
        })
        List<User> findByRoleNames(@Param("roleNames") List<String> roleNames);

        @Select("SELECT u.*, r.id as role_id, r.name as role_name FROM users u LEFT JOIN roles r ON u.role_id = r.id WHERE r.name = #{roleName}")
        @Results({
                        @Result(property = "id", column = "id"),
                        @Result(property = "username", column = "username"),
                        @Result(property = "password", column = "password"),
                        @Result(property = "role.id", column = "role_id"),
                        @Result(property = "role.name", column = "role_name")
        })
        List<User> findByRoleName(String roleName);

        @Insert("INSERT INTO users (username, password, role_id) VALUES (#{username}, #{password}, #{role.id})")
        @Options(useGeneratedKeys = true, keyProperty = "id")
        int insert(User user);
}
