/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aguapotablerural.dao.impl;

import aguapotablerural.dao.contract.MedidorRepository;
import aguapotablerural.dao.contract.TieneMedidorRepository;
import aguapotablerural.dao.contract.UsuarioRepository;
import aguapotablerural.database.contract.DBDriverManager;
import aguapotablerural.model.Medidor;
import aguapotablerural.model.Usuario;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;

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
            return this.usuarioRepository.get(usuarioRs.getString("USUARIO_RUT"));
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
    public Collection<Usuario> getHistoryOfUsuarios(Medidor medidor) {
     Collection<Usuario> usuarios = new ArrayList();
        try {
            PreparedStatement statement = this.driverManager.getConnection().prepareStatement("SELECT USUARIO_RUT FROM TIENE_MEDIDOR WHERE MEDIDOR_ID = ?;");
            statement.setString(1,medidor.getId());
            
            ResultSet usuariosRs = statement.executeQuery();
            
            while (usuariosRs.next()) {
                Usuario usuario;
                if ((usuario = this.usuarioRepository.get(usuariosRs.getString("USUARIO_RUT"))) != null) {
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
    public Collection<Medidor> getHistoryOfMedidores(Usuario usuario) {
      Collection<Medidor> medidores = new ArrayList();
        try {
            PreparedStatement statement = this.driverManager.getConnection().prepareStatement("SELECT MEDIDOR_ID FROM TIENE_MEDIDOR WHERE USUARIO_RUT = ?;");
            statement.setString(1,usuario.getRut());
            
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
            PreparedStatement statement = driverManager.getConnection().prepareStatement("SELECT TOP 1 USUARIO_RUT FROM TIENE_MEDIDOR where MEDIDOR_ID = ? ;");
            statement.setString(1,medidor.getId());
            ResultSet usuarioRs = statement.executeQuery();
            statement.close(); 
            return this.usuarioRepository.get(usuarioRs.getString("USUARIO_RUT"));
        }catch (Exception e){
            System.err.println(this.getClass()+ ": " +e.getClass().getName() + ": " + e.getMessage() );
        }
        return usuario;
     }

    @Override
    public Medidor getMostRecentMedidor(Usuario usuario) {
        Medidor medidor = null;
       try {
            PreparedStatement statement = driverManager.getConnection().prepareStatement("SELECT TOP 1 MEDIDOR_ID FROM TIENE_MEDIDOR where USUARIO_RUT = ? ");
            statement.setString(1,usuario.getRut());
            ResultSet medidorRs = statement.executeQuery();
            statement.close(); 
            return this.medidorRepository.get(medidorRs.getString("MEDIDOR_ID"));
        }catch (Exception e){
            System.err.println(this.getClass()+ ": " +e.getClass().getName() + ": " + e.getMessage() );
        }
        return medidor;
     }
    
}
