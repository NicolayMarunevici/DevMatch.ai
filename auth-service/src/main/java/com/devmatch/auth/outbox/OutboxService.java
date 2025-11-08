package com.devmatch.auth.outbox;

import com.devmatch.auth.model.OutboxEvent;
import com.devmatch.auth.repository.OutboxEventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OutboxService {
  private final ObjectMapper objectMapper;
  private final OutboxEventRepository outboxEventRepository;

  public OutboxService(ObjectMapper objectMapper, OutboxEventRepository outboxEventRepository) {
    this.objectMapper = objectMapper;
    this.outboxEventRepository = outboxEventRepository;
  }

//  Put event into Outbox inside business transaction
  @Transactional
  public UUID saveEvent(String topic, String key, Object event) {
    var e = new OutboxEvent();
    var eventId = UUID.randomUUID();
    e.setId(eventId);
    e.setTopic(topic);
    e.setEventKey(key);
    try {
      e.setPayload(objectMapper.valueToTree(event));
    } catch (Exception ex) {
      throw new RuntimeException("Failed to serialize event", ex);
    }
    outboxEventRepository.save(e);
    return eventId;
  }

  @Transactional
  public List<OutboxEvent> claimBatch(int size, int maxAttempts) {
    return outboxEventRepository.pickBatchForUpdateSkipLocked(size, maxAttempts);
  }

  @Transactional
  public Optional<OutboxEvent> claimOneForUpdate(UUID id) {
    return outboxEventRepository.findForUpdateSkipLocked(id) // native: SELECT ... FOR UPDATE SKIP LOCKED
        .filter(e -> !e.isPublished());
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void markPublished(UUID id) {
    outboxEventRepository.markPublished(id);
  }
}
