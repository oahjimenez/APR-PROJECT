/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aguapotablerural.model;

import aguapotablerural.database.SQLiteJDBCDriverConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 *
 * @author carlo
 */
public class Usuario {
    
    private String rut;
    private String password;
    private String nombre;
    private String direccion;
    private String telefono;
    
    private SQLiteJDBCDriverConnection dbconnection;

    public Usuario(SQLiteJDBCDriverConnection dbconnection){
        this.dbconnection = dbconnection;
    }
    
    public Usuario(String rut, String nombre, String direccion, String telefono, SQLiteJDBCDriverConnection dbconnection) {
        this.rut = rut;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.dbconnection = dbconnection;
    }

    public String getRut() {
        return rut;
    }

    public Usuario setRut(String rut) {
        this.rut = rut;
        return this;
    }
    
    
    public String getPassword() {
        return password;
    }

    public Usuario setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getNombre() {
        return nombre;
    }

    public Usuario setNombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public String getDireccion() {
        return direccion;
    }

    public Usuario setDireccion(String direccion) {
        this.direccion = direccion;
        return this;
    }

    public String getTelefono() {
        return telefono;
    }

    public Usuario setTelefono(String telefono) {
        this.telefono = telefono;
        return this;
    }
    
    @Override
    public String toString() {
        return "Usuario{" + "rut=" + rut + ", nombre=" + nombre + ", direccion=" + direccion + ", telefono=" + telefono + '}';
    }

    
    public void save(){
        try {
            PreparedStatement statement = dbconnection.getConnection().prepareStatement("INSERT OR REPLACE INTO USUARIO (rut,password,nombre,direccion,telefono) " +
                     "  VALUES (?, ? , ?, ? , ? );");
            statement.setString(1,this.rut);
            statement.setString(2,this.password);
            statement.setString(3,this.nombre);
            statement.setString(4,this.direccion);
            statement.setString(5,this.telefono);
            statement.executeUpdate();
            statement.close();
        }catch (Exception e){
            System.err.println(this.getClass()+ ": " + e.getClass().getName() + ": " + e.getMessage() );
        }
    }
    
    public void delete() {
          try {
            PreparedStatement statement = dbconnection.getConnection().prepareStatement("DELETE FROM USUARIO WHERE rut = ?;");
            statement.setString(1,this.rut);
            statement.executeUpdate();
            statement.close();
        }catch (Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }
}
