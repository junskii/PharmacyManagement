package com.example.pharmacy.mapper;

import com.example.pharmacy.models.StockNotification;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface StockNotificationMapper {

    @Insert("INSERT INTO stock_notifications (medicine_id, message, type, notification_date, is_read) VALUES (#{medicineId}, #{message}, #{type}, #{notificationDate}, #{isRead})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(StockNotification notification);

    @Select("SELECT id, medicine_id, message, type, notification_date, is_read FROM stock_notifications WHERE id = #{id}")
    StockNotification findById(Integer id);

    @Select("SELECT id, medicine_id, message, type, notification_date, is_read FROM stock_notifications ORDER BY notification_date DESC")
    List<StockNotification> findAll();

    @Update("UPDATE stock_notifications SET is_read = #{isRead} WHERE id = #{id}")
    void updateIsRead(StockNotification notification);

    @Delete("DELETE FROM stock_notifications WHERE id = #{id}")
    void delete(Integer id);

    @Update("UPDATE stock_notifications SET medicine_id = #{medicineId}, message = #{message}, type = #{type}, notification_date = #{notificationDate}, is_read = #{isRead} WHERE id = #{id}")
    void update(StockNotification notification);

    @Delete("DELETE FROM stock_notifications WHERE medicine_id = #{medicineId} AND type = #{type}")
    void deleteByMedicineIdAndType(@Param("medicineId") Integer medicineId, @Param("type") String type);

    @Select("SELECT id, medicine_id, message, type, notification_date, is_read FROM stock_notifications WHERE medicine_id = #{medicineId} AND type = #{type} AND is_read = FALSE ORDER BY notification_date DESC LIMIT 1")
    StockNotification findUnreadByMedicineIdAndType(@Param("medicineId") Integer medicineId,
            @Param("type") String type);

    @Select("SELECT id, medicine_id, message, type, notification_date, is_read FROM stock_notifications WHERE medicine_id = #{medicineId} AND type = #{type} AND is_read = TRUE ORDER BY notification_date DESC LIMIT 1")
    StockNotification findReadByMedicineIdAndType(@Param("medicineId") Integer medicineId, @Param("type") String type);
}