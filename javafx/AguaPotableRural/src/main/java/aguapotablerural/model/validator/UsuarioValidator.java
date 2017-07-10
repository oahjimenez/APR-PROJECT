/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.aguapotablerural.model.validator;

import main.java.aguapotablerural.model.Usuario;

/**
 *
 * @author cmardones
 */
public class UsuarioValidator {
    
    public static final int RUT_MAXCHAR = 12;
    public static final int RUT_MINCHAR = 2;
    public static final int NOMBRES_MAXCHAR = 40;
    public static final int APELLIDOS_MAXCHAR = 40;
    public static final int DIRECCION_MAXCHAR = 50;
    public static final int TELEFONO_MAXCHAR = 8;
    
    public static boolean isValidRut(String rut) {
        return (rut!=null) && (rut.matches("[0-9]*[kK]?")) && (rut.length() >= RUT_MINCHAR) && (rut.length()<= RUT_MAXCHAR );
    }
    
    public static boolean isValidNombres(String nombres) {
        return nombres!=null && !nombres.isEmpty() && nombres.length() <= NOMBRES_MAXCHAR;
    }
    
    public static boolean isValidApellidos(String apellidos) {
        return apellidos!=null && !apellidos.isEmpty() && apellidos.length() <= APELLIDOS_MAXCHAR;

    }
    
    public static boolean isValidDireccion(String direccion) {
        return direccion!=null && !direccion.isEmpty() && direccion.length() <= DIRECCION_MAXCHAR;

    }
    public static boolean isValidTelefono(String telefono) {
        return telefono!=null && !telefono.isEmpty() && telefono.length() == TELEFONO_MAXCHAR && telefono.matches("[0-9]+");

    }
    
    public static boolean isValid(Usuario usuario) {
        return usuario!=null && isValidRut(usuario.getRut()) && isValidNombres(usuario.getNombres()) && isValidApellidos(usuario.getApellidos()) && isValidDireccion(usuario.getDireccion()) && isValidTelefono(usuario.getTelefono());
    }
    
}
