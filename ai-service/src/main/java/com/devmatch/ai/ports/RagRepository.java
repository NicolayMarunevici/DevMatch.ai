package com.devmatch.ai.ports;

import com.devmatch.ai.domain.RagChunk;
import java.util.List;
import java.util.Map;

// Single access point to DB (vector + FTS) without SQL leaks to the outside
public interface RagRepository {
  void insertChunk(RagChunk chunk, String model, float[] vector);

  void deleteByOwner(String ownerType, String ownerId);

  //  Top-K by chunk vector of the specific owner
  List<RagChunk> topKByVector(String ownerType, String ownerId, float[] queryVec, int k,
                              Map<String, String> filters);

  //  Top-M by the whole text
  List<RagChunk> topMByFts(String ownerType, String ownerId, String query, int m,
                           Map<String, String> filters);
}
