/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.aguapotablerural.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import main.java.aguapotablerural.dao.repository.CostoMetroCubicoRepository;
import main.java.aguapotablerural.database.contract.DBDriverManager;
import main.java.aguapotablerural.model.CostoMetroCubico;

/**
 *
 * @author carlo
 */
public class CostoMetroCubicoRepositoryImpl implements CostoMetroCubicoRepository {

    protected DBDriverManager driverManager;
    public CostoMetroCubicoRepositoryImpl() {}
    
    public CostoMetroCubicoRepositoryImpl(DBDriverManager driverManager) {
        this.driverManager = driverManager;
    }
    
    @Override
    public CostoMetroCubico getCostoMetroCubico(LocalDate fecha) {
        CostoMetroCubico costoMetroCubico = null;
         try {
            PreparedStatement statement = driverManager.getConnection().prepareStatement("SELECT ID,COSTO FROM COSTO_METRO_CUBICO WHERE CAST(strftime('%m',FECHA) as integer) = ? AND CAST(strftime('%Y',FECHA) as integer) = ?;");;
            statement.setInt(1,fecha.getMonthValue());
            statement.setInt(2,fecha.getYear());
            ResultSet costoMetroCubicoRs = statement.executeQuery();
            if (costoMetroCubicoRs.next()) {
                int id = costoMetroCubicoRs.getInt("id");
                double costo = costoMetroCubicoRs.getDouble("costo");
                costoMetroCubico = new CostoMetroCubico(id,costo);
            }
            statement.close();
        }catch (Exception e){
            System.err.println(String.format("%s - getCostoMetroCubico() : %s - %s",this.getClass().getSimpleName(),e.getClass().getSimpleName(),e.getMessage()));
        }
        return costoMetroCubico;
    }
    
}
