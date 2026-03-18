
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
@Table(name = "clientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
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

    @PrePersist
    public void asignarFechaRegistro() {

        fechaRegistro = LocalDateTime.now();
    }

}
