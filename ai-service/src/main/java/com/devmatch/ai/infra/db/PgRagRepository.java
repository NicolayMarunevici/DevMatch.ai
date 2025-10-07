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

  public PgRagRepository(JdbcTemplate jdbc) { this.jdbc = jdbc; }

  @Override
  public void insertChunk(RagChunk c, String model, float[] vector) {
    String vec = toPgVector(vector);
    try {
      String metaJson = om.writeValueAsString(c.meta()==null? Map.of() : c.meta());
      jdbc.update("""
          INSERT INTO rag_chunks (id, owner_type, owner_id, model, dim, vector, text, meta, created_at)
          VALUES (?,?,?,?,?, ?::vector, ?, ?::jsonb, ?)
          ON CONFLICT (id) DO UPDATE SET text=EXCLUDED.text, meta=EXCLUDED.meta, vector=EXCLUDED.vector, model=EXCLUDED.model, dim=EXCLUDED.dim
        """,
          c.id(), c.ownerType(), c.ownerId(), model, vector.length, vec, c.text(), metaJson, Timestamp.from(
              Instant.now()));
    } catch (Exception e) {
      throw new IllegalStateException("RAG insert failed", e);
    }
  }

  @Override
  public void deleteByOwner(String ownerType, String ownerId) {
    jdbc.update("DELETE FROM rag_chunks WHERE owner_type=? AND owner_id=?", ownerType, ownerId);
  }

  @Override
  public List<RagChunk> topKByVector(String ownerType, String ownerId, float[] queryVec, int k, Map<String,String> filters) {
    String sql = """
      SELECT id, owner_type, owner_id, text, meta
      FROM rag_chunks
      WHERE owner_type=? AND owner_id=? %s
      ORDER BY vector <#> ?::vector
      LIMIT ?
    """.formatted(whereFilters(filters));
    return jdbc.query(sql,
        ps -> { ps.setString(1, ownerType); ps.setString(2, ownerId); ps.setObject(3, queryVec); ps.setInt(4, k); },
        (rs, rn) -> new RagChunk(
            rs.getString("id"),
            rs.getString("owner_type"),
            rs.getString("owner_id"),
            rs.getString("text"),
            readMeta(rs.getString("meta"))
        ));
  }

  @Override
  public List<RagChunk> topMByFts(String ownerType, String ownerId, String query, int m, Map<String,String> filters) {
    String sql = """
      SELECT id, owner_type, owner_id, text, meta
      FROM rag_chunks
      WHERE owner_type=? AND owner_id=? %s
      ORDER BY to_tsvector('simple', text) @@ plainto_tsquery('simple', ?) DESC,
               ts_rank_cd(to_tsvector('simple', text), plainto_tsquery('simple', ?)) DESC
      LIMIT ?
    """.formatted(whereFilters(filters));
    return jdbc.query(sql,
        ps -> { ps.setString(1, ownerType); ps.setString(2, ownerId); ps.setString(3, query); ps.setString(4, query); ps.setInt(5, m); },
        (rs, rn) -> new RagChunk(
            rs.getString("id"),
            rs.getString("owner_type"),
            rs.getString("owner_id"),
            rs.getString("text"),
            readMeta(rs.getString("meta"))
        ));
  }

  private static String whereFilters(Map<String,String> f) {
    if (f==null || f.isEmpty()) return "";
    // simple schema: meta->>'key'='value'
    StringBuilder sb = new StringBuilder();
    for (String k : f.keySet()) sb.append(" AND meta->>'").append(k).append("'='").append(f.get(k).replace("'", "''")).append("'");
    return sb.toString();
  }
  private Map<String,String> readMeta(String json){
    try { return om.readValue(json==null? "{}" : json, om.getTypeFactory().constructMapType(Map.class, String.class, String.class)); }
    catch(Exception e){ return Map.of(); }
  }
  private static String toPgVector(float[] v){ StringBuilder sb=new StringBuilder("["); for(int i=0;i<v.length;i++){ if(i>0) sb.append(","); sb.append(Float.toString(v[i])); } return sb.append("]").toString(); }

}
