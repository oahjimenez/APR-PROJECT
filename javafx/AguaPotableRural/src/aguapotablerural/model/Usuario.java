/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aguapotablerural.model;

import aguapotablerural.database.impl.SqliteDriverManager;
import java.sql.Date;
import java.sql.PreparedStatement;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author carlo
 */
public class Usuario {
    
    private final SimpleStringProperty rutColumnProperty;
    private final SimpleStringProperty nombreColumnProperty;
    private final SimpleStringProperty direccionColumnProperty;
    private final SimpleStringProperty telefonoColumnProperty;
    
    private String rut;
    private String nombre;
    private String direccion;
    private String telefono;
    private Date fechaRegistro;
    private Date fechaRetiro;

    public Usuario () {
        this(null,null,null,null);
    }
    
    public Usuario(String rut,String nombre,String dreccion, String telefono) {
        this.rutColumnProperty = new SimpleStringProperty(rut);
        this.nombreColumnProperty = new SimpleStringProperty(nombre);
        this.direccionColumnProperty  = new SimpleStringProperty(direccion);
        this.telefonoColumnProperty = new SimpleStringProperty(telefono);
    }
    
    public String getRut() {
        return this.rutColumnProperty.get();
    }
    
    public StringProperty getRutColumnProperty() {
        return this.rutColumnProperty;
    }

    public void setRut(String rut) {
        this.rut = rut;
        this.rutColumnProperty.set(rut);
    }
    

    public String getNombre() {
        return this.nombreColumnProperty.get();
    }
    
    public StringProperty getNombreColumnProperty() {
        return this.nombreColumnProperty;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
        this.nombreColumnProperty.set(nombre);
    }

    public String getDireccion() {
        return this.direccionColumnProperty.get();
    }
    
    public StringProperty getDireccionColumnProperty() {
        return this.direccionColumnProperty;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
        this.direccionColumnProperty.set(direccion);
    }

    public String getTelefono() {
        return this.telefonoColumnProperty.get();
    }
    
    public StringProperty getTelefonoColumnProperty() {
        return this.telefonoColumnProperty;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
        this.telefonoColumnProperty.set(telefono);
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
