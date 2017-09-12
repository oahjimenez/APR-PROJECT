/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.aguapotablerural.model.validator;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
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
    private boolean isRutMandatory = false;
    private boolean isNombresMandatory = false;
    private boolean isApellidosMandatory = false;
    private boolean isDireccionMandatory = false;
    private boolean isTelefonoMandatory = false;
    
    public UsuarioValidator() {
        
    }
    
    public void setRutMandatory(boolean isRutMandatory) {
        this.isRutMandatory = isRutMandatory;
    }
    
     public void setNombresMandatory(boolean isNombresMandatory) {
        this.isNombresMandatory = isNombresMandatory;
    }
     
    public void setApellidosMandatory(boolean isApellidosMandatory) {
        this.isApellidosMandatory = isApellidosMandatory;
    }
    
    public void setDireccionMandatory(boolean isDireccionMandatory) {
        this.isDireccionMandatory = isDireccionMandatory;
    }

    public void setTelefonoMandatory(boolean isTelefonoMandatory) {
        this.isTelefonoMandatory = isTelefonoMandatory;
    }

    public boolean isValidRut(String rut) {
        if (rut==null) {
            return false;
        }
        rut = rut.replace(".","").replace("-","");
        return ((rut.matches(rutPattern)) && (rut.length() >= RUT_MINCHAR) && (rut.length()<= RUT_MAXCHAR ) && isValidDigitoVerificador(rut.substring(0,rut.length()-1),rut.substring(rut.length() - 1)));
    }

    public boolean isValidNombres(String nombres) {
        return nombres!=null && !nombres.isEmpty() && nombres.length() <= NOMBRES_MAXCHAR;
    }
    
    public boolean isValidApellidos(String apellidos) {
        return apellidos!=null && !apellidos.isEmpty() && apellidos.length() <= APELLIDOS_MAXCHAR;

    }
    
    public boolean isValidDireccion(String direccion) {
        return direccion!=null && !direccion.isEmpty() && direccion.length() <= DIRECCION_MAXCHAR;

    }
    public boolean isValidTelefono(String telefono) {
        return telefono!=null && !telefono.isEmpty() && telefono.length() == TELEFONO_MAXCHAR && telefono.matches(digitPattern);
    }
    
    public boolean isValid(Usuario usuario) {
        return usuario!=null && isValidRut(usuario.getRut())  && isValidNombres(usuario.getNombres()) && isValidApellidos(usuario.getApellidos()) && isValidDireccion(usuario.getDireccion()) && isValidTelefono(usuario.getTelefono());
    }
    
    public boolean isValidDigitoVerificador(String rut, String digitoVerificador) {
       return digitoVerificador.equalsIgnoreCase(String.valueOf(getDigitoVerificador(Integer.parseInt(rut))));
    }
    
    public char getDigitoVerificador(int rut) {
        int m = 0, s = 1;
        int asciiKValue = 75;
        for (; rut != 0; rut /= 10) {
             s = (s + rut % 10 * (9 - m++ % 6)) % 11;
        }
        return ((char) (s != 0 ? s + 47 : asciiKValue));
    }
    
    public String formatRut(String rut){
        rut = rut.replace(".","").replace("-","");
        StringBuilder builder = new StringBuilder();
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        formatter.setDecimalFormatSymbols(symbols);
        if (rut.length() > 1) { 
            String digitoVerificador = rut.substring(rut.length() - 1); 
            String rutSinDgv = rut.substring(0, rut.length() - 1);
            return builder.append(formatter.format(new BigInteger(rutSinDgv))).append("-").append(digitoVerificador).toString();
        }
        return formatter.format(new BigInteger(rut));
    }
}
