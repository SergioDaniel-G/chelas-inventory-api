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
@Table(name = "cigarros")
public class Cigarro {

	// primary key//
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Debe ingresar la marca")
	private String marca;

	@NotBlank(message = "Debe ingresar el precio")
	private String precio;

	@NotBlank(message = "Debe ingresar la cantidad")
	private String cantidad;

	@NotBlank(message = "Debe ingresar el total")
	private String total;

	private LocalDateTime fechaRegistro;

	public Cigarro(@NotBlank(message = "Debe ingresar la marca") String marca,
			@NotBlank(message = "Debe ingresar el precio") String precio,
			@NotBlank(message = "Debe ingresar la cantidad") String cantidad,
			@NotBlank(message = "Debe ingresar el total") String total, LocalDateTime fechaRegistro) {
		super();
		this.marca = marca;
		this.precio = precio;
		this.cantidad = cantidad;
		this.total = total;
		this.fechaRegistro = fechaRegistro;
	}

	public Cigarro() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getPrecio() {
		return precio;
	}

	public void setPrecio(String precio) {
		this.precio = precio;
	}

	public String getCantidad() {
		return cantidad;
	}

	public void setCantidad(String cantidad) {
		this.cantidad = cantidad;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
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
