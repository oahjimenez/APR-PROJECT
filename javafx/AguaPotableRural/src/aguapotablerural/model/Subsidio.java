/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aguapotablerural.model;

import java.sql.Date;

/**
 *
 * @author carlo
 */
public class Subsidio {
    
    private Usuario usuario;

    private Medidor medidor;
    private double porcentaje;
    private double tope;
    private Date fecha;

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public double getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(double porcentaje) {
        this.porcentaje = porcentaje;
    }

    public double getTope() {
        return tope;
    }

    public void setTope(double tope) {
        this.tope = tope;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    
    public Medidor getMedidor() {
        return medidor;
    }

    public void setMedidor(Medidor medidor) {
        this.medidor = medidor;
    }
    
    @Override
    public String toString() {
        return "Subsidio{" + "usuario=" + usuario + ", porcentaje=" + porcentaje + ", tope=" + tope + ", fecha=" + fecha + '}';
    }    
}
