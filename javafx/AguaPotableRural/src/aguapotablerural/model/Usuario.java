/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aguapotablerural.model;

import aguapotablerural.database.impl.SqliteDriverManager;
import java.sql.Date;
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
    private Date fechaRegistro;
    private Date fechaRetiro;

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

    @Override
    public String toString() {
        return "Usuario{" + "rut=" + rut + ", nombre=" + nombre + ", direccion=" + direccion + ", telefono=" + telefono + ", fechaRegistro=" + fechaRegistro + ", fechaRetiro=" + fechaRetiro + '}';
    }
    
}
