package com.devmatch.user.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "processed_events")
public class ProcessedEvents {

  @Id
  private UUID id;
  private Instant processedAt;

  public ProcessedEvents() {
  }
  public ProcessedEvents(UUID id, Instant processedAt) {
    this.id = id;
    this.processedAt = processedAt;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public Instant getProcessedAt() {
    return processedAt;
  }

  public void setProcessedAt(Instant processedAt) {
    this.processedAt = processedAt;
  }
}
