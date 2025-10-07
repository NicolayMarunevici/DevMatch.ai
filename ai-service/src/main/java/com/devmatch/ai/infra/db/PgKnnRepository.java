package com.devmatch.ai.infra.db;

import com.devmatch.ai.ports.KnnRepository;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PgKnnRepository implements KnnRepository {

  private final JdbcTemplate jdbcTemplate;

  public PgKnnRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }


  /*????????????????????*/
  @Override
  public List<Candidate> topKForUser(String userId, int k) {
    // Self-Join: избегаем передачи вектора в параметры JDBC
    // u — последний эмбеддинг пользователя; v — эмбеддинги вакансий

    // Более простой способ ----------

    String sql = """
          SELECT v.entity_id AS vacancy_id,
                 1 - (v.vector <#> u.vector) AS score
          FROM embeddings u
          JOIN embeddings v ON v.entity_type = 'VACANCY'
          WHERE u.entity_type = 'USER' AND u.entity_id = ?
          ORDER BY v.vector <#> u.vector
          LIMIT ?
        """;
    return jdbcTemplate.query(sql,
        ps -> {
          ps.setString(1, userId);
          ps.setInt(2, k);
        },
        (rs, rn) -> new Candidate(rs.getString("vacancy_id"), rs.getDouble("score")));
  }
}
