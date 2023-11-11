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
@Table(name = "precios")
public class Precio {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Debe ingresar el id del producto")
	private String id_producto;

	@NotBlank(message = "Debe ingresar la descripcion")
	private String descripcion;

	@NotBlank(message = "Debe ingresar la existencia")
	private String existencia;

	@NotBlank(message = "Debe ingresar el precio")
	private String precio_producto;

	private LocalDateTime fechaRegistro;

	public Precio(@NotBlank(message = "Debe ingresar el id del producto") String id_producto,
			@NotBlank(message = "Debe ingresar la descripcion") String descripcion,
			@NotBlank(message = "Debe ingresar la existencia") String existencia,
			@NotBlank(message = "Debe ingresar el precio") String precio_producto, LocalDateTime fechaRegistro) {
		super();
		this.id_producto = id_producto;
		this.descripcion = descripcion;
		this.existencia = existencia;
		this.precio_producto = precio_producto;
		this.fechaRegistro = fechaRegistro;
	}

	public Precio() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getId_producto() {
		return id_producto;
	}

	public void setId_producto(String id_producto) {
		this.id_producto = id_producto;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getExistencia() {
		return existencia;
	}

	public void setExistencia(String existencia) {
		this.existencia = existencia;
	}

	public String getPrecio_producto() {
		return precio_producto;
	}

	public void setPrecio_producto(String precio_producto) {
		this.precio_producto = precio_producto;
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
