//package com.devmatch.ai.infra.cache;
//
//import com.devmatch.ai.domain.MatchResult;
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import java.sql.Timestamp;
//import java.time.Duration;
//import java.time.Instant;
//import java.util.List;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Component;
//
//@Component
//public class DbCacheAdapter implements CachePort {
//
//  private final JdbcTemplate jdbc;
//  private final ObjectMapper om = new ObjectMapper();
//
//  public DbCacheAdapter(JdbcTemplate jdbc) {
//    this.jdbc = jdbc;
//  }
//
//  @Override
//  public List<MatchResult> getMatch(String key) {
//    return jdbc.query("SELECT payload, ttl_until FROM match_cache WHERE key=?",
//        ps -> ps.setString(1, key),
//        rs -> {
//          if (!rs.next()) {
//            return null;
//          }
//          Instant ttl = rs.getTimestamp("ttl_until").toInstant();
//          if (Instant.now().isAfter(ttl)) {
//            jdbc.update("DELETE FROM match_cache WHERE key=?", key);
//            return null;
//          }
//          try {
//            String json = rs.getString("payload");
//            return om.readValue(json, new TypeReference<>() {
//            });
//          } catch (Exception e) {
//            return null;
//          }
//        });
//  }
//
//  @Override
//  public void putMatch(String key, List<MatchResult> value, Duration ttl) {
//    try {
//      String json = om.writeValueAsString(value);
//      jdbc.update("""
//              INSERT INTO match_cache(key,payload,ttl_until)
//              VALUES (?,?,?)
//              ON CONFLICT (key)
//              DO UPDATE SET payload=excluded.payload, ttl_until=excluded.ttl_until
//              """,
//          key, json, Timestamp.from(Instant.now().plus(ttl)));
//    } catch (Exception ignored) {
//
//    }
//  }
//}
