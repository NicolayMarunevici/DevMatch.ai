package com.devmatch.ai.infra.ai;

import com.devmatch.ai.ports.EmbeddingPort;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SpringAiEmbeddingAdapter implements EmbeddingPort {
  private final EmbeddingModel model;
  private final String modelName;

  public SpringAiEmbeddingAdapter(EmbeddingModel model,
                                  @Value("${spring.ai.openai.embedding.options.model}")
                                  String modelName) {
    this.model = model;
    this.modelName = modelName;
  }

  @Override
  public float[] embed(String text) {
    var doubles = model.embed(text);
    var out = new float[doubles.length];
    System.arraycopy(doubles, 0, out, 0, out.length);
    return out;
  }

  @Override
  public String modelName() {
    return modelName;
  }
}
