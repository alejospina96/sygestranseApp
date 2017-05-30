package com.sistemasivhorsnet.tutransporte.entities;

/**
 * Created by Pedro on 25/05/2017.
 */

public class Beneficiario {
    private String primerNombre, segundoNombre, primerApellido, segundoApellido,documento;
    public Beneficiario(String documento, String primerNombre, String segundoNombre, String primerApellido, String segundoApellido) {
        this.documento = documento;
        this.primerNombre = primerNombre;
        this.segundoNombre = segundoNombre;
        this.primerApellido = primerApellido;
        this.segundoApellido = segundoApellido;
    }

    public String getDocumento() {
        return documento;
    }

    public boolean equals(String nombre) {
        return this.toString().equals(nombre);
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public String getPrimerApellido() {

        return primerApellido;
    }

    public String getSegundoNombre() {

        return segundoNombre;
    }

    public String getPrimerNombre() {
        return primerNombre;
    }

    @Override
    public String toString() {
        return primerNombre + ' ' +segundoNombre + ' ' + primerApellido + ' ' + segundoApellido;
    }
}
