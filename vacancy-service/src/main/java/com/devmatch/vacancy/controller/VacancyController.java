package com.devmatch.vacancy.controller;

import com.devmatch.vacancy.dto.VacancyRequest;
import com.devmatch.vacancy.dto.VacancyResponse;
import com.devmatch.vacancy.mapper.VacancyMapper;
import com.devmatch.vacancy.model.Vacancy;
import com.devmatch.vacancy.security.JwtTokenProvider;
import com.devmatch.vacancy.service.VacancyService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/vacancies")
public class VacancyController {

  private final VacancyService vacancyService;
  private final JwtTokenProvider jwtTokenProvider;
  private final VacancyMapper mapper;


  public VacancyController(VacancyService vacancyService, JwtTokenProvider jwtTokenProvider,
                           VacancyMapper mapper) {
    this.vacancyService = vacancyService;
    this.jwtTokenProvider = jwtTokenProvider;
    this.mapper = mapper;
  }

  @GetMapping
  public List<VacancyResponse> getAll() {
    return vacancyService.findAll().stream()
        .map(mapper::toResponse)
        .toList();
  }

  @GetMapping("/{id}")
  public VacancyResponse getById(@PathVariable UUID id) {
    Vacancy vacancy = vacancyService.findById(id)
        .orElseThrow(() -> new RuntimeException("Vacancy not found"));
    return mapper.toResponse(vacancy);
  }

  @PostMapping
  public VacancyResponse create(@RequestBody @Valid VacancyRequest request, HttpServletRequest httpServletRequest) {
    String token = jwtTokenProvider.resolveToken(httpServletRequest);
    Long ownerId = jwtTokenProvider.getUserId(token);
    Vacancy vacancy = mapper.toNewEntity(request, ownerId);
    vacancy.setOwnerId(ownerId);
    return mapper.toResponse(vacancyService.save(vacancy));
  }

  @PutMapping("/{id}")
  public VacancyResponse update(@PathVariable UUID id,
                                @RequestBody @Valid VacancyRequest request,
                                Authentication auth
//      , HttpServletRequest httpServletRequest
  ) {
//    String token = jwtTokenProvider.resolveToken(httpServletRequest);
//    String currentUserId = jwtTokenProvider.getUserId(token).toString();
    Vacancy vacancy = vacancyService.findById(id)
        .orElseThrow(() -> new RuntimeException("Vacancy not found"));

    if (!vacancy.getOwnerId().toString().equals(auth.getName())) {
      throw new SecurityException("Access denied");
    }

//    if(!vacancy.getRecruiterId().equals(currentUserId)){
//      throw new AccessDeniedException("You are not allowed to edit this vacancy");
//    }

    vacancy.setTitle(request.getTitle());
    vacancy.setDescription(request.getDescription());
    vacancy.setLocation(request.getLocation());
    vacancy.setCompanyName(request.getCompanyName());
    vacancy.setEmploymentType(request.getEmploymentType());

    return mapper.toResponse(vacancyService.save(vacancy));
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable UUID id, Authentication auth) {
    Vacancy existing = vacancyService.findById(id)
        .orElseThrow(() -> new RuntimeException("Vacancy not found"));

    if (!existing.getOwnerId().toString().equals(auth.getName())) {
      throw new SecurityException("Access denied");
    }

    vacancyService.delete(id);
  }

  @GetMapping("/mine")
  public Page<VacancyResponse> getAllMyVacancies(Pageable pageable, HttpServletRequest request){
    String token = jwtTokenProvider.resolveToken(request);
    Long ownerId = jwtTokenProvider.getUserId(token);
    return vacancyService.listMine(ownerId, pageable);
  }
}