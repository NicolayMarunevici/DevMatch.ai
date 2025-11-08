package com.devmatch.ai.infra.db;

import com.devmatch.ai.domain.RagChunk;
import com.devmatch.ai.ports.RagRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


// Stores chunks + search (vector + FTS)
@Repository
public class PgRagRepository implements RagRepository {

  private final JdbcTemplate jdbc;
  private final ObjectMapper om = new ObjectMapper();

  public PgRagRepository(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

  @Override
  public void insertChunk(RagChunk c, String model, float[] vector) {
    try {
      String sql = """
      INSERT INTO rag_chunks (id, owner_type, owner_id, model, dim, vector, text, meta, created_at)
      VALUES (?, ?, ?, ?, ?, ?::vector, ?, ?::jsonb, ?)
      ON CONFLICT (id) DO UPDATE SET
        owner_type = EXCLUDED.owner_type,
        owner_id   = EXCLUDED.owner_id,
        model      = EXCLUDED.model,
        dim        = EXCLUDED.dim,
        vector     = EXCLUDED.vector,
        text       = EXCLUDED.text,
        meta       = EXCLUDED.meta
    """;
      jdbc.update(sql, ps -> {
        ps.setString(1, c.id());
        ps.setString(2, c.ownerType());
        ps.setString(3, c.ownerId());
        ps.setString(4, model);
        ps.setInt(5, vector.length);
        ps.setString(6, toPgVector(vector));
        ps.setString(7, c.text());
        ps.setString(8, writeMeta(c.meta()));
        ps.setTimestamp(9, Timestamp.from(Instant.now()));
      });
    } catch (Exception e) {
      throw new IllegalStateException("RAG insert failed", e);
    }
  }

  @Override
  public void insertChunksBatch(List<RagChunk> chunks, String model, List<float[]> vectors) {
    if (chunks.size() != vectors.size()) {
      throw new IllegalArgumentException("chunks.size != vectors.size");
    }
    String sql = """
      INSERT INTO rag_chunks (id, owner_type, owner_id, model, dim, vector, text, meta, created_at)
      VALUES (?, ?, ?, ?, ?, ?::vector, ?, ?::jsonb, ?)
      ON CONFLICT (id) DO UPDATE SET
        owner_type = EXCLUDED.owner_type,
        owner_id   = EXCLUDED.owner_id,
        model      = EXCLUDED.model,
        dim        = EXCLUDED.dim,
        vector     = EXCLUDED.vector,
        text       = EXCLUDED.text,
        meta       = EXCLUDED.meta
    """;
    jdbc.batchUpdate(sql, chunks, 500, (ps, c) -> {
      int idx = chunks.indexOf(c);
      float[] v = vectors.get(idx);
      ps.setString(1, c.id());
      ps.setString(2, c.ownerType());
      ps.setString(3, c.ownerId());
      ps.setString(4, model);
      ps.setInt(5, v.length);
      ps.setString(6, toPgVector(v));
      ps.setString(7, c.text());
      ps.setString(8, writeMeta(c.meta()));
      ps.setTimestamp(9, Timestamp.from(Instant.now()));
    });
  }

  @Override
  public void deleteByOwner(String ownerType, String ownerId) {
    jdbc.update("DELETE FROM rag_chunks WHERE owner_type=? AND owner_id=?", ownerType, ownerId);
  }

  @Override
  public List<RagChunk> topKByVector(String ownerType, String ownerId, float[] queryVec, int k,
                                     Map<String, String> filters) {
    String sql = """
      SELECT id, owner_type, owner_id, text, meta,
             1 - (vector <#> ?::vector) AS score
      FROM rag_chunks
      WHERE owner_type = ? AND owner_id = ?
      %s
      ORDER BY vector <#> ?::vector
      LIMIT ?
    """.formatted(whereFilters(filters));
    String vec = toPgVector(queryVec);
    return jdbc.query(sql,
        ps -> {
          ps.setString(1, vec);
          ps.setString(2, ownerType);
          ps.setString(3, ownerId);
          ps.setString(4, vec);
          ps.setInt(5, k);
        },
        (rs, rn) -> new RagChunk(
            rs.getString("id"),
            rs.getString("owner_type"),
            rs.getString("owner_id"),
            rs.getString("text"),
            readMeta(rs.getString("meta"))
        )
    );
  }

  @Override
  public List<RagChunk> topMByFts(String ownerType, String ownerId, String query, int m,
                                  Map<String, String> filters) {
    String sql = """
      SELECT id, owner_type, owner_id, text, meta
      FROM rag_chunks
      WHERE owner_type = ? AND owner_id = ?
        AND to_tsvector('simple', text) @@ plainto_tsquery('simple', ?)
        %s
      ORDER BY id
      LIMIT ?
    """.formatted(whereFilters(filters));
    return jdbc.query(sql,
        ps -> {
          ps.setString(1, ownerType);
          ps.setString(2, ownerId);
          ps.setString(3, query);
          ps.setInt(4, m);
        },
        (rs, rn) -> new RagChunk(
            rs.getString("id"),
            rs.getString("owner_type"),
            rs.getString("owner_id"),
            rs.getString("text"),
            readMeta(rs.getString("meta"))
        )
    );
  }

  /* --- helpers --- */

  private static String whereFilters(Map<String, String> f) {
    if (f == null || f.isEmpty()) return "";
    StringBuilder sb = new StringBuilder();
    for (Map.Entry<String,String> e : f.entrySet()) {
      // meta->>'key' = 'value'
      sb.append(" AND meta->>'")
          .append(e.getKey().replace("'", "''"))
          .append("'='")
          .append(e.getValue().replace("'", "''"))
          .append("'");
    }
    return sb.toString();
  }

  private String writeMeta(Map<String, String> meta) {
    try { return om.writeValueAsString(meta == null ? Map.of() : meta); }
    catch (Exception e) { return "{}"; }
  }

  private Map<String, String> readMeta(String json) {
    try {
      return om.readValue(json == null ? "{}" : json,
          om.getTypeFactory().constructMapType(Map.class, String.class, String.class));
    } catch (Exception e) {
      return Map.of();
    }
  }

  private static String toPgVector(float[] v) {
    StringBuilder sb = new StringBuilder("[");
    for (int i = 0; i < v.length; i++) {
      if (i > 0) {
        sb.append(",");
      }
      sb.append(v[i]);
    }
    return sb.append("]").toString();
  }

}
