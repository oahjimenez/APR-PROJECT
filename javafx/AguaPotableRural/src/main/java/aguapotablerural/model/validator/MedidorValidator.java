/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.aguapotablerural.model.validator;

/**
 *
 * @author cmardones
 */
public class MedidorValidator {
    public static final int ID_MAXCHAR = 10;
    
    
    public static boolean isValidId(String id) {
        return (id!=null) && (!id.isEmpty()) && (id.length() <= ID_MAXCHAR) && (id.matches("[0-9]+"));
    }
}