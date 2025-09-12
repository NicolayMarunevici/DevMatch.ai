package com.devmatch.ai.rag.usecase;

import com.devmatch.ai.rag.domain.RagChunk;
import com.devmatch.ai.rag.port.Chunker;
import com.devmatch.ai.rag.port.EmbeddingPort;
import com.devmatch.ai.rag.port.RagRepository;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

/** Разрезает текст → эмбеддит чанки → пишет в БД (перезапись owner). */
@Service
public class IndexOwnerTextUseCase {
  private final Chunker chunker;
  private final EmbeddingPort embed;
  private final RagRepository repo;

  public IndexOwnerTextUseCase(Chunker chunker, EmbeddingPort embed, RagRepository repo) {
    this.chunker = chunker;
    this.embed = embed;
    this.repo = repo;
  }

  public void reindex(String ownerType, String ownerId, String rawText, Map<String,String> meta) {
    repo.deleteByOwner(ownerType, ownerId);
    List<RagChunk> chunks = chunker.chunk(ownerType, ownerId, rawText, meta);
    for (RagChunk c : chunks) {
      float[] vec = embed.embed(c.text());
      repo.insertChunk(c, embed.modelName(), vec);
    }
  }
}
