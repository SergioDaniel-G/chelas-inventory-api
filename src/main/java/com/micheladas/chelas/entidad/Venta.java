package com.micheladas.chelas.entidad;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "ventas")
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

	public Venta(@NotBlank(message = "Debe ingresar los vasos") String vasos,
			@NotBlank(message = "Debe ingresar el stock de caguamas") String stock_caguamas,
			@NotBlank(message = "Debe ingresar el stock de cigarros") String stock_cigarros,
			@NotBlank(message = "Debe ingresar los cigarros") String cigarros,
			@NotBlank(message = "Debe ingresar las caguamas") String caguamas,
			@NotBlank(message = "Debe ingresar el total") String total, LocalDateTime fechaRegistro) {
		super();
		this.vasos = vasos;
		this.cigarros = cigarros;
		this.caguamas = caguamas;
		this.stock_caguamas = stock_caguamas;
		this.caguamas = caguamas;
		this.stock_cigarros = stock_cigarros;
		this.fechaRegistro = fechaRegistro;
	}

	public Venta() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getVasos() {
		return vasos;
	}

	public void setVasos(String vasos) {
		this.vasos = vasos;
	}

	public String getCigarros() {
		return cigarros;
	}

	public void setCigarros(String cigarros) {
		this.cigarros = cigarros;
	}

	public String getCaguamas() {
		return caguamas;
	}

	public void setCaguamas(String caguamas) {
		this.caguamas = caguamas;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getStock_caguamas() {
		return stock_caguamas;
	}

	public void setStock_caguamas(String stock_caguamas) {
		this.stock_caguamas = stock_caguamas;
	}

	public String getStock_cigarros() {
		return stock_cigarros;
	}

	public void setStock_cigarros(String stock_cigarros) {
		this.stock_cigarros = stock_cigarros;
	}

	public LocalDateTime getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(LocalDateTime fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	@PrePersist
	public void asignarFechaRegistro() {
		fechaRegistro = LocalDateTime.now();
	}

}
