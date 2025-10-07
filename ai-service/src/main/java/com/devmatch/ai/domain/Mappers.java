package com.devmatch.ai.domain;

public class Mappers {
  private Mappers() {
  }

  public static MatchResultDto toDto(MatchResult r) {
    return new MatchResultDto(r.targetId(), r.finalScore(), r.features());
  }
}
