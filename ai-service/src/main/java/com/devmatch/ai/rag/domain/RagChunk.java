package com.devmatch.ai.rag.domain;

import java.util.Map;

// Rag works better on fragments, not all documents. It is cheaper and more clear
public record RagChunk(
    String id, // example: "u:<userId>:<n>"
    String ownerType, // USER | VACANCY | DOC
    String ownerId, // userId / vacancyId
    String text, // fragment of tokens (300-800 token)
    Map<String, String> meta // язык, стек, секция
) {}
