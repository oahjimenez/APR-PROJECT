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
    public double getLecturaMensual(Medidor medidor, Date fecha) {
       double lecturaMensual = -1;
       try {
            PreparedStatement statement = this.driverManager.getConnection().prepareStatement("SELECT VALOR FROM LECTURA_MENSUAL where MEDIDOR_ID = ? AND FECHA = ?;");
            statement.setString(1,medidor.getId());
            statement.setDate(1,fecha);

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
    public double getLecturaMensual(Usuario usuario, Date fecha) {
        double lecturaMensual = -1;
        try {
            PreparedStatement statement = this.driverManager.getConnection().prepareStatement("SELECT VALOR FROM LECTURA_MENSUAL where USUARIO_RUT = ? AND FECHA = ?;");
            statement.setString(1,usuario.getRut());
            statement.setDate(2,fecha);
           
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
    public double getLecturaMensual(Usuario usuario, Medidor medidor,Date fecha) {
        double lecturaMensual = -1;
        try {
            PreparedStatement statement = this.driverManager.getConnection().prepareStatement("SELECT VALOR FROM LECTURA_MENSUAL where USUARIO_RUT = ? AND MEDIDOR_ID = ? AND FECHA = ?;");
            statement.setString(1,usuario.getRut());
            statement.setString(2,medidor.getId());
            statement.setDate(3,fecha);
           
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
           //update or insertS
            String upsertSql = "UPDATE LECTURA_MENSUAL SET VALOR=? WHERE USUARIO_RUT=? AND CAST(strftime('%m',FECHA) as integer) = ? AND CAST(strftime('%Y',FECHA) as integer) = ?; INSERT INTO LECTURA_MENSUAL(USUARIO_RUT,MEDIDOR_ID,FECHA,VALOR) SELECT ?,(SELECT RUT),? WHERE (Select Changes() = 0);";
            
            PreparedStatement statement = this.driverManager.getConnection().prepareStatement(upsertSql);
            statement.setDouble(1,lectura);
            statement.setString(2,usuario.getRut());
            statement.setInt(3,fecha.getMonthValue());
            statement.setInt(4,fecha.getYear());
            
            statement.setString(5,usuario.getRut());
            statement.setString(6,medidor.getId());
            statement.setDate(7,Date.valueOf(fecha));
            statement.setDouble(8,lectura);
            int rowsAffected = statement.executeUpdate();
            statement.close();
            return rowsAffected==1;
        }catch (Exception e){
            System.err.println(String.format("%s - save(): %s %s",this.getClass().getSimpleName(),e.getClass().getSimpleName(),e.getMessage()));
        }
       return false;
    }
    
}
