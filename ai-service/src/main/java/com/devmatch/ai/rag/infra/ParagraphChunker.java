package com.devmatch.ai.rag.infra;

import com.devmatch.ai.rag.domain.RagChunk;
import com.devmatch.ai.rag.port.Chunker;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

// Separates text to chunks, glues up to maxChars, does overlap
@Component
public class ParagraphChunker implements Chunker {
  private static final int MAX_CHARS = 2200; // ~ 600-800 tokens
  private static final int OVERLAP_CHARS = 200;
  private static final Pattern SPLIT = Pattern.compile("\\n{2,}");

  @Override
  public List<RagChunk> chunk(String ownerType, String ownerId, String text,
                              Map<String, String> meta) {
    var paras = Arrays.stream(text.split("\\R"))
        .map(String::trim).filter(s -> !s.isBlank()).toList();

    List<String> blocks = new ArrayList<>();
    StringBuilder cur = new StringBuilder();

    for (String p : paras) {
      if (cur.length() + p.length() + 1 > MAX_CHARS) {
        if (cur.length() > 0) {
          blocks.add(cur.toString());
          cur.setLength(0);
        }
      }
      if (cur.length() > 0) {
        cur.append("\n");
        cur.append(p);
      }
    }
    if (cur.length() > 0) {
      blocks.add(cur.toString());
    }

    // making overlap
    List<RagChunk> out = new ArrayList<>();
    String prevTail = "";
    for (int i = 0; i < blocks.size(); i++) {
      String b = (prevTail.isEmpty() ? "" : prevTail + "\n") + blocks.get(i);
      String id = (ownerType.startsWith("U") ? "u: " : ownerType.startsWith("V") ? "v:" : "d:") +
          ownerId + ":" + i;
      out.add(new RagChunk(id, ownerType, ownerId, b, meta == null ? Map.of() : meta));

      // refresh tail
      String blk = blocks.get(i);
      prevTail = blk.substring(Math.max(0, blk.length() - OVERLAP_CHARS));
    }
    return out;
  }
}
