package com.devmatch.vacancy.service;

import com.devmatch.vacancy.dto.VacancyResponse;
import com.devmatch.vacancy.mapper.VacancyMapper;
import com.devmatch.vacancy.model.Vacancy;
import com.devmatch.vacancy.repository.VacancyRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class VacancyService {
  private final VacancyRepository vacancyRepository;
  private final VacancyMapper mapper;

  public VacancyService(VacancyRepository vacancyRepository, VacancyMapper mapper) {
    this.vacancyRepository = vacancyRepository;
    this.mapper = mapper;
  }

  public List<Vacancy> findAll() {
    return vacancyRepository.findAll();
  }

  public Optional<Vacancy> findById(UUID id) {
    return vacancyRepository.findById(id);
  }

  public Vacancy save(Vacancy vacancy) {
    return vacancyRepository.save(vacancy);
  }

  public void delete(UUID id) {
    vacancyRepository.deleteById(id);
  }

  public Page<VacancyResponse> listMine(Long ownerId, Pageable pageable){
    return vacancyRepository.findByOwnerId(ownerId, pageable).map(mapper::toResponse);
  }
}
