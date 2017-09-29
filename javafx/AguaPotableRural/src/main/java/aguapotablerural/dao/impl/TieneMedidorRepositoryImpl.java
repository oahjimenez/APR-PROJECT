/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.aguapotablerural.dao.impl;

import main.java.aguapotablerural.dao.repository.MedidorRepository;
import main.java.aguapotablerural.dao.repository.TieneMedidorRepository;
import main.java.aguapotablerural.dao.repository.UsuarioRepository;
import main.java.aguapotablerural.database.contract.DBDriverManager;
import main.java.aguapotablerural.model.Medidor;
import main.java.aguapotablerural.model.Usuario;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author carlo
 */
public class TieneMedidorRepositoryImpl implements TieneMedidorRepository {
    
    private UsuarioRepository usuarioRepository;
    private MedidorRepository medidorRepository;
    private final DBDriverManager driverManager;
   
    public TieneMedidorRepositoryImpl(DBDriverManager driverManager, UsuarioRepository usuarioRepository, MedidorRepository medidorRepository) {
        this.driverManager = driverManager;
        this.usuarioRepository = usuarioRepository;
        this.medidorRepository = medidorRepository;
    }
    
    @Override
    public Usuario getActiveUsuario(Medidor medidor) {
       Usuario usuario = null;
       try {
            PreparedStatement statement = driverManager.getConnection().prepareStatement("SELECT USUARIO_RUT FROM TIENE_MEDIDOR where MEDIDOR_ID = ? AND FECHA_FINIQUITO IS NULL;");
            statement.setString(1,medidor.getId());
            ResultSet usuarioRs = statement.executeQuery();
            statement.close(); 
            return this.usuarioRepository.getActive(usuarioRs.getString("USUARIO_RUT"));
        }catch (Exception e){
            System.err.println(this.getClass()+ ": " +e.getClass().getName() + ": " + e.getMessage() );
        }
        return usuario;
     }

    @Override
    public Medidor getActiveMedidor(Usuario usuario) {
      Medidor medidor = null;
       try {
            PreparedStatement statement = driverManager.getConnection().prepareStatement("SELECT MEDIDOR_ID FROM TIENE_MEDIDOR where USUARIO_RUT = ? AND FECHA_FINIQUITO IS NULL;");
            statement.setString(1,usuario.getRut());
            ResultSet medidorRs = statement.executeQuery();
            statement.close(); 
            return this.medidorRepository.get(medidorRs.getString("MEDIDOR_ID"));
        }catch (Exception e){
            System.err.println(this.getClass()+ ": " +e.getClass().getName() + ": " + e.getMessage() );
        }
        return medidor;
    }

    @Override
    public Collection<Usuario> getAllUsuarios(Medidor medidor) {
     Collection<Usuario> usuarios = new ArrayList();
        try {
            PreparedStatement statement = this.driverManager.getConnection().prepareStatement("SELECT USUARIO_ID FROM TIENE_MEDIDOR WHERE MEDIDOR_ID = ?;");
            statement.setString(1,medidor.getId());
            
            ResultSet usuariosRs = statement.executeQuery();
            
            while (usuariosRs.next()) {
                Usuario usuario;
                if ((usuario = this.usuarioRepository.getActive(usuariosRs.getString("USUARIO_ID"))) != null) {
                    usuarios.add(usuario);
                }
            }
            statement.close();
        }catch (Exception e){
            System.err.println(this.getClass()+ ": " +e.getClass().getName() + ": " + e.getMessage() );
        }
        return usuarios;  
   }

    @Override
    public Collection<Medidor> getAllMedidores(Usuario usuario) {
      Collection<Medidor> medidores = new ArrayList();
        try {
            PreparedStatement statement = this.driverManager.getConnection().prepareStatement("SELECT MEDIDOR_ID FROM TIENE_MEDIDOR WHERE USUARIO_ID = ?;");
            statement.setInt(1,usuario.getId());
            
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
    public Usuario getMostRecentUsuario(Medidor medidor) {
    Usuario usuario = null;
       try {
            PreparedStatement statement = driverManager.getConnection().prepareStatement("SELECT TOP 1 USUARIO_ID FROM TIENE_MEDIDOR where MEDIDOR_ID = ? ;");
            statement.setString(1,medidor.getId());
            ResultSet usuarioRs = statement.executeQuery();
            statement.close(); 
            return this.usuarioRepository.getActive(usuarioRs.getString("USUARIO_ID"));
        }catch (Exception e){
            System.err.println(this.getClass()+ ": " +e.getClass().getName() + ": " + e.getMessage() );
        }
        return usuario;
     }

    @Override
    public Medidor getMostRecentMedidor(Usuario usuario) {
        Medidor medidor = null;
       try {
            PreparedStatement statement = driverManager.getConnection().prepareStatement("SELECT TOP 1 MEDIDOR_ID FROM TIENE_MEDIDOR where USUARIO_ID = ? ");
            statement.setInt(1,usuario.getId());
            ResultSet medidorRs = statement.executeQuery();
            statement.close(); 
            return this.medidorRepository.get(medidorRs.getString("MEDIDOR_ID"));
        }catch (Exception e){
            System.err.println(this.getClass()+ ": " +e.getClass().getName() + ": " + e.getMessage() );
        }
        return medidor;
     }

    @Override
    public boolean save(Usuario usuario, Medidor medidor) {
        try {
            PreparedStatement statement = this.driverManager.getConnection().prepareStatement("INSERT OR REPLACE INTO TIENE_MEDIDOR (USUARIO_ID,MEDIDOR_ID,FECHA_ADQUISICION)  VALUES (?, ? , CURRENT_DATE );");
            statement.setInt(1,usuario.getId());
            statement.setString(2,medidor.getId());
            int rowsAffected = statement.executeUpdate();
            statement.close();
            return rowsAffected>0;
        }catch (Exception e){
            System.err.println(this.getClass()+ ": " + e.getClass().getName() + ": " + e.getMessage() );
        }
       return false;
    }
    
    @Override
    public boolean save(Usuario usuario,Collection<? extends Medidor> medidores) {
        boolean allSaved = true;
        for (Medidor medidor : medidores){
            allSaved = allSaved && this.save(usuario, medidor);
        }
        return allSaved;
    }
    
    @Override
    public List<? extends Medidor> getMedidorOf(Usuario usuario, LocalDate fecha) {
        List<Medidor> medidores = new ArrayList();
        try {
            PreparedStatement statement = driverManager.getConnection().prepareStatement("SELECT MEDIDOR_ID FROM TIENE_MEDIDOR WHERE USUARIO_ID = ? AND CAST(strftime('%m',fecha_adquisicion) as integer) = ? AND CAST(strftime('%Y',fecha_adquisicion) as integer) = ?;");
            statement.setInt(1,usuario.getId());
            statement.setInt(2,fecha.getMonthValue());
            statement.setInt(3,fecha.getYear());
            
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
    public boolean removeMedidoresNotIn(Usuario usuario, Collection<? extends Medidor> medidores, LocalDate fechaActual) {
        int removedCount = 0;
        try {
            PreparedStatement statement = driverManager.getConnection().prepareStatement("DELETE FROM TIENE_MEDIDOR WHERE MEDIDOR_ID = ? AND USUARIO_ID = ? AND CAST(strftime('%m',fecha_adquisicion) as integer) = ? AND CAST(strftime('%Y',fecha_adquisicion) as integer) = ?;");
            statement.setInt(1,usuario.getId());
            statement.setInt(2,fecha.getMonthValue());
            statement.setInt(3,fecha.getYear());
            
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
        return (removedCount>0);   
    }
}
