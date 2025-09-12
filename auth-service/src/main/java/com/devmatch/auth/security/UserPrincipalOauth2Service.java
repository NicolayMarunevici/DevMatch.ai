package com.devmatch.auth.security;

import com.devmatch.auth.dto.RoleEnum;
import com.devmatch.auth.dto.UserPrincipal;
import com.devmatch.auth.model.Provider;
import com.devmatch.auth.model.Role;
import com.devmatch.auth.model.User;
import com.devmatch.auth.repository.UserRepository;
import java.util.Optional;
import java.util.Set;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class UserPrincipalOauth2Service extends DefaultOAuth2UserService {

  private final UserRepository userRepository;

  public UserPrincipalOauth2Service(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2User oAuth2User = super.loadUser(userRequest);

    String email = oAuth2User.getAttribute("email");
    String name = oAuth2User.getAttribute("name");

    Optional<User> optionalUser = userRepository.findByEmail(email);
    User user;

    if (optionalUser.isPresent()) {
      user = optionalUser.get();
    } else {
      user = new User();
      user.setEmail(email);
      user.setFirstName(name);
      user.setProvider(Provider.GOOGLE);
      user.setRoles(Set.of(new Role(RoleEnum.ROLE_CANDIDATE)));
      user.setEnabled(true);
      user.setLocked(false);
    }

    return new UserPrincipal(user, oAuth2User.getAttributes());
  }
}
