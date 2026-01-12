package com.example.pharmacy.mapper;

import com.example.pharmacy.models.Medicine;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface MedicineMapper {
    @Select("SELECT * FROM medicines")
    List<Medicine> findAll();

    @Select("SELECT * FROM medicines WHERE id = #{id}")
    Medicine findById(Integer id);

    @Insert("INSERT INTO medicines (name, stock, price, description, image, expiry_date) VALUES (#{name}, #{stock}, #{price}, #{description}, #{image}, #{expiryDate})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Medicine medicine);

    @Update("UPDATE medicines SET name=#{name}, stock=#{stock}, price=#{price}, description=#{description}, image=#{image}, expiry_date=#{expiryDate} WHERE id=#{id}")
    int update(Medicine medicine);

    @Delete("DELETE FROM medicines WHERE id = #{id}")
    int delete(Integer id);
}