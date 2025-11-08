package com.devmatch.auth.model;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "outbox_events")
public class OutboxEvent {
  @Id
  private UUID id;
  private String topic;
  @Column(name = "event_key")
  private String eventKey;
  @Column(name = "payload", columnDefinition = "jsonb", nullable = false)
  @JdbcTypeCode(SqlTypes.JSON)
  private JsonNode payload;
  @Column(name = "created_at")
  private Instant createdAt = Instant.now();
  private boolean published = false;
  private int attempts = 0;

  public UUID getId() { return id; }
  public void setId(UUID id) { this.id = id; }
  public String getTopic() { return topic; }
  public void setTopic(String topic) { this.topic = topic; }
  public String getEventKey() { return eventKey; }
  public void setEventKey(String eventKey) { this.eventKey = eventKey; }
  public JsonNode getPayload() { return payload; }
  public void setPayload(JsonNode payload) { this.payload = payload; }
  public Instant getCreatedAt() { return createdAt; }
  public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
  public boolean isPublished() { return published; }
  public void setPublished(boolean published) { this.published = published; }
  public int getAttempts() { return attempts; }
  public void setAttempts(int attempts) { this.attempts = attempts; }


  @Override
  public String toString() {
    return "OutboxEvent{" +
        "id=" + id +
        ", topic='" + topic + '\'' +
        ", eventKey='" + eventKey + '\'' +
        ", payload=" + payload +
        ", createdAt=" + createdAt +
        ", published=" + published +
        ", attempts=" + attempts +
        '}';
  }
}
