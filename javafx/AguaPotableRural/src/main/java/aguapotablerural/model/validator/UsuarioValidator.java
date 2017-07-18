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
    
    private static final String digitPattern = "[0-9]+";
    private static final String rutPattern = "[0-9]*[kK]?";
    
    public static final int RUT_MAXCHAR = 12;
    public static final int RUT_MINCHAR = 2;
    public static final int NOMBRES_MAXCHAR = 40;
    public static final int APELLIDOS_MAXCHAR = 40;
    public static final int DIRECCION_MAXCHAR = 50;
    public static final int TELEFONO_MAXCHAR = 8;

    public static boolean isValidRut(String rut) {
        if (rut==null) {
            return false;
        }
        rut = rut.replace(".","").replace("-","");
        return (rut.matches(rutPattern)) && (rut.length() >= RUT_MINCHAR) && (rut.length()<= RUT_MAXCHAR ) && isValidDigitoVerificador(rut.substring(0,rut.length()-1),rut.substring(rut.length() - 1));
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
        return telefono!=null && !telefono.isEmpty() && telefono.length() == TELEFONO_MAXCHAR && telefono.matches(digitPattern);
    }
    
    public static boolean isValid(Usuario usuario) {
        return usuario!=null && isValidRut(usuario.getRut()) && isValidNombres(usuario.getNombres()) && isValidApellidos(usuario.getApellidos()) && isValidDireccion(usuario.getDireccion()) && isValidTelefono(usuario.getTelefono());
    }
    
    public static boolean isValidDigitoVerificador(String rut, String digitoVerificador) {
       return digitoVerificador.equalsIgnoreCase(String.valueOf(getDigitoVerificador(Integer.parseInt(rut))));
    }
    
    public static char getDigitoVerificador(int rut) {
        int m = 0, s = 1;
        int asciiKValue = 75;
        for (; rut != 0; rut /= 10) {
             s = (s + rut % 10 * (9 - m++ % 6)) % 11;
        }
        return ((char) (s != 0 ? s + 47 : asciiKValue));
    }
}
