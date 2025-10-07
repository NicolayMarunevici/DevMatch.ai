package com.devmatch.ai.domain;

import java.util.Map;

public record MatchResultDto(String targetId, double score, Map<String, Double> features) {
}