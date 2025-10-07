package com.devmatch.ai.ports;

import java.util.List;

/*
 *  Зачем Knn
 */
public interface KnnRepository {
  record Candidate(String id, double vectoreStore) {
  }

  List<Candidate> topKForUser(String userId, int k);
}
