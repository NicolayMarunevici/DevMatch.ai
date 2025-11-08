package com.devmatch.user.repository;

import com.devmatch.user.model.User;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
  List<User> findByRolesIn(Set<String> role);
  List<User> findByTechStackContainingIgnoreCase(String techStack);
}
