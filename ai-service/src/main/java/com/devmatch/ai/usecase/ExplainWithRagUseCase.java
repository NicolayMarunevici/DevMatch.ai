package com.devmatch.ai.usecase;


import com.devmatch.ai.domain.Explanation;
import com.devmatch.ai.domain.RagChunk;
import com.devmatch.ai.ports.ChatPort;
import com.devmatch.ai.util.PromptBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Строит контекст по профилю и по JD, собирает промпт и запрашивает LLM (JSON).
 * Зачем: полноценный RAG-контур для объяснения матча. Возвращает JSON с bullets и citations.
 */
@Service
public class ExplainWithRagUseCase {
  private final RetrieveContextUseCase retriever;
  private final ChatPort chat;
  private final ObjectMapper om;

  public ExplainWithRagUseCase(RetrieveContextUseCase retriever, ChatPort chat, ObjectMapper om) {
    this.retriever = retriever;
    this.chat = chat;
    this.om = om;
  }

  public Explanation explainUserVacancy(String userId, String vacancyId) {
    // контекст с двух сторон
    List<RagChunk> userCtx = retriever.retrieve("USER", userId,
        "Опыт и ключевые навыки кандидата", 6, 6, 5, null);
    List<RagChunk> vacCtx  = retriever.retrieve("VACANCY", vacancyId,
        "Основные требования и обязанности вакансии", 6, 6, 5, null);

    List<RagChunk> ctx = new ArrayList<>(userCtx);
    ctx.addAll(vacCtx);

    String sys  = PromptBuilder.system();
    String user = PromptBuilder.user(ctx,
        "Объясни, почему кандидат подходит под вакансию. Дай 3–5 bullets и список citations (ID чанков).");

    String json = chat.completeJson(sys, user);
    try {
      return om.readValue(json, Explanation.class);
    } catch (Exception e) {
      // graceful degradation
      List<String> ids = ctx.stream().map(RagChunk::id).toList();
      return Explanation.fallback(ids);
    }
  }
}
