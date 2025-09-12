package com.devmatch.ai.api.dto;

import java.util.Map;

public record MatchResultDto(String targetId, double score, Map<String, Double> features) {}