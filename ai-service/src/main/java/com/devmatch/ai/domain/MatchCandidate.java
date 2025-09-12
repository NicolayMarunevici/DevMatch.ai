package com.devmatch.ai.domain;

/*
  Candidate from vector search
*/
public record MatchCandidate(String id, double vectorScore) {
}
