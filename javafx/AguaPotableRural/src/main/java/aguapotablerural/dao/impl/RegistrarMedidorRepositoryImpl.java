/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.aguapotablerural.dao.impl;

import main.java.aguapotablerural.dao.repository.AdministradorRepository;
import main.java.aguapotablerural.dao.repository.MedidorRepository;
import main.java.aguapotablerural.dao.repository.RegistraMedidorRepository;
import main.java.aguapotablerural.database.contract.DBDriverManager;
import main.java.aguapotablerural.model.Administrador;
import main.java.aguapotablerural.model.Medidor;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author carlo
 */
public class RegistrarMedidorRepositoryImpl implements RegistraMedidorRepository {

    private AdministradorRepository administradorRepository;
    private MedidorRepository medidorRepository;
    private DBDriverManager driverManager;

    public RegistrarMedidorRepositoryImpl(DBDriverManager driverManager, AdministradorRepository administradorRepository,MedidorRepository medidorRepository) {
      this.driverManager = driverManager;
      this.administradorRepository = administradorRepository;
      this.medidorRepository = medidorRepository;
    }
    
    @Override
    public Collection<Administrador> getAdministradores(Medidor medidor) {
      Collection<Administrador> administradores = new ArrayList();
        try {
            PreparedStatement statement = this.driverManager.getConnection().prepareStatement("SELECT ADMINISTRADOR_RUT FROM REGISTRA_MEDIDOR WHERE MEDIDOR_ID = ?;");
            statement.setString(1,medidor.getId());
            
            ResultSet administradoresRs = statement.executeQuery();
            
            while (administradoresRs.next()) {
                Administrador admin;
                if ((admin = this.administradorRepository.getActive(administradoresRs.getString("administrador_rut"))) != null) {
                    administradores.add(admin);
                }
            }
            statement.close();
        }catch (Exception e){
            System.err.println(this.getClass()+ ": " +e.getClass().getName() + ": " + e.getMessage() );
        }
        return administradores;  
      }

    @Override
    public Collection<Medidor> getMedidores(Administrador administrador) {
    Collection<Medidor> medidores = new ArrayList();
        try {
            PreparedStatement statement = this.driverManager.getConnection().prepareStatement("SELECT MEDIDOR_ID FROM REGISTRA_MEDIDOR WHERE ADMINISTRADOR_RUT = ?;");
            statement.setString(1,administrador.getRut());
            
            ResultSet medidoresRs = statement.executeQuery();
            
            while (medidoresRs.next()) {
                Medidor medidor;
                if ((medidor = this.medidorRepository.get(medidoresRs.getString("MEDIDOR_ID"))) != null) {
                    medidores.add(medidor);
                }
            }
            statement.close();
        }catch (Exception e){
            System.err.println(this.getClass()+ ": " +e.getClass().getName() + ": " + e.getMessage() );
        }
        return medidores;  
 }

    @Override
    public boolean save(Administrador admin, Medidor medidor, Date fechaRegistro) {
        try {
            PreparedStatement statement = this.driverManager.getConnection().prepareStatement("INSERT OR REPLACE INTO REGISTRA_MEDIDOR (ADMINISTRADOR_RUT,MEDIDOR_ID,FECHA_REGISTRO) VALUES (?, ? , ?);");
            statement.setString(1,admin.getRut());
            statement.setString(2,medidor.getId());
            statement.setDate(3,fechaRegistro);
            
            int rowsAffected = statement.executeUpdate();
            statement.close();
            return rowsAffected>0;
        }catch (Exception e){
            System.err.println(this.getClass()+ ": " + e.getClass().getName() + ": " + e.getMessage() );
        }  
        return false;
     }
    
}
