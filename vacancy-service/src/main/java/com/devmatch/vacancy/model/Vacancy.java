package com.devmatch.vacancy.model;

import com.devmatch.vacancy.dto.EmploymentType;
import com.devmatch.vacancy.dto.VacancyStatus;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "vacancies")
public class Vacancy {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vacancy_seq_gen")
  @SequenceGenerator(name = "vacancy_seq_gen", sequenceName = "vacancy_seq", allocationSize = 50)
  private Long id;

  @Column(name = "title", nullable = false, length = 200)
  private String title;

  @Lob
  @Column(name = "description", columnDefinition = "text")
  private String description;

  @Column(name = "location", length = 120)
  private String location;

  @Column(name = "company_name", length = 120)
  private String companyName;

  @Enumerated(EnumType.STRING)
  @Column(name = "employment_type", nullable = false, length = 20)
  private EmploymentType employmentType; // FULL_TIME, PART_TIME, etc.


  @Column(name = "owner_id", nullable = false)
  private Long ownerId;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 16)
  private VacancyStatus vacancyStatus = VacancyStatus.DRAFT;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

  @Version
  @Column(name = "version", nullable = false)
  private Long version;

  @PrePersist
  void prePersist(){
    var now = Instant.now();
    this.createdAt = now;
    this.updatedAt = now;
  }

  @PreUpdate
  void preUpdate(){
    this.updatedAt = Instant.now();
  }


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getCompanyName() {
    return companyName;
  }

  public void setCompanyName(String companyName) {
    this.companyName = companyName;
  }

  public EmploymentType getEmploymentType() {
    return employmentType;
  }

  public void setEmploymentType(EmploymentType employmentType) {
    this.employmentType = employmentType;
  }

  public Long getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(Long ownerId) {
    this.ownerId = ownerId;
  }

  public VacancyStatus getStatus() {
    return vacancyStatus;
  }

  public void setStatus(VacancyStatus vacancyStatus) {
    this.vacancyStatus = vacancyStatus;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Instant updatedAt) {
    this.updatedAt = updatedAt;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }
}