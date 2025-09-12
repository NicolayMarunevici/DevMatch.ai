package com.devmatch.ai.controller;

import com.devmatch.ai.usecase.ExplainMatchUseCase;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai/explain")
public class ExplanationController {
  private final ExplainMatchUseCase useCase;

  public ExplanationController(ExplainMatchUseCase useCase) {
    this.useCase = useCase;
  }

  public record ExplainRequest(
      @NotBlank String userId,
      @NotBlank String vacancyId,
      @NotBlank String userText,
      @NotBlank String vacancyText
  ) {}
  public record ExplainResponse(UUID id, String summary, List<String> bullets) {}

  @PostMapping("/match")
  public ResponseEntity<ExplainResponse> explain(@RequestBody ExplainRequest req) {
    var r = useCase.explainAndStore(req.userId(), req.vacancyId(), req.userText(), req.vacancyText());
    return ResponseEntity.ok(new ExplainResponse(r.id(), r.summary(), r.bullets()));
  }
}
