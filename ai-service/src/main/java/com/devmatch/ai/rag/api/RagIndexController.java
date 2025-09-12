package com.devmatch.ai.rag.api;

import com.devmatch.ai.rag.usecase.IndexOwnerTextUseCase;
import jakarta.validation.constraints.NotBlank;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Зачем: единственный вход для (ре)индексации профилей/JD/доков в RAG.*/
@RestController
@RequestMapping("/api/ai/rag/index")
public class RagIndexController {
  private final IndexOwnerTextUseCase useCase;

  public RagIndexController(IndexOwnerTextUseCase useCase) {
    this.useCase = useCase;
  }

  public record IndexRequest(
      @NotBlank String ownerType,
      @NotBlank String ownerId,
      @NotBlank String text,
      Map<String, String> meta
  ) {}

  @PostMapping
  public ResponseEntity<Void> index(@RequestBody IndexRequest req) {
    useCase.reindex(req.ownerType(), req.ownerId(), req.text(), req.meta());
    return ResponseEntity.accepted().build();
  }
}
