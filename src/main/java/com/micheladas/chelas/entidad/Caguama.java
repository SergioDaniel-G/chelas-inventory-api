package com.micheladas.chelas.entidad;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.persistence.GenerationType;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "caguamas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Caguama {

	// primary key//
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotEmpty (message = "Debe ingresar la marca")
	private String marca;

	@NotNull(message = "Debe ingresar el precio")
	@Positive(message = "El precio debe ser mayor a 0")
	private Double precio;

	@NotNull(message = "Debe ingresar la cantidad")
	@Positive(message = "La cantidad debe ser mayor a 0")
	private Integer cantidad;

	private Double total;

	private LocalDateTime fechaRegistro;

	// Se asigna la fecha automáticamente antes de persistir
	@PrePersist
	public void asignarFechaRegistro() {

		fechaRegistro = LocalDateTime.now();
	}

}
