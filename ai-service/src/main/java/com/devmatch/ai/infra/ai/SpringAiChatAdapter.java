package com.devmatch.ai.infra.ai;

import com.devmatch.ai.ports.ChatPort;
import java.util.List;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Component;


// Single point of call for LLM-chat
@Component
public class SpringAiChatAdapter implements ChatPort {
  private final ChatClient chat;

  private static final SystemMessage DEFAULT_SYS =
      new SystemMessage("You are DevMatch.AI assistant. Return STRICT JSON when asked. Be concise.");


  public SpringAiChatAdapter(ChatClient chat) {
    this.chat = chat;
  }

  @Override
  public String completeJson(String systemInstruction, String userPrompt) {
    Message sys = (systemInstruction == null || systemInstruction.isBlank()) ?
        DEFAULT_SYS : new SystemMessage(systemInstruction);

    return chat.prompt(new Prompt(List.of(
        sys, new UserMessage(userPrompt)
    ))).call().content();
  }
}

