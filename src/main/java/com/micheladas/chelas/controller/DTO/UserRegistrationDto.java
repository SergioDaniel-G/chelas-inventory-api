package com.micheladas.chelas.controller.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for user registration, encapsulating validation logic
 * and data normalization before persisting to the database.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationDto {

	@NotBlank(message = "{user.name.required}")
	private String name;

	@NotBlank(message = "{user.lastname.required}")
	private String surname;

	@NotBlank(message = "{user.email.required}")
	@Email(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "{validation.email.invalid}")
	private String email;

	@NotBlank(message = "{user.password.required}")
	private String password;

	@NotBlank(message = "{user.phone.required}")
	@Pattern(regexp = "^\\d{10}$", message = "{validation.phone.invalid}")
	private String mobileNumber;

	/**
	 * Normalizes the phone number by removing non-digit characters.
	 * This ensures consistency before the validation pattern is applied.
	 */
	public void setMobileNumber(String mobileNumber) {
		if (mobileNumber != null) {
			this.mobileNumber = mobileNumber.replaceAll("\\D", "");
		} else {
			this.mobileNumber = null;
		}
	}
}