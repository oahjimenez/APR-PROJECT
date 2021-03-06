/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.aguapotablerural.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import main.java.aguapotablerural.dao.repository.CargoFijoMensualRepository;
import main.java.aguapotablerural.database.contract.DBDriverManager;
import main.java.aguapotablerural.model.CargoFijoMensual;

/**
 *
 * @author carlo
 */
public class CargoFijoMensualRepositoryImpl implements CargoFijoMensualRepository {

    protected DBDriverManager driverManager;
    
    public CargoFijoMensualRepositoryImpl() {}
    
    public CargoFijoMensualRepositoryImpl(DBDriverManager driverManager) {
        this.driverManager = driverManager;
    }
     public CargoFijoMensual getCargoFijoMensual(LocalDate fecha) {
        CargoFijoMensual cargoFijoMensual = null;
        try {
            PreparedStatement statement = driverManager.getConnection().prepareStatement("SELECT ID,CARGO FROM CARGO_FIJO_MENSUAL WHERE CAST(strftime('%m',FECHA) as integer) = ? AND CAST(strftime('%Y',FECHA) as integer) = ?;");;
            statement.setInt(1,fecha.getMonthValue());
            statement.setInt(2,fecha.getYear());
            ResultSet cargoFijoMensualRs = statement.executeQuery();
            if (cargoFijoMensualRs.next()) {
                int id = cargoFijoMensualRs.getInt("id");
                double cargo = cargoFijoMensualRs.getDouble("cargo");
                cargoFijoMensual = new CargoFijoMensual(id,cargo);
            }
            statement.close();
        }catch (Exception e){
            System.err.println(String.format("%s - getCargoFijoMensual() : %s - %s",this.getClass().getSimpleName(),e.getClass().getSimpleName(),e.getMessage()));
        }
        return cargoFijoMensual;
    }
}
