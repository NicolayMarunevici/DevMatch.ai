package com.devmatch.vacancy.dto;

import java.time.Instant;

public class VacancyResponse {
  private Long id;
  private String title;
  private String description;
  private String location;
  private String companyName;
  private EmploymentType employmentType;
  private VacancyStatus status;

  /** id владельца вакансии (бывш. recruiterId) */
  private Long ownerId;

  private Instant createdAt;
  private Instant updatedAt;
  private Long version;

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public String getTitle() { return title; }
  public void setTitle(String title) { this.title = title; }

  public String getDescription() { return description; }
  public void setDescription(String description) { this.description = description; }

  public String getLocation() { return location; }
  public void setLocation(String location) { this.location = location; }

  public String getCompanyName() { return companyName; }
  public void setCompanyName(String companyName) { this.companyName = companyName; }

  public EmploymentType getEmploymentType() { return employmentType; }
  public void setEmploymentType(EmploymentType employmentType) { this.employmentType = employmentType; }

  public VacancyStatus getStatus() { return status; }
  public void setStatus(VacancyStatus status) { this.status = status; }

  public Long getOwnerId() { return ownerId; }
  public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }

  public Instant getCreatedAt() { return createdAt; }
  public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

  public Instant getUpdatedAt() { return updatedAt; }
  public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

  public Long getVersion() { return version; }
  public void setVersion(Long version) { this.version = version; }
}