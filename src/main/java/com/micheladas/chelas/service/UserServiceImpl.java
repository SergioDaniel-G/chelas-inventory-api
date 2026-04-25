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
	 * Locates the user based on the email (username) for Spring Security authentication.
	 * Maps the database UserAccount entity to the Security UserDetails object.
	 * * @param username The email provided during login.
	 * return UserDetails containing credentials and authorities.
	 * throws UsernameNotFoundException if the email is not registered.
	 */

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserAccount userAccount = userRepository.findByEmail(username);
		if (userAccount == null) {
			throw new UsernameNotFoundException("Usuario o password inválidos");
		}
		return new User(userAccount.getEmail(), userAccount.getPassword(), true,                       // enabled: ¿Está habilitada?
				true,
				true,
				userAccount.isAccountNonLocked(),
				mapearAutoridadesRoles(userAccount.getRoles()));
	}

	private Collection<? extends GrantedAuthority> mapearAutoridadesRoles(Collection<Role> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}

	/**
	 * Creates and persists a new UserAccount with a default "ROLE_USER" authority.
	 * Encrypts the password using BCrypt before saving.
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
	 * Switches the account's lock status between locked and unlocked.
	 * Resets failed attempts to 0 if the account is being unlocked.
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
	 * Records the exact date and time of the user's last successful login.
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
