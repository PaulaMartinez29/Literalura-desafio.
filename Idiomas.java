package com.alura.literalura.model;


    public enum Idiomas {
        en("[en]", "Ingles"),
        es("[es]", "Español"),
        fr("[fr]", "Frances"),
        pt("[pt]", "Portugues");

        private String idiomaGutendex;
        private String idiomaEspanol;

        Idiomas(String idiomaGutendex, String idiomaEspanol){
            this.idiomaGutendex = idiomaGutendex;
            this.idiomaEspanol = idiomaEspanol;

        }

        public static Idiomas fromString(String text){
            for (Idiomas idiomas : Idiomas.values()){
                if (idiomas.idiomaGutendex.equalsIgnoreCase(text)){
                    return idiomas;
                }
            }
            throw new IllegalArgumentException("Ningun idioma encontrado: " + text);
        }

        public static Idiomas fromEspanol(String text){
            for (Idiomas idiomas : Idiomas.values()){
                if (idiomas.idiomaEspanol.equalsIgnoreCase(text)){
                    return idiomas;
                }
            }
            throw new IllegalArgumentException("No hay ningún libro en este idioma: " + text);
        }

        public String getIdiomaGutendex() {
            return idiomaGutendex;
        }

        public String getIdiomaEspanol() {
            return idiomaEspanol;
        }

    }

