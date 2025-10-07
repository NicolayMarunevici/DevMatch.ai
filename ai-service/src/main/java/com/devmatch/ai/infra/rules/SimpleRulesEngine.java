package com.devmatch.ai.infra.rules;

import com.devmatch.ai.domain.MatchCandidate;
import com.devmatch.ai.domain.MatchResult;
import com.devmatch.ai.ports.RulesEngine;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class SimpleRulesEngine implements RulesEngine {

  /*Что делает*/
  @Override
  public List<MatchResult> rescoreUserToVacancies(String userId, List<MatchCandidate> initial) {
    // TODO: подтягивать реальные данные из user-/vacancy-service (Feign)

    /*Что за числа */
    return initial.stream().map(c -> {
      double base = 0.80 * c.vectorScore();
//      double skill = 0.20 * 0.60; // пример: 60% пересечения must-have
      double geo = 0.20; // пример: гео ок
      double score = clamp(base + geo);

      Map<String, Double> feats = new LinkedHashMap<>();
      feats.put("vector", c.vectorScore());
//      feats.put("skill_overlap", 0.60);
      feats.put("location", 1.00);

      return new MatchResult(c.id(), score, feats);
    }).sorted(Comparator.comparingDouble(MatchResult::finalScore).reversed()).toList();
  }

  private static double clamp(double v) {
    return Math.max(0, Math.min(1, v));
  }
}
