package com.deemo.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class GameDao {
    private final JdbcTemplate jdbcTemplate;

    public GameDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean insert(String game, double price) {
        String sql = "INSERT INTO `game` (`name`, `price`, `description`) VALUE (?, ?, ?)";
        return jdbcTemplate.update(sql, game, price, game + " ￥" + price) > 0;
    }

}
