package com.devmatch.ai.usecase;

import com.devmatch.ai.domain.RagChunk;
import com.devmatch.ai.ports.Chunker;
import com.devmatch.ai.ports.EmbeddingPort;
import com.devmatch.ai.ports.RagRepository;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Разрезает текст → эмбеддит чанки → пишет в БД (перезапись owner).
 */
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

  @Transactional
  public void reindex(String ownerType, String ownerId, String rawText, Map<String, String> meta) {
//    remove old
    repo.deleteByOwner(ownerType, ownerId);
//    prepare chanks
    List<RagChunk> chunks = chunker.chunk(ownerType, ownerId, rawText, meta);

    if(chunks.isEmpty()){
      return;
    }

    List<String> texts = chunks.stream().map(RagChunk::text).toList();
    var vectors = embed.embedBatch(texts);

    repo.insertChunksBatch(chunks, embed.modelName(), vectors);
  }
}
