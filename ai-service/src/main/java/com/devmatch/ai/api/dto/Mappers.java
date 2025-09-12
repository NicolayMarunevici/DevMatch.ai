package com.devmatch.ai.api.dto;

import com.devmatch.ai.domain.MatchResult;

public class Mappers {
  private Mappers() {}
  public static MatchResultDto toDto(MatchResult r) {
    return new MatchResultDto(r.targetId(), r.finalScore(), r.features());
  }
}
