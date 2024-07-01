package com.alura.literalura.repositorio;
import com.alura.literalura.model.Autores;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AutoresRepositorio extends JpaRepository<Autores, Long> {
    Optional<Autores> findByNombre(String nombre);

    @Query("SELECT a FROM Autor a WHERE a.fechaNacimiento <= :year AND (a.fechaDeceso IS NULL OR a.fechaDeceso > :year)")
    List<Autores> findAutoresVivosEnAnio(int year);
}




