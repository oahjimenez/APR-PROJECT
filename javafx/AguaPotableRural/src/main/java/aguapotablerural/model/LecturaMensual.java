/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.aguapotablerural.model;

import java.time.LocalDate;

/**
 *
 * @author cmardones
 */
public class LecturaMensual {
    
    private Medidor medidor;
    private Double lectura = 0.0;
    private LocalDate fecha;
    private Double costo = 0.0;
    

    public Medidor getMedidor() {
        return medidor;
    }

    public void setMedidor(Medidor medidor) {
        this.medidor = medidor;
    }

    public Double getLectura() {
        return lectura;
    }

    public void setLectura(Double lectura) {
        this.lectura = lectura;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Double getCosto() {
        return costo;
    }

    public void setCosto(Double costo) {
        this.costo = costo;
    }
    
    
}
