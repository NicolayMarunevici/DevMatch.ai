package com.devmatch.ai.usecase;

import com.devmatch.ai.infra.db.EmbeddingJdbcRepository;
import com.devmatch.ai.ports.AiClient;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class CreateEmbeddingUseCase {
  private final AiClient ai;
  private final EmbeddingJdbcRepository repo;

  public CreateEmbeddingUseCase(AiClient ai, EmbeddingJdbcRepository repo) {
    this.ai = ai;
    this.repo = repo;
  }

  public UUID create(String entityType, String entityId, List<String> texts, String modelName){
    String merged = String.join("\n", texts);
    if(merged.isBlank()){
      throw new IllegalArgumentException("Text is empty");
    }
    float[] vec = ai.embedText(merged);
    return repo.insert(entityType, entityId, modelName, vec);
  }
}
