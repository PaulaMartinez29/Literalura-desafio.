package com.alura.literalura.model;



import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;

@Entity
@Table(name = "libros")


    public class Libro {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long Id;
        private String titulo;
        @ManyToOne
        private Autores autores;
        @Enumerated(EnumType.STRING)
        private Idiomas lenguaje;
        private Integer numeroDescargas;


    public Libro(){}

    public Libro(DatosLibros datosLibro){
        this.titulo = datosLibro.titulo();
        this.lenguaje = Idiomas.fromString(datosLibro.idiomas().toString().split(",")[0].trim());
        this.numeroDescargas = datosLibro.numeroDescargas();
    }



    @Override
        public String toString() {
            String nombreAutores = (autores != null) ? autores.getNombre() : "Autor desconocido";
            return String.format("********** Libro **********%nTitulo:" +
                    " %s%nAutores: %s%nIdiomas:" +
                    " %d%n***************************%n",titulo,nombreAutores,lenguaje,numeroDescargas);
        }

        public Long getId() {
            return Id;
        }

        public void setId(Long id) {
            Id = id;
        }

        public String getTitulo() {
            return titulo;
        }

        public void setTitulo(String titulo) {
            this.titulo = titulo;
        }

        public Autores getAutores() {
            return autores;
        }

        public void setAutores(Autores autores) {
            this.autores = autores;
        }

        public Idiomas getLenguaje() {
            return lenguaje;
        }

        public void setLenguaje(Idiomas lenguaje) {
            this.lenguaje = lenguaje;
        }

    public Integer getNumeroDescargas() {
        return numeroDescargas;
    }

    public void setNumeroDescargas(Integer numeroDescargas) {
        this.numeroDescargas = numeroDescargas;
    }
}
