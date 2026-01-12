package com.example.pharmacy.mapper;

import org.apache.ibatis.jdbc.SQL;
import java.time.LocalDateTime;
import java.util.Map;

public class SaleSqlBuilder {

    public String buildFindSalesWithFiltersSql(Map<String, Object> params) {
        return new SQL() {
            {
                SELECT("s.id, s.user_id, s.sale_date, s.total_amount, s.discount_details, s.payment_method, u.username as cashierName");
                FROM("sales s");
                LEFT_OUTER_JOIN("users u ON s.user_id = u.id");

                if (params.get("startDate") != null && params.get("endDate") != null) {
                    AND();
                    WHERE("s.sale_date BETWEEN #{startDate} AND #{endDate}");
                }
                if (params.get("cashierName") != null && !((String) params.get("cashierName")).isEmpty()) {
                    AND();
                    WHERE("u.username LIKE CONCAT('%', #{cashierName}, '%')");
                }
                if (params.get("minAmount") != null) {
                    AND();
                    WHERE("s.total_amount >= #{minAmount}");
                }
                if (params.get("maxAmount") != null) {
                    AND();
                    WHERE("s.total_amount <= #{maxAmount}");
                }
                if (params.get("hasDiscount") != null && (Boolean) params.get("hasDiscount")) {
                    AND();
                    WHERE("s.discount_details IS NOT NULL");
                }
                if (params.get("searchKeyword") != null && !((String) params.get("searchKeyword")).isEmpty()) {
                    AND();
                    WHERE("s.id LIKE CONCAT('%', #{searchKeyword}, '%') OR " +
                            "u.username LIKE CONCAT('%', #{searchKeyword}, '%') OR " +
                            "s.discount_details LIKE CONCAT('%', #{searchKeyword}, '%') OR " +
                            "s.payment_method LIKE CONCAT('%', #{searchKeyword}, '%')");
                }

                if (params.get("orderBy") != null && !((String) params.get("orderBy")).isEmpty()) {
                    ORDER_BY(params.get("orderBy") + " " + params.get("orderDirection"));
                }
                LIMIT("#{pageSize}");
                OFFSET("#{offset}");
            }
        }.toString();
    }

    public String buildCountSalesWithFiltersSql(Map<String, Object> params) {
        return new SQL() {
            {
                SELECT("COUNT(*)");
                FROM("sales s");
                LEFT_OUTER_JOIN("users u ON s.user_id = u.id");

                if (params.get("startDate") != null && params.get("endDate") != null) {
                    AND();
                    WHERE("s.sale_date BETWEEN #{startDate} AND #{endDate}");
                }
                if (params.get("cashierName") != null && !((String) params.get("cashierName")).isEmpty()) {
                    AND();
                    WHERE("u.username LIKE CONCAT('%', #{cashierName}, '%')");
                }
                if (params.get("minAmount") != null) {
                    AND();
                    WHERE("s.total_amount >= #{minAmount}");
                }
                if (params.get("maxAmount") != null) {
                    AND();
                    WHERE("s.total_amount <= #{maxAmount}");
                }
                if (params.get("hasDiscount") != null && (Boolean) params.get("hasDiscount")) {
                    AND();
                    WHERE("s.discount_details IS NOT NULL");
                }
                if (params.get("searchKeyword") != null && !((String) params.get("searchKeyword")).isEmpty()) {
                    AND();
                    WHERE("s.id LIKE CONCAT('%', #{searchKeyword}, '%') OR " +
                            "u.username LIKE CONCAT('%', #{searchKeyword}, '%') OR " +
                            "s.discount_details LIKE CONCAT('%', #{searchKeyword}, '%') OR " +
                            "s.payment_method LIKE CONCAT('%', #{searchKeyword}, '%')");
                }
            }
        }.toString();
    }
}