package com.devmatch.ai.infra.llm;

import com.devmatch.ai.ports.AiClient;
import java.util.ArrayList;
import java.util.List;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Component;


/*Single point of invoking Spring AI
* Embedding
* Chat explanation
* Application does not depend on the specific client
* */

@Component
public class SpringAiClient implements AiClient {

  private final EmbeddingModel embeddingModel;
  private final ChatClient chatClient;

  public SpringAiClient(EmbeddingModel embeddingModel, ChatClient chatClient) {
    this.embeddingModel = embeddingModel;
    this.chatClient = chatClient;
  }

  @Override
  public float[] embedText(String text) {
    var doubles = embeddingModel.embed(text);
    var out = new float[doubles.length];
    for (int i = 0; i < doubles.length; i++) {
      out[i] = doubles[i];
    }
    return out;
  }

  @Override
  public Explanation explainMatch(String userText, String vacancyText) {
    var sys = """
          You are a technical assistant for hire. Keep it short and to the point.
          Format:
          - 1–2 summary sentences
          - 3–5 bullet points (specific matches in skills/experience)
           Only facts from the input texts. Language: Russian.
        """;

    var usr = """
          [CANDIDATE]
        %s

        [JOB]
        %s
        """.formatted(userText, vacancyText);

    var res = chatClient.prompt(new Prompt(List.of(new SystemMessage(sys), new UserMessage(usr))))
        .call().content();

    String[] lines = res.split("\\R+");
    String summary = lines.length > 0 ? lines[0] : res;
    List<String> bullets = new ArrayList<>();
    for (String l : lines) {
      var t = l.trim();
      if (t.startsWith("-")) {
        bullets.add(t.substring(1).trim());
      }
      if (bullets.size() >= 5) {
        break;
      }
      if (bullets.isEmpty()) {
        bullets = List.of(res);
      }
    }
    return new Explanation(summary, bullets);
  }
}
