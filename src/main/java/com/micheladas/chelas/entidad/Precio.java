package com.micheladas.chelas.entidad;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull; // Importante para números
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "precios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Precio {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotEmpty(message = "Debe ingresar el id del producto")
	private String id_producto;

	@NotEmpty(message = "Debe ingresar la descripcion")
	private String descripcion;

	@NotNull(message = "Debe ingresar la existencia") // NotNull para números
	private Integer existencia;

	@NotNull(message = "Debe ingresar el precio") // NotNull para números
	private Double precio_producto;

	private LocalDateTime fechaRegistro;

	@PrePersist
	public void asignarFechaRegistro() {
		fechaRegistro = LocalDateTime.now();
	}
}