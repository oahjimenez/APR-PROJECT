/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.aguapotablerural.dao.impl;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import main.java.aguapotablerural.dao.repository.CostoMetroCubicoRepository;
import main.java.aguapotablerural.database.contract.DBDriverManager;
import main.java.aguapotablerural.model.CostoMetroCubico;
import main.java.aguapotablerural.model.Usuario;

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
    public CostoMetroCubico getCostoMetroCubico() {
        CostoMetroCubico costoMetroCubico = null;
         try {
            PreparedStatement statement = driverManager.getConnection().prepareStatement("SELECT ID,COSTO FROM COSTO_METRO_CUBICO LIMIT 1;");;
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
