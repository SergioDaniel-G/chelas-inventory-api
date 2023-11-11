package com.micheladas.chelas.entidad;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "prestados")
public class Prestado {

	// primary key//
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Debe ingresar el nombre")
	private String nombre;

	@NotBlank(message = "Debe ingresar la cantidad")
	private String cantidad;

	@NotBlank(message = "Debe ingresar el total")
	private String total;

	@NotBlank(message = "Debe ingresar deuda del cliente")
	private String descripcionDelPedido;

	public Prestado(Long id, @NotBlank(message = "Debe ingresar el nombre") String nombre,
			@NotBlank(message = "Debe ingresar la cantidad") String cantidad,
			@NotBlank(message = "Debe ingresar el total") String total,
			@NotBlank(message = "Debe ingresar deuda del cliente") String descripcionDelPedido) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.cantidad = cantidad;
		this.total = total;
		this.descripcionDelPedido = descripcionDelPedido;

	}

	public Prestado() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
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

	public String getDescripcionDelPedido() {
		return descripcionDelPedido;
	}

	public void setDescripcionDelPedido(String descripcionDelPedido) {
		this.descripcionDelPedido = descripcionDelPedido;
	}

}
