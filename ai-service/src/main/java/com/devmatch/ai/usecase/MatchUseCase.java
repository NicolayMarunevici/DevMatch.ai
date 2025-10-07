package com.devmatch.ai.usecase;

import com.devmatch.ai.domain.MatchCandidate;
import com.devmatch.ai.domain.MatchResult;
import com.devmatch.ai.ports.KnnRepository;
import com.devmatch.ai.ports.RulesEngine;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MatchUseCase {

  private final KnnRepository knnRepository;
  private final RulesEngine rulesEngine;
  //  private final CachePort cachePort;
  private final int overfetch;

  public MatchUseCase(KnnRepository knnRepository, RulesEngine rulesEngine,
                      @Value("${ai.match.overfetch:3}") int overfetch) {
    this.knnRepository = knnRepository;
    this.rulesEngine = rulesEngine;
    this.overfetch = overfetch;
  }

  /*Coordinate matching scenarios. First KNN, then rules, then reducing by k*/
  public List<MatchResult> matchForUser(String userId, int k) {
    List<KnnRepository.Candidate> initial =
        knnRepository.topKForUser(userId, k * Math.max(1, overfetch));
    List<MatchCandidate> domainCandidates = initial.stream()
        .map(c -> new MatchCandidate(c.id(), c.vectoreStore()))
        .collect(Collectors.toList());
    List<MatchResult> rescored = rulesEngine.rescoreUserToVacancies(userId, domainCandidates);
    return rescored.stream().limit(k).toList();
  }
}
