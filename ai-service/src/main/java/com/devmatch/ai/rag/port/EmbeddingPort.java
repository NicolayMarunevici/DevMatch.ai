package com.devmatch.ai.rag.port;

public interface EmbeddingPort {

  // returns Embedding text. The size should match the column vector(N) in DB
  float[] embed(String text);

  // returns name of the model
  String modelName();
}
