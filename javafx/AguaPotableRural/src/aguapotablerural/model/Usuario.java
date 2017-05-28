/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aguapotablerural.model;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 *
 * @author carlo
 */
public class Usuario {
    
    private String rut;
    private String nombre;
    private String direccion;
    private String telefono;

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    @Override
    public String toString() {
        return "Usuario{" + "rut=" + rut + ", nombre=" + nombre + ", direccion=" + direccion + ", telefono=" + telefono + '}';
    }

    
    public void save(){
        Connection connection = null;
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT OR REPLACE INTO USUARIO (rut,nombre,direccion,telefono) " +
                     "  VALUES (? , ?, ? , ? );");
            statement.setString(1,this.rut);
            statement.setString(2,this.nombre);
            statement.setString(3,this.direccion);
            statement.setString(4,this.telefono);
            statement.executeQuery();
            statement.close();
        }catch (Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }
}
