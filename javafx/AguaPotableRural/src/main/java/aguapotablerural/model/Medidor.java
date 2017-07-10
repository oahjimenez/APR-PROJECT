/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.aguapotablerural.model;

import java.sql.Date;
import java.util.Objects;

/**
 *
 * @author carlo
 */
public class Medidor implements Comparable {
    
    private String id;
    private String descripcion;
    private Date fechaRegistro;
    private Date fechaRetiro;
    private Usuario usuario;
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Date getFechaRetiro() {
        return fechaRetiro;
    }

    public void setFechaRetiro(Date fechaRetiro) {
        this.fechaRetiro = fechaRetiro;
    }
    
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Medidor)) {
            return false;
        }
        Medidor medidor = (Medidor) o;
        return Objects.equals(this.getId(),medidor.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
    
    @Override
    public int compareTo(Object o) {
        if(!(o instanceof Medidor)){
            return -1;
        }
        if (this.getId().equals(((Medidor)o).getId())){
           return 0; 
        }
        return -1;
    }

    @Override
    public String toString() {
        return String.format("Medidor %s",this.id);
    }
  
}
