package com.devmatch.ai.domain;

import java.util.Map;

/*
  Final Result
*/
public record MatchResult(String targetId, double finalScore, Map<String, Double> features) {
}
