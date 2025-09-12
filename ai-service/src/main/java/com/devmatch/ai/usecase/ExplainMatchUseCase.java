package com.devmatch.ai.usecase;


import com.devmatch.ai.infra.db.ExplanaitionJdbcRepository;
import com.devmatch.ai.ports.AiClient;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class ExplainMatchUseCase {
  private final AiClient aiClient;
  private final ExplanaitionJdbcRepository repo;

  public ExplainMatchUseCase(AiClient aiClient, ExplanaitionJdbcRepository repo) {
    this.aiClient = aiClient;
    this.repo = repo;
  }


/* Launch LLM with limit prompt. Save explanation, return result */
  public Result explainAndStore(String userId, String vacancyId, String userText, String vacancyText){
    AiClient.Explanation e = aiClient.explainMatch(userText, vacancyText);
    UUID id = repo.insert(userId, vacancyId, e.summary(), e.bullets());
    return new Result(id, e.summary(), e.bullets());
  }


  public record Result(UUID id, String summary, List<String> bullets){}
}
