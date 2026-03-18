package com.micheladas.chelas.controlador.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRegistroDto {

	@NotBlank(message = "El nombre es obligatorio")
	private String nombre;

	@NotBlank(message = "El apellido es obligatorio")
	private String apellido;

	@NotBlank(message = "Correo electrónico requerido")
	@Email(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "Ingrese una dirección de correo válido")
	private String email;

	@NotBlank(message = "La contraseña es obligatoria")
	private String password;

	@NotBlank(message = "Número telefónico obligatorio")
	@Pattern(regexp = "^\\d{10}$", message = "El teléfono debe contener 10 números")
	private String mobileNumber;

	public void setMobileNumber(String mobileNumber) {

		if (mobileNumber != null) {
			// Esto quita todos los espacios en blanco automáticamente
			this.mobileNumber = mobileNumber.replaceAll("\\s+", "");
		} else {
			this.mobileNumber = mobileNumber;
		}
	}
}
