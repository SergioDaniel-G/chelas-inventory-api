package com.micheladas.chelas.entidad;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "proveedores")
public class Proveedor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotEmpty(message = "Debe ingresar el nombre")
	private String nombre;

	@NotBlank(message = "Debe ingresar el telefono")
	private String telefono;

	@NotBlank(message = "Debe ingresar la ubicacion")
	private String ubicacion;

	public Proveedor(Long id, @NotEmpty(message = "Debe ingresar el nombre") String nombre,
			@NotBlank(message = "Debe ingresar el telefono") String telefono,
			@NotBlank(message = "Debe ingresar la ubicacion") String ubicacion) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.telefono = telefono;
		this.ubicacion = ubicacion;
	}

	public Proveedor() {
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

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}

}
