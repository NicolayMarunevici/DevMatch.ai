package com.devmatch.ai.util;

import com.devmatch.ai.domain.RagChunk;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Reciprocal Rank Fusion: score = sum(1/(k + rank(list_i, id))). */
public class HybridRanker {
  public static List<RagChunk> fuse(List<RagChunk> vec, List<RagChunk> fts, int limit) {
    int k = 60; // сглаживание
    Map<String, Double> score = new HashMap<>();
    rank(vec).forEach((id, r) -> score.merge(id, 1.0/(k+r), Double::sum));
    rank(fts).forEach((id, r) -> score.merge(id, 1.0/(k+r), Double::sum));
    // уникальная карта id->chunk
    Map<String, RagChunk> byId = new LinkedHashMap<>();
    for (var c : vec) byId.putIfAbsent(c.id(), c);
    for (var c : fts) byId.putIfAbsent(c.id(), c);
    return byId.values().stream()
        .sorted((a,b) -> Double.compare(score.getOrDefault(b.id(),0.0), score.getOrDefault(a.id(),0.0)))
        .limit(limit)
        .toList();
  }
  private static Map<String,Integer> rank(List<RagChunk> l){ Map<String,Integer> m=new HashMap<>(); for(int i=0;i<l.size();i++) m.put(l.get(i).id(), i+1); return m; }

}
