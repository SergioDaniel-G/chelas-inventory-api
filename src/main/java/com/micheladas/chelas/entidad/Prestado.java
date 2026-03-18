package com.micheladas.chelas.entidad;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "prestados")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Prestado {

	// primary key//
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotEmpty(message = "Debe ingresar el nombre")
	private String nombre;

	@NotEmpty(message = "Debe ingresar la cantidad")
	private String cantidad;

	@NotEmpty(message = "Debe ingresar el total")
	private String total;

	@NotEmpty(message = "Debe ingresar deuda del cliente")
	private String descripcionDelPedido;

	private LocalDateTime fechaRegistro;

	@PrePersist
	public void asignarFechaRegistro() {
		fechaRegistro = LocalDateTime.now();
	}

}
