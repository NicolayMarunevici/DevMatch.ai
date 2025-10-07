package com.devmatch.ai.ports;

import java.util.ArrayList;
import java.util.List;

public interface EmbeddingPort {

  // returns Embedding text. The size should match the column vector(N) in DB
  float[] embed(String text);

  // returns name of the model
  String modelName();

  default List<float[]> embedBatch(List<String> texts) {
    List<float[]> out = new ArrayList<>(texts.size());
    for (String t : texts) {
      out.add(embed(t));
    }
    return out;
  }


}
