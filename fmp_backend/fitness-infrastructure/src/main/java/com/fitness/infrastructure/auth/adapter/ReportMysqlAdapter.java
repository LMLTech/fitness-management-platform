package com.fitness.infrastructure.auth.adapter;

import com.fitness.core.auth.port.out.IReportRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ReportMysqlAdapter implements IReportRepositoryPort {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public BigDecimal calculateRevenueToday() {
        String sql = "SELECT COALESCE(SUM(amount), 0) FROM payments WHERE DATE(created_at) = CURDATE() AND status = 'Success'";
        return jdbcTemplate.queryForObject(sql, BigDecimal.class);
    }

    @Override
    public BigDecimal calculateRevenueThisMonth() {
        String sql = "SELECT COALESCE(SUM(amount), 0) FROM payments WHERE MONTH(created_at) = MONTH(CURDATE()) AND YEAR(created_at) = YEAR(CURDATE()) AND status = 'Success'";
        return jdbcTemplate.queryForObject(sql, BigDecimal.class);
    }

    @Override
    public Integer countNewMembersThisMonth() {
        String sql = "SELECT COUNT(id) FROM users WHERE MONTH(created_at) = MONTH(CURDATE()) AND YEAR(created_at) = YEAR(CURDATE())";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    @Override
    public Integer countActiveMembers() {
        String sql = "SELECT COUNT(id) FROM users WHERE status = 'Active'";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    @Override
    public Integer countCheckInsToday() {
        String sql = "SELECT COUNT(id) FROM check_in_logs WHERE DATE(check_in_time) = CURDATE()";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    @Override
    public Integer countPendingSupportTickets() {
        String sql = "SELECT COUNT(id) FROM support_tickets WHERE status = 'Open'";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    @Override
    public Map<String, BigDecimal> getRevenueLast7Days() {
        // Đồng nhất hàm DATE_FORMAT ở cả phần SELECT, GROUP BY và ORDER BY
        String sql = "SELECT DATE_FORMAT(created_at, '%d/%m') as log_date, COALESCE(SUM(amount), 0) as daily_total " +
                "FROM payments " +
                "WHERE created_at >= DATE_SUB(CURDATE(), INTERVAL 6 DAY) AND status = 'Success' " +
                "GROUP BY DATE_FORMAT(created_at, '%d/%m') " +
                "ORDER BY MIN(created_at) ASC";

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
        Map<String, BigDecimal> result = new LinkedHashMap<>();

        for (Map<String, Object> row : rows) {
            String dateLabel = (String) row.get("log_date");
            BigDecimal total = (BigDecimal) row.get("daily_total");
            result.put(dateLabel, total);
        }

        return result;
    }
}