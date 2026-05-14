package com.micheladas.chelas.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.micheladas.chelas.entity.Role;
import com.micheladas.chelas.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.micheladas.chelas.controller.DTO.UserRegistrationDto;
import com.micheladas.chelas.entity.UserAccount;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;

	public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	/**
	 * LOCATES THE USER BASED ON THE EMAIL (USERNAME) FOR SPRING SECURITY AUTHENTICATION.
	 * MAPS THE DATABASE USERACCOUNT ENTITY TO THE SECURITY USERDETAILS OBJECT.
	 * * @PARAM USERNAME THE EMAIL PROVIDED DURING LOGIN.
	 * RETURN USERDETAILS CONTAINING CREDENTIALS AND AUTHORITIES.
	 * THROWS USERNAMENOTFOUNDEXCEPTION IF THE EMAIL IS NOT REGISTERED.
	 */

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// 1. Normalizamos el username (email) a minúsculas y quitamos espacios en blanco
		String normalizedEmail = (username != null) ? username.toLowerCase().trim() : "";

		UserAccount userAccount = userRepository.findByEmail(normalizedEmail);
		if (userAccount == null) {
			throw new UsernameNotFoundException("Usuario o password inválidos");
		}

		List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_PRE_VERIFIED"));

		return new User(userAccount.getEmail(), userAccount.getPassword(), true,
				true,
				true,
				userAccount.isAccountNonLocked(),
				authorities);
	}

	private Collection<? extends GrantedAuthority> mapearAutoridadesRoles(Collection<Role> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}

	/**
	 * CREATES AND PERSISTS A NEW USERACCOUNT WITH A DEFAULT "ROLE_USER" AUTHORITY.
	 * ENCRYPTS THE PASSWORD USING BCRYPT BEFORE SAVING.
	 */

	@Override
	public UserAccount save(UserRegistrationDto userRegistrationDto) {

		UserAccount userAccount = UserAccount.builder()
				.name(userRegistrationDto.getName())
				.surname(userRegistrationDto.getSurname())
				.email(userRegistrationDto.getEmail())
				.password(passwordEncoder.encode(userRegistrationDto.getPassword()))
				.mobileNumber(userRegistrationDto.getMobileNumber())
				.accountNonLocked(true)
				.failedAttempts(0)
				.roles(Arrays.asList(new Role("ROLE_USER")))
				.build();

		return userRepository.save(userAccount);
	}

	/**
	 * SWITCHES THE ACCOUNT'S LOCK STATUS BETWEEN LOCKED AND UNLOCKED.
	 * RESETS FAILED ATTEMPTS TO 0 IF THE ACCOUNT IS BEING UNLOCKED.
	 */

	@Override
	public void toggleLockStatus(Long id) {
		userRepository.findById(id).ifPresent(userAccount -> {

			boolean newStatus = !userAccount.isAccountNonLocked();
			userAccount.setAccountNonLocked(newStatus);

			if (!newStatus) {
				userAccount.setFailedAttempts(0);
			}

			userRepository.save(userAccount);
		});
	}

	@Override
	public List<UserAccount> UserLists() {

		return userRepository.findAll();
	}

	@Override
	public UserAccount findByEmail(String email) {

		return userRepository.findByEmail(email);
	}

	/**
	 * RECORDS THE EXACT DATE AND TIME OF THE USER'S LAST SUCCESSFUL LOGIN.
	 */

	@Override
	@Transactional
	public void updateLastLoginDate(String email, LocalDateTime loginDate) {

		UserAccount user = Optional.ofNullable(userRepository.findByEmail(email))
				.orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));
					user.setLastLogin(loginDate);
					userRepository.save(user);
				}
}
