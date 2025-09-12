package com.devmatch.vacancy.repository;

import com.devmatch.vacancy.dto.VacancyStatus;
import com.devmatch.vacancy.model.Vacancy;
import java.util.Collection;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VacancyRepository extends JpaRepository<Vacancy, Long> {
  Page<Vacancy> findByOwnerId(Long ownerId, Pageable pageable);
  Page<Vacancy> findByStatus(VacancyStatus status, Pageable pageable);
  Page<Vacancy> findByOwnerIdAndStatusIn(Long ownerId, Collection<VacancyStatus> statuses, Pageable p);
}
