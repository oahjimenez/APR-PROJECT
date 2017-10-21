/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.aguapotablerural.model;

/**
 *
 * @author carlo
 */
public class CostoMetroCubico {
    
    private final int id;
    private final double costo;
    
    public CostoMetroCubico(int id,double costo){
        this.id = id;
        this.costo = costo;
    }
    
    public double getCosto() {
        return this.costo;
    }
    
}
