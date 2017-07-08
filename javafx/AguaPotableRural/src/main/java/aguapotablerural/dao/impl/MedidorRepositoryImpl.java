/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.aguapotablerural.dao.impl;

import java.sql.Date;
import main.java.aguapotablerural.dao.contract.MedidorRepository;
import main.java.aguapotablerural.database.contract.DBDriverManager;
import main.java.aguapotablerural.database.impl.SqliteDriverManager;
import main.java.aguapotablerural.model.Medidor;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

/**
 *
 * @author carlo
 */
public class MedidorRepositoryImpl implements MedidorRepository {

     private DBDriverManager driverManager;

    public MedidorRepositoryImpl(DBDriverManager driverManager) {
        this.driverManager = driverManager;
    }
    
    @Override
    public Medidor get(String id) {
          
       Medidor medidor = null;
       try {
            PreparedStatement statement = driverManager.getConnection().prepareStatement("SELECT * FROM MEDIDOR where ID = ? AND FECHA_RETIRO IS NULL;");
            statement.setString(1,id);
            ResultSet medidorRs = statement.executeQuery();

            String medidorId = medidorRs.getString("id");
            String descripcion = medidorRs.getString("descripcion");
            //Date fechaRegistro = java.sql.Date.valueOf( medidorRs.getObject("fecha_registro", LocalDate.class ) );
            //Date fechaRetiro = medidorRs.getDate("fecha_retiro");
            medidor = new Medidor();
            medidor.setId(medidorId);
            medidor.setDescripcion(descripcion);
           // medidor.setFechaRegistro(fechaRegistro);
           // medidor.setFechaRetiro(fechaRetiro);
            statement.close();
        }catch (Exception e){
            System.err.println(this.getClass()+ ": " +e.getClass().getName() + ": " + e.getMessage() );
        }
        return medidor;
    }
    
    @Override
    public boolean save(Medidor medidor) {
       try {
            PreparedStatement statement = this.driverManager.getConnection().prepareStatement("INSERT OR REPLACE INTO MEDIDOR (id,descripcion,fecha_registro,fecha_retiro) " +
                     "  VALUES (?, ? , CURRENT_DATE, ?  );");
            statement.setString(1,medidor.getId());
            statement.setString(2,medidor.getDescripcion());
            statement.setDate(3,medidor.getFechaRetiro());
            int rowsAffected = statement.executeUpdate();
            statement.close();
            return rowsAffected>0;
        }catch (Exception e){
            System.err.println(this.getClass()+ ": " + e.getClass().getName() + ": " + e.getMessage() );
        }
       return false;
    }

    @Override
    public boolean saveAll(Collection<? extends Medidor> medidores) {
        boolean allSaved = true;
        for (Medidor medidor : medidores){
            allSaved = allSaved && this.save(medidor);
        }
        return allSaved;
    }
    
    @Override
    public boolean delete(Medidor medidor) {
        System.out.println("Eliminando medidor en base sqlite3");
        try {
            PreparedStatement statement = this.driverManager.getConnection().prepareStatement("UPDATE MEDIDOR SET FECHA_RETIRO = CURRENT_DATE WHERE ID = ?;");
            statement.setString(1,medidor.getId());
            int rowsAffected = statement.executeUpdate();
            statement.close();
            return rowsAffected > 0;
        }catch (Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
        return false;
    }

    @Override
    public Collection<Medidor> getAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<Medidor> getAllActive() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
