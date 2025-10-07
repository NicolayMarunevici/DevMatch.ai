package com.devmatch.ai.api;


import com.devmatch.ai.usecase.ExplainWithRagUseCase;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai/rag/explain")
public class RagExplainController {
  private final ExplainWithRagUseCase useCase;

  public RagExplainController(ExplainWithRagUseCase useCase) {
    this.useCase = useCase;
  }

  public record ExplainRequest(@NotBlank String userId, @NotBlank String vacancyId) {}
  public record ExplainResponse(String json) {}

  @PostMapping("/match")
  public ResponseEntity<ExplainResponse> explain(@RequestBody ExplainRequest req) {
    String json = useCase.explainUserVacancy(req.userId(), req.vacancyId());
    return ResponseEntity.ok(new ExplainResponse(json)); // JSON summary/bullets/citations
  }
}
