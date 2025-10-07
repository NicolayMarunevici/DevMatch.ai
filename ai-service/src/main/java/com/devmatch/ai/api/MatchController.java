package com.devmatch.ai.api;

import com.devmatch.ai.domain.Mappers;
import com.devmatch.ai.domain.MatchResultDto;
import com.devmatch.ai.usecase.MatchUseCase;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai/match")
public class MatchController {

  private final MatchUseCase useCase;

  public MatchController(MatchUseCase useCase) {
    this.useCase = useCase;
  }

  /*что такое k*/
  @GetMapping("/user/{userId}")
  public ResponseEntity<List<MatchResultDto>> matchForUser(
      @PathVariable String userId,
      @RequestParam(defaultValue = "20") @Min(1) @Max(100) int k) {
    var results = useCase.matchForUser(userId, k)
        .stream().map(Mappers::toDto).toList();
    return ResponseEntity.ok(results);
  }

}
