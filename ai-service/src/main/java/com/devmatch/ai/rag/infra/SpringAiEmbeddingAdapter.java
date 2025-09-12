package com.devmatch.ai.rag.infra;

import com.devmatch.ai.rag.port.EmbeddingPort;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SpringAiEmbeddingAdapter implements EmbeddingPort {
  private final EmbeddingModel model;
  private final String modelName;

  public SpringAiEmbeddingAdapter(EmbeddingModel model,
                                  @Value("${spring.ai.embedding.options.model}") String modelName) {
    this.model = model;
    this.modelName = modelName;
  }

  @Override
  public float[] embed(String text) {
    var doubles = model.embed(text);
    var out = new float[doubles.length];
    for (int i = 0; i < out.length; i++) {
      out[i] = doubles[i];
    }
    return out;
  }

  @Override
  public String modelName() {
    return modelName;
  }
}
