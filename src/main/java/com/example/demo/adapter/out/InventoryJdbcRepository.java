package com.example.demo.adapter.out;

import com.example.demo.application.port.out.InventoryRepository;
import com.example.demo.domain.Inventory;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class InventoryJdbcRepository implements InventoryRepository {

  private final JdbcClient jdbc;

  public boolean decrease(String id, long total) {
    return jdbc.sql("""
            UPDATE inventories
            SET total_available = total_available - :total
            WHERE id = :id AND total_available >= :total
            """)
        .param("id", id)
        .param("total", total)
        .update() == 1;
  }

  public Optional<Inventory> findById(String id) {
    return jdbc.sql("""
            SELECT id, total_available, created_at
            FROM inventories
            WHERE id = :id
            """)
        .param("id", id)
        .query((rs, rowNum) -> new Inventory(
            rs.getString("id"),
            rs.getLong("total_available"),
            rs.getString("created_at")))
        .optional();
  }

}
