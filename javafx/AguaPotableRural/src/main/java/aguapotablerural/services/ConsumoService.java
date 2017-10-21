/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.aguapotablerural.services;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;
import main.java.aguapotablerural.model.CargoFijoMensual;
import main.java.aguapotablerural.model.CostoMetroCubico;
import main.java.aguapotablerural.model.Medidor;
import main.java.aguapotablerural.model.Subsidio;
import main.java.aguapotablerural.model.Usuario;

/**
 *
 * @author carlo
 */
public class ConsumoService {
        
    private final SubsidioService subsidioService;
    private final LecturaService lecturaService;
    private final CostoMetroCubicoService costoMetroCubicoService;
    private final CargoFijoMensualService cargoFijoMensualService;
    
    
    public ConsumoService() {
        this.lecturaService = new LecturaService();
        this.subsidioService = new SubsidioService();
        this.costoMetroCubicoService = new CostoMetroCubicoService();
        this.cargoFijoMensualService = new CargoFijoMensualService();
    }
    
    public double getConsumoMensual(Usuario usuario,Medidor medidor, LocalDate mes) {
        return lecturaService.obtenerLectura(usuario, medidor, mes) - lecturaService.obtenerLectura(usuario, medidor, mes.minusMonths(1));
    }
    
    public double getValorACancelarSinSubsidio(Usuario usuario,Medidor medidor,LocalDate mes) {
        return this.getCargoFijoMensual(mes).getCargo() + this.getCostoMetroCubico(mes).getCosto()*this.getConsumoMensual(usuario,medidor,mes);
    }
    
    private Subsidio getSubsidio(Usuario usuario,LocalDate mes) {
        return this.subsidioService.getSubsidio(usuario, mes);
    }
    
    public CostoMetroCubico getCostoMetroCubico(LocalDate fecha) {
        return this.costoMetroCubicoService.getCostoMetroCubico(fecha);
    }
    
    public CargoFijoMensual getCargoFijoMensual(LocalDate fecha) {
        return this.cargoFijoMensualService.getCargoFijoMensual(fecha);
    }
    
    public double getTopeSubsidiado(Usuario usuario,LocalDate mes) {
        return this.getSubsidio(usuario, mes).getTope();
    }
    
    public double getPorcentajeSubsidio(Usuario usuario,LocalDate mes) {
        return this.getSubsidio(usuario, mes).getPorcentaje();
    }
    
    public double getValorACancelarConSubsidio(Usuario usuario,Medidor medidor, LocalDate mes) {
        double consumoMensual = this.getConsumoMensual(usuario,medidor,mes);
        double valorACancelarConSubsidio = 0.0;
        if (consumoMensual < getTopeSubsidiado(usuario,mes)) {
            valorACancelarConSubsidio = (this.cargoFijoMensualService.getCargoFijoMensual(mes).getCargo() + this.getCostoMetroCubico(mes).getCosto()*consumoMensual)*(1-getPorcentajeSubsidio(usuario,mes));
        } else {
            valorACancelarConSubsidio = ((this.cargoFijoMensualService.getCargoFijoMensual(mes).getCargo() + this.getCostoMetroCubico(mes).getCosto()*getTopeSubsidiado(usuario,mes))*(1-getPorcentajeSubsidio(usuario,mes)) + (this.getCostoMetroCubico(mes).getCosto()*(consumoMensual-getTopeSubsidiado(usuario,mes))));
        }
        return valorACancelarConSubsidio;
    }
    public static void main(String[] args) {
        ConsumoService consumoService = new ConsumoService();
        Usuario usuario = new Usuario();
        usuario.setId(1);
        LocalDate mesActual = LocalDate.now();
        String nombreMesActual =  LocalDate.now().getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH).toString();
        System.out.println(String.format("tope mes %s: %s",nombreMesActual,consumoService.getTopeSubsidiado(usuario,mesActual)));
        System.out.println(String.format("costo metro cubico mes %s: %s",nombreMesActual,consumoService.getCostoMetroCubico(mesActual).getCosto()));
        System.out.println(String.format("cargo fijo mensual mes %s: %s",nombreMesActual,consumoService.getCargoFijoMensual(mesActual).getCargo()));

    }
    
    
}
