package com.alura.literalura.repositorio;

import com.alura.literalura.model.Idiomas;
import com.alura.literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LibrosRepositorio extends JpaRepository<Libro, Long> {


    Optional<Libro> findByTitulo(String titulo);

    List<Libro> findByLenguaje(Idiomas lenguaje);
}








