package com.micheladas.chelas.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.micheladas.chelas.entidad.Caguama;

@Repository
public interface CaguamaRepositorio extends JpaRepository<Caguama, Long> {

}
