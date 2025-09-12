package com.devmatch.ai.infra.db;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ExplanaitionJdbcRepository {
  private final JdbcTemplate jdbc;
  private final ObjectMapper om = new ObjectMapper();

  public ExplanaitionJdbcRepository(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

  /*Saves summary and bullets (jsonb) for further analyzing*/
  public UUID insert(String userId, String vacancyId, String summary, List<String> bullets){
    var id = UUID.randomUUID();
    try {
      String bulletsJson = om.writeValueAsString(bullets);
      jdbc.update("""
          INSERT INTO explanations (id, user_id, vacancy_id, summary, bullets, created_at)
          VALUES (?,?,?,?,?::jsonb,?)
          """, id, userId, vacancyId, summary, bulletsJson, Timestamp.from(Instant.now()));
      return id;
    } catch (Exception e){
      throw new IllegalStateException("Failed to persist explanation", e);
    }
  }
}
