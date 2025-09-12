package com.devmatch.ai.controller;

import com.devmatch.ai.usecase.CreateEmbeddingUseCase;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai/embed")
public class EmbeddingController {
  private final CreateEmbeddingUseCase useCase;

  public EmbeddingController(CreateEmbeddingUseCase useCase) {
    this.useCase = useCase;
  }

  public record EmbedRequest(
      @NotBlank String entityType,   // USER | VACANCY
      @NotBlank String entityId,
      @NotEmpty List<String> texts,
      @NotBlank String model
  ){}

  public record EmbedResponse(UUID emdeddingId){}

  @PostMapping
  public ResponseEntity<EmbedResponse> create(@RequestBody EmbedRequest request) {
    UUID id =
        useCase.create(request.entityType(), request.entityId(), request.texts(), request.model());
    return ResponseEntity.ok(new EmbedResponse(id));
  }
}
