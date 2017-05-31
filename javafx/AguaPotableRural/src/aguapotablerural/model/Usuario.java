/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aguapotablerural.model;

import aguapotablerural.database.SQLiteJDBCDriverConnection;
import java.sql.Date;
import java.sql.PreparedStatement;

/**
 *
 * @author carlo
 */
public class Usuario implements ModeloDominio {
    
    private String rut;
    private String nombre;
    private String direccion;
    private String telefono;
    private Date fechaRegistro;
    private Date fechaRetiro;
    
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

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public Usuario setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
        return this;
    }

    public Date getFechaRetiro() {
        return fechaRetiro;
    }

    public Usuario setFechaRetiro(Date fechaRetiro) {
        this.fechaRetiro = fechaRetiro;
        return this;
    }
    
    protected SQLiteJDBCDriverConnection getDbconnection() {
        return this.dbconnection;
    }

    @Override
    public String toString() {
        return "Usuario{" + "rut=" + rut + ", nombre=" + nombre + ", direccion=" + direccion + ", telefono=" + telefono + ", fechaRegistro=" + fechaRegistro + ", fechaRetiro=" + fechaRetiro + '}';
    }
    
    @Override
    public void save(){
        try {
            PreparedStatement statement = dbconnection.getConnection().prepareStatement("INSERT OR REPLACE INTO USUARIO (rut,nombre,direccion,telefono,fechaRegistro,fechaRetiro) " +
                     "  VALUES (?, ? , ?, ? , ? );");
            statement.setString(1,this.rut);
            statement.setString(3,this.nombre);
            statement.setString(4,this.direccion);
            statement.setString(5,this.telefono);
            statement.setDate(5,this.fechaRegistro);
            statement.setDate(6,this.fechaRetiro);
            statement.executeUpdate();
            statement.close();
        }catch (Exception e){
            System.err.println(this.getClass()+ ": " + e.getClass().getName() + ": " + e.getMessage() );
        }
    }
    
    @Override
    public void delete() {
          try {
            PreparedStatement statement = dbconnection.getConnection().prepareStatement("UPDATE USUARIO SET FECHA_FINIQUITO = CURRENT_DATE WHERE rut = ?;");
            statement.setString(1,this.rut);
            statement.executeUpdate();
            statement.close();
        }catch (Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }
}
