/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.aguapotablerural.services;

import java.time.LocalDate;
import main.java.aguapotablerural.dao.impl.CargoFijoMensualRepositoryImpl;
import main.java.aguapotablerural.dao.repository.CargoFijoMensualRepository;
import main.java.aguapotablerural.database.contract.DBDriverManager;
import main.java.aguapotablerural.database.impl.SqliteDriverManager;
import main.java.aguapotablerural.model.CargoFijoMensual;
import main.java.aguapotablerural.model.Usuario;

/**
 *
 * @author carlo
 */
public class CargoFijoMensualService {
    private final CargoFijoMensualRepository cargoFijoMensualRepository;
    
    public CargoFijoMensualService() {
        DBDriverManager driver = new SqliteDriverManager();
        this.cargoFijoMensualRepository = new CargoFijoMensualRepositoryImpl(driver);
    }
    
    public CargoFijoMensual getCargoFijoMensual(LocalDate fecha) {
        return this.cargoFijoMensualRepository.getCargoFijoMensual(fecha);
    }
    
    public CargoFijoMensual getgetCargoFijoMensual(Usuario usuario) {
        return this.cargoFijoMensualRepository.getCargoFijoMensual(LocalDate.now());
    }

}
