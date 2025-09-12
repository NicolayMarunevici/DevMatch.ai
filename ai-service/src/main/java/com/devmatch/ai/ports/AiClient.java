package com.devmatch.ai.ports;

import java.util.List;

/*Isolate Application from the particular library LLM.
* Any realisation (Spring AI/local) can be replaced
*/
public interface AiClient {
  float[] embedText(String text);
  Explanation explainMatch(String userText,String vacancyText);

  record Explanation(String summary, List<String> bullets){}
}
