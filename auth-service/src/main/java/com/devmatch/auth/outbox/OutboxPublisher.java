package com.devmatch.auth.outbox;

import com.devmatch.auth.model.OutboxEvent;
import com.devmatch.auth.repository.OutboxEventRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

// Publisher sends to Kafka and mark published=true
@Service
public class OutboxPublisher {
  private final OutboxEventRepository repo;
  private final KafkaTemplate<String, JsonNode> kafka;
  private final OutboxService outboxService;
  private final ObjectMapper objectMapper;

  Logger logger = LoggerFactory.getLogger(OutboxPublisher.class);

  public OutboxPublisher(OutboxEventRepository repo, KafkaTemplate<String, JsonNode> kafka,
                         OutboxService outboxService, ObjectMapper objectMapper) {
    this.repo = repo;
    this.kafka = kafka;
    this.outboxService = outboxService;
    this.objectMapper = objectMapper;
  }

  @Scheduled(fixedDelayString = "${outbox.publisher.delay:1000}")
  @Transactional
  public void publishBatch() {
    List<OutboxEvent> batch = outboxService.claimBatch(10, 5);

    if(batch.isEmpty()) {
      return;
    }

    for (var e: batch) {
      try {
        kafka.send(e.getTopic(), e.getEventKey(), e.getPayload()).get();
        outboxService.markPublished(e.getId());
        logger.info("--- Sent register user event to user service. Event {}", e.toString());
      } catch (Exception ex) {
        e.setAttempts(e.getAttempts() + 1);
        if(e.getAttempts() > 5) {
          kafka.send("devmatch.dlq.v1", e.getEventKey(), e.getPayload());
          e.setPublished(true);
        }
      }
      repo.save(e);
    }
  }

  public void publishOne(UUID id) throws Exception{
    var opt = outboxService.claimOneForUpdate(id);
    if(opt.isEmpty()) return;

    var e = opt.get();
    try {
      kafka.send(e.getTopic(), e.getEventKey(), e.getPayload()).get();
    } catch (InterruptedException ie) {
      Thread.currentThread().interrupt();
      throw ie;
    }
    outboxService.markPublished(e.getId());
  }

}








