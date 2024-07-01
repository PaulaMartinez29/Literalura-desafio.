package com.alura.literalura.principal;

import com.alura.literalura.model.*;
import com.alura.literalura.repositorio.AutoresRepositorio;
import com.alura.literalura.repositorio.LibrosRepositorio;
import com.alura.literalura.service.ConsumoApi;
import com.alura.literalura.service.ConvierteDatos;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;


public class Principal {
    private static final String URL_BASE = "https://gutendex.com/books/";
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConvierteDatos convertir = new ConvierteDatos();
    private Scanner scanner = new Scanner(System.in);
    private final LibrosRepositorio repositorioLibros;
    private final AutoresRepositorio repositorioAutores;

    public Principal(LibrosRepositorio repositorioLibros, AutoresRepositorio repositorioAutores) {
        this.repositorioLibros = repositorioLibros;
        this.repositorioAutores = repositorioAutores;
    }

    public void menuOpciones(){
        int opcion = -1;
        while (opcion != 0) {
            String menu = """
                    1 - Buscar libro por título
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un determinado año
                    5 - Listar libros por idiomas
                    0 - Salir
                    """;
            System.out.println(menu);
            while (!scanner.hasNextInt()) {
                System.out.println("No es valido este número de opción, ingrese uno de los numeros mostrados en el menú");
                scanner.nextLine();
            }
            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1 -> buscarLibroTitulo();
                case 2 -> buscarLibroRegistro();
                case 3 -> listarAutoresRegistrados();
                case 4 -> listarAutoresVivos();
                case 5 -> listarLibrosIdiomas();
                case 0 -> System.out.println("Cerrando la aplicación...");
                default -> System.out.println("Opción inválida");
            }
        }


    }


    public void mostrarElMenu(){
        var json = consumoApi.obtenerDatos(URL_BASE );
        System.out.println(json);
        var datos = convertir.obtenerDatos(json, Datos.class);
        System.out.println(datos);
        //Busqueda por titulo
        System.out.println("Ingrese el nombre del libro que desee buscar");
        var tituloLibro  = scanner.nextLine();
        json = consumoApi.obtenerDatos(URL_BASE + "?search=" + tituloLibro.replace(" ","+"));
        var datosBusqueda = convertir.obtenerDatos(json, Datos.class);
        Optional <DatosLibros> libroBuscado = datosBusqueda.resultados().stream()
                .filter(l -> l.titulo().toUpperCase().contains(tituloLibro.toUpperCase()))
                .findFirst();

        if(libroBuscado.isPresent()){
            System.out.println("Libro encontrado");
            System.out.println(libroBuscado.get());
        }else{
                System.out.println("Libro no encontrado");
            }
        }
    private Datos getDatosLibro() {
        System.out.println("Escribe el nombre del libro que deseas buscar");
        String nombreLibro = scanner.nextLine();
        String json = consumoApi.obtenerDatos(URL_BASE + nombreLibro.replace(" ", "+"));
        return convertir.obtenerDatos(json, Datos.class);
    }

    private void buscarLibroTitulo() {
        Datos datosBusqueda = getDatosLibro();
        if (datosBusqueda == null || datosBusqueda.resultados().isEmpty()) {
            System.out.println("Libro no encontrado");
            return;
        }

        DatosLibros primerLibro = datosBusqueda.resultados().getFirst();
        Libro libro = new Libro(primerLibro);
        System.out.println("---- Libro ----");
        System.out.println(libro);
        System.out.println("---------------");

        Optional<Libro> libroExistenteOptional = repositorioLibros.findByTitulo(libro.getTitulo());
        if (libroExistenteOptional.isPresent()) {
            System.out.println("\nEl libro ya está registrado\n");
            return;
        }

        if (primerLibro.autor().isEmpty()) {
            System.out.println("Sin autor");
            return;
        }

        DatosAutor datosAutor = primerLibro.autor().getFirst();
        Autores autor = new Autores(datosAutor);
        Optional<Autores> autorOptional = repositorioAutores.findByNombre(autor.getNombre());

        Autores autorExistente = autorOptional.orElseGet(() -> repositorioAutores.save(autor));
        libro.setAutores(autorExistente);
        repositorioLibros.save(libro);

        System.out.printf("""
                ---------- Libro ----------
                Título: %s
                Autor: %s
                Idioma: %s
                Número de Descargas: %d
                ---------------------------
                """, libro.getTitulo(), autor.getNombre(), libro.getLenguaje(), libro.getNumeroDescargas());
    }

    private void buscarLibroRegistro() {
        List<Libro> libros = repositorioLibros.findAll();

        if (libros.isEmpty()) {
            System.out.println("No se encontraron libros registrados.");
            return;
        }

        System.out.println("----- Libros Registrados -----");
        libros.forEach(System.out::println);
        System.out.println("-------------------------------");
    }

    private void listarAutoresRegistrados() {
        List<Autores> autores = repositorioAutores.findAll();

        if (autores.isEmpty()) {
            System.out.println("No se encontraron autores registrados.");
            return;
        }

        System.out.println("----- Autores Registrados -----");
        autores.forEach(System.out::println);
        System.out.println("--------------------------------");
    }

    private void listarAutoresVivos() {
        System.out.println("Introduce el año para listar los autores vivos:");
        while (!scanner.hasNextInt()) {
            System.out.println("Formato inválido, ingrese un número válido para el año");
            scanner.nextLine();
        }
        int anio = scanner.nextInt();
        scanner.nextLine();

        List<Autores> autores = repositorioAutores.findAutoresVivosEnAnio(anio);

        if (autores.isEmpty()) {
            System.out.println("No se encontraron autores vivos en el año " + anio);
        } else {
            System.out.println("----- Autores Vivos en el Año " + anio + " -----");
            autores.forEach(System.out::println);
            System.out.println("---------------------------------------------");
        }
    }

    private void listarLibrosIdiomas() {
        System.out.println("Selecciona el lenguaje/idioma que deseas buscar: ");
        while (true) {
            String opciones = """
                    1. en - Inglés
                    2. es - Español
                    3. fr - Francés
                    4. pt - Portugués
                    0. Volver a las opciones anteriores
                    """;
            System.out.println(opciones);
            while (!scanner.hasNextInt()) {
                System.out.println("Formato inválido, ingrese un número que esté disponible en el menú");
                scanner.nextLine();
            }
            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1 -> mostrarLibrosPorIdioma(Idiomas.en);
                case 2 -> mostrarLibrosPorIdioma(Idiomas.es);
                case 3 -> mostrarLibrosPorIdioma(Idiomas.fr);
                case 4 -> mostrarLibrosPorIdioma(Idiomas.pt);
                case 0 -> {
                    return;
                }
                default -> System.out.println("Opción inválida");
            }
        }
    }

    private void mostrarLibrosPorIdioma(Idiomas idioma) {
        List<Libro> librosPorIdioma = repositorioLibros.findByLenguaje(idioma);
        if (librosPorIdioma.isEmpty()) {
            System.out.println("No se encontraron libros en " + idioma.getIdiomaEspanol());
        } else {
            System.out.printf("----- Libros en %s ----- %n", idioma.getIdiomaEspanol());
            librosPorIdioma.forEach(System.out::println);
            System.out.println("-----------------------------");
        }
    }

    }

