package com.devmatch.ai.domain;

import java.util.List;

public record Explanation(String summary, List<String> bullets, List<String> citations) {
  public static Explanation fallback(List<String> citations) {
    return new Explanation("Недостаточно данных или ошибка формата", List.of(), citations);
  }
}
