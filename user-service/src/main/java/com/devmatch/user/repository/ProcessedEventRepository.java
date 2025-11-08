package com.devmatch.user.repository;

import com.devmatch.user.model.ProcessedEvents;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessedEventRepository extends JpaRepository<ProcessedEvents, UUID> {
}
