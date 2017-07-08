/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.aguapotablerural.model;

import main.java.aguapotablerural.database.impl.SqliteDriverManager;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.Objects;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author carlo
 */
public class Usuario implements Comparable{
    
    private final SimpleStringProperty rutProperty;
    private final SimpleStringProperty nombresProperty;
    private final SimpleStringProperty apellidosProperty;
    private final SimpleStringProperty direccionProperty;
    private final SimpleStringProperty telefonoProperty;
    private final ObservableList<Medidor> medidores = FXCollections.observableArrayList();
    
    private Date fechaRegistro;
    private Date fechaRetiro;

    public Usuario () {
        this(null,null,null,null,null);
    }
    
    public Usuario(String rut,String nombres,String apellidos,String direccion, String telefono) {
        this.rutProperty = new SimpleStringProperty(rut);
        this.nombresProperty = new SimpleStringProperty(nombres);
        this.apellidosProperty = new SimpleStringProperty(apellidos);
        this.direccionProperty  = new SimpleStringProperty(direccion);
        this.telefonoProperty = new SimpleStringProperty(telefono);
    }
    
    public String getRut() {
        return this.rutProperty.get();
    }
    
    public StringProperty getRutProperty() {
        return this.rutProperty;
    }

    public void setRut(String rut) {
        this.rutProperty.set(rut);
    }
    

    public String getNombres() {
        return this.nombresProperty.get();
    }
    
    public StringProperty getNombresProperty() {
        return this.nombresProperty;
    }

    public void setNombres(String nombres) {
        this.nombresProperty.set(nombres);
    }
    
    public String getApellidos(){
        return this.apellidosProperty.get();
    }
    
    public void setApellidos(String apellidos){
        this.apellidosProperty.set(apellidos);
    }
    
    public StringProperty getApellidosProperty() {
        return this.apellidosProperty;
    }

    public String getDireccion() {
        return this.direccionProperty.get();
    }
    
    public StringProperty getDireccionProperty() {
        return this.direccionProperty;
    }

    public void setDireccion(String direccion) {
        this.direccionProperty.set(direccion);
    }

    public String getTelefono() {
        return this.telefonoProperty.get();
    }
    
    public StringProperty getTelefonoProperty() {
        return this.telefonoProperty;
    }

    public void setTelefono(String telefono) {
        this.telefonoProperty.set(telefono);
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
    
    public ObservableList<Medidor> getMedidoresObservable() {
        return this.medidores;
    }
    
    public boolean addMedidor(Medidor medidor) {
        return this.medidores.add(medidor);
    }
    
    public boolean addMedidores(Collection<Medidor> medidores) {
        return this.medidores.addAll(medidores);
    }
    
     @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Usuario)) {
            return false;
        }
        Usuario usuario = (Usuario) o;
        return Objects.equals(this.getRut(),usuario.getRut());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getRut());
    }
    
    @Override
    public int compareTo(Object o) {
        if(!(o instanceof Usuario)){
            return -1;
        }
        if (this.getRut().equals(((Usuario)o).getRut())){
           return 0; 
        }
        return -1;
    }


    @Override
    public String toString() {
        return "Usuario{" + "rut=" + this.getRut() + ", nombre=" + this.getNombres() + ", direccion=" + this.getDireccion() + ", telefono=" + this.getTelefono() + ", fechaRegistro=" + fechaRegistro + ", fechaRetiro=" + fechaRetiro + '}';
    }
    
}
