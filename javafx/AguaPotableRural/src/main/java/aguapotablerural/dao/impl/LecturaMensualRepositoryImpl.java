/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.aguapotablerural.dao.impl;

import main.java.aguapotablerural.dao.repository.LecturaMensualRepository;
import main.java.aguapotablerural.database.contract.DBDriverManager;
import main.java.aguapotablerural.model.Medidor;
import main.java.aguapotablerural.model.Usuario;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

/**
 *
 * @author carlo
 */
public class LecturaMensualRepositoryImpl implements LecturaMensualRepository{

    private DBDriverManager driverManager;

    public LecturaMensualRepositoryImpl(DBDriverManager driverManager) {
        this.driverManager = driverManager;
    }

    @Override
    public double getLecturaMensual(Usuario usuario, Medidor medidor,LocalDate fecha) {
        double lecturaMensual = -1;
        try {
            PreparedStatement statement = this.driverManager.getConnection().prepareStatement("SELECT VALOR FROM LECTURA_MENSUAL where USUARIO_RUT = ? AND MEDIDOR_ID = ? AND FECHA = ?;");
            statement.setString(1,usuario.getRut());
            statement.setString(2,medidor.getId());
            statement.setDate(3,Date.valueOf(fecha));
           
            ResultSet lecturaRs = statement.executeQuery();
            lecturaMensual = lecturaRs.getDouble("VALOR");
            
            statement.close();
            return lecturaMensual;
        }catch (Exception e){
            System.err.println(this.getClass()+ ": " +e.getClass().getName() + ": " + e.getMessage() );
        }
        return lecturaMensual;  
     }

    @Override
    public boolean save(Usuario usuario,Medidor medidor, LocalDate fecha, double lectura) {
       try {
            String upsertSql = "INSERT OR REPLACE INTO LECTURA_MENSUAL(USUARIO_RUT,MEDIDOR_ID,FECHA,VALOR) VALUES (?,?,?,?);";
            
            PreparedStatement statement = this.driverManager.getConnection().prepareStatement(upsertSql);
            statement.setString(1,usuario.getRut());
            statement.setString(2,medidor.getId());
            statement.setDate(3,Date.valueOf(fecha));
            statement.setDouble(4,lectura);
            int rowsAffected = statement.executeUpdate();
            statement.close();
            return rowsAffected==1;
        }catch (Exception e){
            System.err.println(String.format("%s - save(): %s %s",this.getClass().getSimpleName(),e.getClass().getSimpleName(),e.getMessage()));
        }
       return false;
    }
    
}
