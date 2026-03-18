package com.micheladas.chelas.entidad;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "gastos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Gasto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotEmpty(message = "Debe ingresar el articulo")
	private String articulo;

	@NotNull(message = "Debe ingresar la cantidad")
	@Positive(message = "La cantidad debe ser mayor a 0")
	private Integer cantidad;

	@NotNull(message = "Debe ingresar el precio")
	@Positive(message = "El precio debe ser mayor a 0")
	private Double precio;

	private Double total;

	private LocalDateTime fechaRegistro;

	@PrePersist
	public void asignarFechaRegistro() {

		fechaRegistro = LocalDateTime.now();
	}

}
