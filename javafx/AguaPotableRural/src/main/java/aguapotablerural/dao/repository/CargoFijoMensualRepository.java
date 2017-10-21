/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.aguapotablerural.dao.repository;

import java.time.LocalDate;
import main.java.aguapotablerural.model.CargoFijoMensual;

/**
 *
 * @author carlo
 */
public interface CargoFijoMensualRepository {
    public CargoFijoMensual getCargoFijoMensual(LocalDate fecha);
    
}
