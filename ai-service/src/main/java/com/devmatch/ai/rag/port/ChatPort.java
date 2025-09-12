package com.devmatch.ai.rag.port;

// Generation LLM response
public interface ChatPort {

//  LLM invoking by the prompt. Are waining JSON
  String completeJson(String systemInstruction, String userPrompt);
}
