
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
@Table(name = "clientes")
public class Cliente {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Debe ingresar el numero del cliente")
	private String num_cliente;

	@NotBlank(message = "Debe ingresar el nombre completo del cliente")
	private String nombre_completo;

	@NotBlank(message = "Debe ingresar la direccion del cliente")
	private String direccion;

	@NotBlank(message = "Debe ingresar el codigo postal del cliente")
	private String cp;

	private LocalDateTime fechaRegistro;

	
	public Cliente(@NotBlank(message = "Debe ingresar el numero del cliente") String num_cliente,
			@NotBlank(message = "Debe ingresar el nombre completo del cliente") String nombre_completo,
			@NotBlank(message = "Debe ingresar la direccion del cliente") String direccion,
			@NotBlank(message = "Debe ingresar el codigo postal del cliente") String cp, LocalDateTime fechaRegistro) {
		super();
		this.num_cliente = num_cliente;
		this.nombre_completo = nombre_completo;
		this.direccion = direccion;
		this.cp = cp;
		this.fechaRegistro = fechaRegistro;
	}

	public Cliente() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNum_cliente() {
		return num_cliente;
	}

	public void setNum_cliente(String num_cliente) {
		this.num_cliente = num_cliente;
	}

	public String getNombre_completo() {
		return nombre_completo;
	}

	public void setNombre_completo(String nombre_completo) {
		this.nombre_completo = nombre_completo;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getCp() {
		return cp;
	}

	public void setCp(String cp) {
		this.cp = cp;
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
