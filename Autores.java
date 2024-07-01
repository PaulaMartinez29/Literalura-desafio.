package com.alura.literalura.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;

import java.util.List;

@Entity
@Table (name = "autores")


    public class Autores {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        @Column(unique = true)
        private String nombre;
        private String fecha_nacimiento;
        private String fecha_muerte;
        @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
        private List<Libro> libro;



        public Autores(DatosAutor DatosAutor){
            this.nombre=DatosAutor.nombre();
            this.fecha_nacimiento = DatosAutor.fechaDeNacimiento();
            this.fecha_muerte = DatosAutor.fechaFallecimiento();
        }

        @Override
        public String toString() {
            StringBuilder librosStr = new StringBuilder();
            librosStr.append("Libros: ");

            for(int i = 0; i < libro.size() ; i++) {
                librosStr.append(libro.get(i).getTitulo());
                if (i < libro.size() - 1 ){
                    librosStr.append(", ");
                }
            }
            return String.format("********** Autor **********%nNombre:" +
                    " %s%n%s%nFecha de Nacimiento: %s%nFecha de Muerte:" +
                    " %s%n***************************%n",nombre,librosStr.toString(),fecha_nacimiento,fecha_muerte);
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

        public String getFecha_nacimiento() {
            return fecha_nacimiento;
        }

        public void setFecha_nacimiento(String fecha_nacimiento) {
            this.fecha_nacimiento = fecha_nacimiento;
        }

        public String getFecha_muerte() {
            return fecha_muerte;
        }

        public void setFecha_muerte(String fecha_muerte) {
            this.fecha_muerte = fecha_muerte;
        }

        public List<Libro> getLibro() {
            return libro;
        }

        public void setLibro(List<Libro> libro) {
            this.libro = libro;
        }
    }


}
