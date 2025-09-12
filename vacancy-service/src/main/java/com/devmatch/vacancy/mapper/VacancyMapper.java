package com.devmatch.vacancy.mapper;

import com.devmatch.vacancy.dto.VacancyResponse;
import com.devmatch.vacancy.dto.VacancyStatus;
import com.devmatch.vacancy.model.Vacancy;
import com.devmatch.vacancy.dto.VacancyRequest;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class VacancyMapper {

  public Vacancy toNewEntity(VacancyRequest req, Long ownerId) {
    Objects.requireNonNull(req, "VacancyRequest must not be null");
    Objects.requireNonNull(ownerId, "ownerId must not be null");

    Vacancy v = new Vacancy();
    // id/createdAt/updatedAt/version выставятся автоматически (lifecycle hooks)
    v.setTitle(trimToNull(req.getTitle()));
    v.setDescription(req.getDescription()); // допускаем длинный текст, без trim
    v.setLocation(trimToNull(req.getLocation()));
    v.setCompanyName(trimToNull(req.getCompanyName()));
    v.setEmploymentType(req.getEmploymentType());
    v.setStatus(req.getStatus() != null ? req.getStatus() : VacancyStatus.OPEN);
    v.setOwnerId(ownerId);
    return v;
  }

  public VacancyResponse toResponse(Vacancy v) {
    if (v == null) return null;

    VacancyResponse dto = new VacancyResponse();
    dto.setId(v.getId());
    dto.setTitle(nullSafe(v.getTitle()));
    dto.setDescription(nullSafe(v.getDescription()));
    dto.setLocation(nullSafe(v.getLocation()));
    dto.setCompanyName(nullSafe(v.getCompanyName()));
    dto.setEmploymentType(v.getEmploymentType());
    dto.setStatus(v.getStatus());
    dto.setOwnerId(v.getOwnerId());
    dto.setCreatedAt(v.getCreatedAt());
    dto.setUpdatedAt(v.getUpdatedAt());
    dto.setVersion(v.getVersion());
    return dto;
  }

  public void overwriteEntity(Vacancy target, VacancyRequest req) {
    Objects.requireNonNull(target, "target entity must not be null");
    Objects.requireNonNull(req, "VacancyRequest must not be null");

    target.setTitle(trimToNull(req.getTitle()));
    target.setDescription(req.getDescription());
    target.setLocation(trimToNull(req.getLocation()));
    target.setCompanyName(trimToNull(req.getCompanyName()));
    target.setEmploymentType(req.getEmploymentType());

    if (req.getStatus() != null) {
      target.setStatus(req.getStatus());
    }
  }

  /* ===================== enum mapping ===================== */

//  private EmploymentTypeDto toDto(Vacancy.EmploymentType e) {
//    return e == null ? null : EmploymentTypeDto.valueOf(e.name());
//  }
//
//  private Vacancy.EmploymentType toEntity(EmploymentTypeDto e) {
//    return e == null ? null : Vacancy.EmploymentType.valueOf(e.name());
//  }
//
//  private VacancyStatusDto toDto(Vacancy.Status s) {
//    return s == null ? null : VacancyStatusDto.valueOf(s.name());
//  }
//
//  private Vacancy.Status toEntity(VacancyStatusDto s) {
//    return s == null ? null : Vacancy.Status.valueOf(s.name());
//  }

  /* ===================== helpers ===================== */

  private String nullSafe(String s) { return s == null ? "" : s; }
  private static String trimToNull(String s) {
    if (s == null) return null;
    String t = s.trim();
    return t.isEmpty() ? null : t;
  }
}