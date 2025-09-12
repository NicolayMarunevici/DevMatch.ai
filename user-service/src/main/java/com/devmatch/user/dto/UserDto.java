package com.devmatch.user.dto;

import com.devmatch.user.model.Role;
import com.devmatch.user.model.SeniorityLevel;
import java.util.Set;

public class UserDto {
  private Long id;
  private String email;
  private String firstName;
  private String lastName;
  private String profilePictureUrl;
  private String jobTitle;
  private Set<Role> role;
  private String techStack;
  private SeniorityLevel seniority;
  private Long resumeId;
  private Long companyId;

  // Getters & Setters
  public Long getId() { return id; }

  public void setId(Long id) { this.id = id; }

  public String getEmail() { return email; }

  public void setEmail(String email) { this.email = email; }

  public String getFirstName() { return firstName; }

  public void setFirstName(String firstName) { this.firstName = firstName; }

  public String getLastName() { return lastName; }

  public void setLastName(String lastName) { this.lastName = lastName; }

  public String getProfilePictureUrl() { return profilePictureUrl; }

  public void setProfilePictureUrl(String profilePictureUrl) { this.profilePictureUrl = profilePictureUrl; }

  public String getJobTitle() { return jobTitle; }

  public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }

  public Set<Role> getRole() { return role; }

  public void setRole(Set<Role> role) { this.role = role; }

  public String getTechStack() { return techStack; }

  public void setTechStack(String techStack) { this.techStack = techStack; }

  public SeniorityLevel getSeniority() { return seniority; }

  public void setSeniority(SeniorityLevel seniority) { this.seniority = seniority; }

  public Long getResumeId() { return resumeId; }

  public void setResumeId(Long resumeId) { this.resumeId = resumeId; }

  public Long getCompanyId() { return companyId; }

  public void setCompanyId(Long companyId) { this.companyId = companyId; }
}
