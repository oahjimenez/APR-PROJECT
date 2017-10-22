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
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        double lecturaMensual = 0.0;
        PreparedStatement statement = null;
        try {
            statement = this.driverManager.getConnection().prepareStatement("SELECT VALOR FROM LECTURA_MENSUAL where USUARIO_ID = ? AND MEDIDOR_ID = ? AND CAST(strftime('%m',FECHA) as integer) = ? AND CAST(strftime('%Y',FECHA) as integer) = ?;");
            statement.setInt(1,usuario.getId());
            statement.setString(2,medidor.getId());
            statement.setInt(3,fecha.getMonthValue());
            statement.setInt(4,fecha.getYear());
           
           
            ResultSet lecturaRs = statement.executeQuery();
            
            while (lecturaRs.next()) {
                lecturaMensual = lecturaRs.getDouble("VALOR");
            }
            return lecturaMensual;
        }catch (Exception e){
            System.err.println(String.format("%s - getLecturaMensual() : %s %s",this.getClass().getSimpleName(),e.getClass().getSimpleName(),e.getMessage()));
        } finally {
            try {
                statement.close();
            } catch (SQLException ex) {
                Logger.getLogger(LecturaMensualRepositoryImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return lecturaMensual;  
     }

    @Override
    public boolean save(Usuario usuario,Medidor medidor, LocalDate fecha, double lectura) {
       try {
            String upsertSql = "INSERT OR REPLACE INTO LECTURA_MENSUAL(USUARIO_ID,MEDIDOR_ID,FECHA,VALOR) VALUES (?,?,?,?);";
            
            PreparedStatement statement = this.driverManager.getConnection().prepareStatement(upsertSql);
            statement.setInt(1,usuario.getId());
            statement.setString(2,medidor.getId());
            statement.setString(3,fecha.format(DateTimeFormatter.ISO_DATE));
            statement.setDouble(4,lectura);
            int rowsAffected = statement.executeUpdate();
            statement.close();
            return rowsAffected==1;
        }catch (Exception e){
            System.err.println(String.format("%s - save(): %s %s",this.getClass().getSimpleName(),e.getClass().getSimpleName(),e.getMessage()));
        }
       return false;
    }

    @Override
    public List<Integer> getIdUsuariosConLecturaMensual(LocalDate fecha) {
        List<Integer> ids = new ArrayList<>();
        try {
            PreparedStatement statement = this.driverManager.getConnection().prepareStatement("SELECT USUARIO_ID FROM LECTURA_MENSUAL WHERE CAST(strftime('%m',FECHA) as integer) = ? AND CAST(strftime('%Y',FECHA) as integer) = ?;");
            statement.setInt(1,fecha.getMonthValue());
            statement.setInt(1,fecha.getYear());
            ResultSet lecturasRs = statement.executeQuery();
            while (lecturasRs.next()) {
                ids.add(lecturasRs.getInt("USUARIO_ID"));
            }
            statement.close();
        }catch (Exception e){
            System.err.println(String.format("%s - getIdUsuariosConLecturaMensual(): %s - %s",this.getClass().getSimpleName(),e.getClass().getName(),e.getMessage() ));
        }
        return ids; 
    }
    
}
