package com.devmatch.vacancy.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class VacancyRequest {

  @NotBlank
  @Size(min = 3, max = 200)
  private String title;

  @NotBlank
  @Size(min = 10)
  private String description;

  @Size(max = 120)
  private String location;

  @Size(max = 120)
  private String companyName;

  @NotNull
  private EmploymentType employmentType; // FULL_TIME, PART_TIME, ...

  private VacancyStatus status;

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
}