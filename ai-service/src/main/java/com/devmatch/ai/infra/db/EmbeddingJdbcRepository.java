package com.devmatch.ai.infra.db;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class EmbeddingJdbcRepository {
  private final JdbcTemplate jdbcTemplate;

  public EmbeddingJdbcRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  /* Simple and reliable embedding record */
  public UUID insert(String entityType, String entityId, String model, float[] vector){
    UUID id = UUID.randomUUID();
    int dim = vector.length;
    String vec = toPgVector(vector); // [0.1, 0.2, ...]
    jdbcTemplate.update("""
        INSERT INTO embeddings (id, entity_type, entity_id, model, dim, vector, created_at)
        VALUES (?,?,?,?,?, ?::vector, ?)
        """,
        id, entityType, entityId, model, dim, vec, Timestamp.from(Instant.now()));
    return id;
  }

  private static String toPgVector(float[] v){
    StringBuilder sb = new StringBuilder("[");
    for (int i = 0; i < v.length; i++) {
      if(i > 0){
        sb.append(", ");
        sb.append(v[i]);
      }
    }
    return sb.append("]").toString();
  }
}
