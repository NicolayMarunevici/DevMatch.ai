package com.devmatch.ai.infra.ai;

import com.devmatch.ai.ports.EmbeddingPort;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SpringAiEmbeddingAdapter implements EmbeddingPort {
  private final EmbeddingModel model;
  private final String modelName;
  private final int expectedDim;

  public SpringAiEmbeddingAdapter(EmbeddingModel model,
                                  @Value("${spring.ai.openai.embedding.options.model}")
                                  String modelName,
                                  @Value("${app.ai.embedding.expected-dim:1024}")
                                  int expectedDim) {
    this.model = model;
    this.modelName = modelName;
    this.expectedDim = expectedDim;
  }

  @Override
  public float[] embed(String text) {
    var doubles = model.embed(text);
    if(doubles.length != expectedDim) {
      throw new IllegalStateException("\"Embedding dim mismatch: got \" + doubles.length + \", expected \" + expectedDim");
    }
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
