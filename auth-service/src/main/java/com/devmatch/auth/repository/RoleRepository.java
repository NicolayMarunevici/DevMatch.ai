package com.devmatch.auth.repository;

import com.devmatch.auth.dto.RoleEnum;
import com.devmatch.auth.model.Role;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
  Set<Role> findByNameIn(Set<RoleEnum> name);
}
