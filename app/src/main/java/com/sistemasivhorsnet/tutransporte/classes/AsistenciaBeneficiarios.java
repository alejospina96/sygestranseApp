package com.sistemasivhorsnet.tutransporte.classes;

import com.sistemasivhorsnet.tutransporte.entities.Beneficiario;

import java.util.ArrayList;

/**
 * Created by Daniel on 26/05/2017.
 */

public class AsistenciaBeneficiarios {

    private ArrayList<Beneficiario> beneficiarios;
    public AsistenciaBeneficiarios() {
        beneficiarios = new ArrayList<>();
    }
    public void add(Beneficiario beneficiario){
        beneficiarios.add(beneficiario);
    }
    public void remove(Beneficiario beneficiario){
        beneficiarios.remove(beneficiario);
    }
    public boolean alreadyAdded(Beneficiario nombre){
        return beneficiarios.contains(nombre);
    }
}
