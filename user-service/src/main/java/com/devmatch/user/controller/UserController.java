package com.devmatch.user.controller;

import com.devmatch.user.dto.CreateUserProfileRequest;
import com.devmatch.user.dto.UpdateProfileRequest;
import com.devmatch.user.dto.UserDto;
import com.devmatch.user.security.CurrentUser;
import com.devmatch.user.security.CurrentUserProvider;
import com.devmatch.user.service.UserService;
import java.util.List;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
  private final UserService userService;
  private final CurrentUserProvider currentUserProvider;

  public UserController(UserService userService, CurrentUserProvider currentUserProvider) {
    this.userService = userService;
    this.currentUserProvider = currentUserProvider;
  }

//  @ResponseStatus(HttpStatus.CREATED)
//  @PostMapping
//  public void createUser(@RequestBody CreateUserProfileRequest request) {
//    System.out.println("Profile incoming: " + request);
//    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//    System.out.println("Authenticated: " + auth.isAuthenticated());
//    System.out.println("Authorities: " + auth.getAuthorities());
//    userService.createUserProfile(request);
//  }

  @GetMapping("/{id}")
  public UserDto getUser(@PathVariable Long id) {
    return userService.getUserById(id);
  }

  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  @PutMapping("/{id}")
  public UserDto updateUser(@PathVariable Long id,
                            @RequestBody UserDto userDto) {
    return userService.updateUser(id, userDto);
  }

  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  @DeleteMapping("/{id}")
  public void deleteUser(@PathVariable Long id) {
    userService.deleteUser(id);
  }

  @GetMapping("/roles")
  public List<UserDto> getUsersByRole(@RequestParam Set<String> role) {
    return userService.getAllByRole(role);
  }

  @GetMapping("/search")
  public List<UserDto> searchByTech(@RequestParam("stack") String stack) {
    return userService.searchByTechStack(stack);
  }

  @GetMapping("/me")
  public UserDto getCurrentUser(){
    Long userId = currentUserProvider.getCurrentUserId();
    return userService.getUserById(userId);
  }

  @PutMapping("/me")
  public UserDto updateProfile(@AuthenticationPrincipal CurrentUser currentUser,
                               @RequestBody UpdateProfileRequest request){
    return userService.updateProfile(currentUser.getId(), request);
  }
}
