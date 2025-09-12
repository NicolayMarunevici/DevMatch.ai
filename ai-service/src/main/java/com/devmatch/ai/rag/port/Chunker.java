package com.devmatch.ai.rag.port;

import com.devmatch.ai.rag.domain.RagChunk;
import java.util.List;
import java.util.Map;

// Separating text to chunks
public interface Chunker {
  List<RagChunk> chunk(String ownerType, String ownerId, String text, Map<String, String> meta);
}
