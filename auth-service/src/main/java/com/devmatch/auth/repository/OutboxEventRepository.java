package com.devmatch.auth.repository;

import com.devmatch.auth.model.OutboxEvent;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, UUID> {

  @Query(value = """
      SELECT *
      FROM outbox_events
      WHERE published = false
        AND attempts <= :maxAttempts
      ORDER BY created_at, id
      FOR UPDATE SKIP LOCKED
      LIMIT :batchSize
      """, nativeQuery = true)
  List<OutboxEvent> pickBatchForUpdateSkipLocked(@Param("batchSize") int batchSize,
                                                 @Param("maxAttempts") int maxAttempts);


  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query(value = """
        UPDATE outbox_events
           SET published = TRUE
         WHERE id = :id
           AND published = FALSE
      """, nativeQuery = true)
  int markPublished(@Param("id") UUID id);

  @Query(value = """
      SELECT * FROM outbox_events
      WHERE id = :id AND published = false
      FOR UPDATE SKIP LOCKED
      """, nativeQuery = true)
  Optional<OutboxEvent> findForUpdateSkipLocked(@Param("id") UUID id);
}
