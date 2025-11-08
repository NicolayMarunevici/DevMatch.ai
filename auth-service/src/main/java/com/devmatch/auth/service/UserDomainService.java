package com.devmatch.auth.service;

import com.devmatch.auth.dto.RegisterRequest;
import com.devmatch.auth.dto.RoleEnum;
import com.devmatch.auth.model.Provider;
import com.devmatch.auth.model.Role;
import com.devmatch.auth.model.User;
import com.devmatch.auth.repository.RoleRepository;
import com.devmatch.auth.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.Set;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class UserDomainService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;

  public UserDomainService(UserRepository userRepository, RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Transactional
  public User createUser(RegisterRequest request) {
    if (userRepository.existsByEmail(request.getEmail())) {
      throw new IllegalArgumentException("User already registered");
    }

    Set<RoleEnum> roleEnums =
        (request.getRoles() == null || request.getRoles().isEmpty())
            ? Set.of(RoleEnum.ROLE_CANDIDATE)
            : request.getRoles();

    Set<Role> roles = roleRepository.findByNameIn(roleEnums);
    if (roles == null || roles.isEmpty()) {
      throw new IllegalArgumentException("Roles not found: " + roleEnums);
    }

    User user = new User();
    user.setEmail(request.getEmail());
    user.setFirstName(request.getFirstName());
    user.setLastName(request.getLastName());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setRoles(roles);
    user.setProvider(Provider.NONE);
    user.setEnabled(true);
    user.setLocked(false);

    return userRepository.save(user);
  }
}
