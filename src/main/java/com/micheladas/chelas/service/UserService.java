package com.micheladas.chelas.service;

import java.util.List;
import org.springframework.security.core.userdetails.UserDetailsService;
import com.micheladas.chelas.controller.DTO.UserRegistrationDto;
import com.micheladas.chelas.entity.UserAccount;

public interface UserService extends UserDetailsService {

	UserAccount save(UserRegistrationDto userRegistrationDto);

	List<UserAccount> UserLists();

	UserAccount findByEmail(String email);

	void toggleLockStatus(Long id);

}
