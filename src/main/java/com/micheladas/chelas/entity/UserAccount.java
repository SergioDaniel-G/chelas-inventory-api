package com.micheladas.chelas.entity;

import java.time.LocalDateTime;
import java.util.Collection;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAccount {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "{user.name.required}")
	@Column(name = "name")
	private String name;

	@NotBlank(message = "{user.lastname.required}")
	@Column(name = "surname")
	private String surname;

	@NotBlank(message = "{user.email.required}")
	@Email(message = "{validation.email.invalid}")
	@Column(name = "email")
	private String email;

	@NotBlank(message = "{user.password.required}")
	private String password;

	@Column(name = "phone_number")
	@Pattern(regexp = "^\\d{10}$", message = "{validation.phone.invalid}")
	private String mobileNumber;

	@Column(name = "account_non_locked")
	private boolean accountNonLocked = true;

	@Column(name = "failed_attempts")
	private int failedAttempts = 0;

	@Column(name = "last_login")
	private LocalDateTime lastLogin;

	/**
	 * Collection of security roles granted to the user.
	 * Loaded eagerly to facilitate immediate authorization checks.
	 */

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(
			name = "users_roles",
			joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
	)
	private Collection<Role> roles;
}