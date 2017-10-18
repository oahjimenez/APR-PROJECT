/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.aguapotablerural.services;

import java.time.LocalDate;
import main.java.aguapotablerural.model.Medidor;
import main.java.aguapotablerural.model.Usuario;

/**
 *
 * @author carlo
 */
public class ConsumoService {
    
    private static final double CARGO_FIJO_MENSUAL = 1500.00; //todo parametrizar por mes y agregar tabla log de cambio
    private static final double COSTO_METRO_CUBICO = 500; //todo parametrizar por mes y agregar tabla log de cambio
    private static final double TOPE_SUBSIDIADO = -1; //todo parametrizar por usuario y mes, agregar tabla log de cambio
    
    private final SubsidioService subsidioService;
    private final LecturaService lecturaService;
    
    
    public ConsumoService() {
        this.lecturaService = new LecturaService();
        this.subsidioService = new SubsidioService();
    }
    
    public double getConsumoMensual(Usuario usuario,Medidor medidor, LocalDate mes) {
        return lecturaService.obtenerLectura(usuario, medidor, mes) - lecturaService.obtenerLectura(usuario, medidor, mes.minusMonths(1));
    }
    
    public double getValorACancelarSinSubsidio(Usuario usuario,Medidor medidor,LocalDate mes) {
        return CARGO_FIJO_MENSUAL + COSTO_METRO_CUBICO*this.getConsumoMensual(usuario,medidor,mes);
    }
    
    public double getTopeSubsidiado(Usuario usuario,LocalDate mes) {
        return this.subsidioService.getSubsidio(usuario, mes).getTope();
    }
    
    public double getPorcentajeSubsidio(Usuario usuario,LocalDate mes) {
        return -1;
    }
    
    public double getValorACancelarConSubsidio(Usuario usuario,Medidor medidor, LocalDate mes) {
        double consumoMensual = this.getConsumoMensual(usuario,medidor,mes);
        double valorACancelarConSubsidio = 0.0;
        if (consumoMensual < getTopeSubsidiado(usuario,mes)) {
            valorACancelarConSubsidio = (CARGO_FIJO_MENSUAL + COSTO_METRO_CUBICO*consumoMensual)*(1-getPorcentajeSubsidio(usuario,mes));
        } else {
            valorACancelarConSubsidio = ((CARGO_FIJO_MENSUAL + COSTO_METRO_CUBICO*getTopeSubsidiado(usuario,mes))*(1-getPorcentajeSubsidio(usuario,mes)) + (COSTO_METRO_CUBICO*(consumoMensual-getTopeSubsidiado(usuario,mes))));
        }
        return valorACancelarConSubsidio;
    }
    public static void main(String[] args) {
        ConsumoService consumoService = new ConsumoService();
        Usuario usuario = new Usuario();
        usuario.setId(1);
        System.out.println("tope:"+consumoService.getTopeSubsidiado(usuario,LocalDate.now()));
    }
    
    
}
