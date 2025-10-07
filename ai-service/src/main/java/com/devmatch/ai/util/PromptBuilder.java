package com.devmatch.ai.util;

import com.devmatch.ai.domain.RagChunk;
import java.util.List;
import java.util.stream.Collectors;

/** убирает хрупкость — всегда просим строгий JSON и управляем длиной контекста. */
public class PromptBuilder {

  public static String system(){
    return """
      Ты — ассистент по найму. Отвечай ТОЛЬКО на основе CONTEXT.
      Верни строго JSON без лишнего текста:
      {"summary":"...", "bullets":["...","..."], "citations":["chunkId1","chunkId2"]}
      Язык: русский. Если контекста недостаточно — укажи это в summary.
    """;
  }

  public static String user(List<RagChunk> ctx, String task) {
    // обрезаем контекст до разумной длины (≈ токен-бюджет)
    String contextBlock = ctx.stream()
        .map(c -> "### " + c.id() + "\n" + truncate(c.text(), 1800))
        .collect(Collectors.joining("\n\n"));
    return """
      CONTEXT:
      %s

      TASK:
      %s
    """.formatted(contextBlock, task);
  }

  private static String truncate(String s, int max){
    if (s.length() <= max) return s;
    return s.substring(0, max) + "…";
  }
}
