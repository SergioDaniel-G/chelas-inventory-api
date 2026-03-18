package com.micheladas.chelas.entidad;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ventas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Venta {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Debe ingresar los vasos")
	private String vasos;

	@NotBlank(message = "Debe ingresar los cigarros")
	private String cigarros;

	@NotBlank(message = "Debe ingresar las caguamas")
	private String caguamas;

	@NotBlank(message = "Debe ingresar el total")
	private String total;

	@NotBlank(message = "Debe ingresar el stock de caguamas")
	private String stock_caguamas;

	@NotBlank(message = "Debe ingresar el stock de cigarros")
	private String stock_cigarros;

	private LocalDateTime fechaRegistro;

	@PrePersist
	public void asignarFechaRegistro() {
		fechaRegistro = LocalDateTime.now();
	}

}
