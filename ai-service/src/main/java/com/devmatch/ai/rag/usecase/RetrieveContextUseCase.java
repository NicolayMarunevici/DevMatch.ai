package com.devmatch.ai.rag.usecase;

import com.devmatch.ai.rag.domain.RagChunk;
import com.devmatch.ai.rag.port.EmbeddingPort;
import com.devmatch.ai.rag.port.RagRepository;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;


/** Гибридный ретрив: topK по вектору + topM по FTS → RRF слияние.
 * Зачем: подбирает компактный и релевантный контекст для LLM.
 * */
@Service
public class RetrieveContextUseCase {
  private final EmbeddingPort embed;
  private final RagRepository repo;

  public RetrieveContextUseCase(EmbeddingPort embed, RagRepository repo) {
    this.embed = embed;
    this.repo = repo;
  }

  public List<RagChunk> retrieve(String ownerType, String ownerId, String query, int k, int m, int finalLimit, Map<String,String> filters) {
    float[] qVec = embed.embed(query);
    var v = repo.topKByVector(ownerType, ownerId, qVec, k, filters);
    var f = repo.topMByFts(ownerType, ownerId, query, m, filters);
    return HybridRanker.fuse(v, f, finalLimit);
  }
}
