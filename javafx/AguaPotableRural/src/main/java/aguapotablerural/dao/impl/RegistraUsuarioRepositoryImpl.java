/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.aguapotablerural.dao.impl;

import main.java.aguapotablerural.dao.contract.AdministradorRepository;
import main.java.aguapotablerural.dao.contract.RegistraUsuarioRepository;
import main.java.aguapotablerural.dao.contract.UsuarioRepository;
import main.java.aguapotablerural.database.contract.DBDriverManager;
import main.java.aguapotablerural.database.impl.SqliteDriverManager;
import main.java.aguapotablerural.model.Administrador;
import main.java.aguapotablerural.model.Usuario;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author carlo
 */
public class RegistraUsuarioRepositoryImpl implements RegistraUsuarioRepository {

    private UsuarioRepository usuarioRepository;
    private AdministradorRepository administradorRepository;
    private DBDriverManager driverManager;

    public RegistraUsuarioRepositoryImpl(DBDriverManager driverManager, UsuarioRepository usuarioRepository,AdministradorRepository administradorRepository) {
       this.usuarioRepository = usuarioRepository;
    }
    
    @Override
    public Collection<Usuario> getUsuarios(Administrador admin) {
        Collection<Usuario> usuarios = new ArrayList();
        try {
            PreparedStatement statement = this.driverManager.getConnection().prepareStatement("SELECT USUARIO_RUT FROM REGISTRA_USUARIO WHERE ADMINISTRADOR_RUT = ?;");
            statement.setString(1,admin.getRut());
            
            ResultSet usuariosRs = statement.executeQuery();
            
            while (usuariosRs.next()) {
                Usuario usuario;
                if ((usuario = this.usuarioRepository.get(usuariosRs.getString("usuario_rut"))) != null) {
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
    public Collection<Administrador> getAdministradores(Usuario usuario) {
         Collection<Administrador> administradores = new ArrayList();
        try {
            PreparedStatement statement = this.driverManager.getConnection().prepareStatement("SELECT ADMINISTRADOR_RUT FROM REGISTRA_USUARIO WHERE USUARIO_RUT = ?;");
            statement.setString(1,usuario.getRut());
            
            ResultSet administradoresRs = statement.executeQuery();
            
            while (administradoresRs.next()) {
                Administrador admin;
                if ((admin = this.administradorRepository.get(administradoresRs.getString("ADMINISTRADOR_RU"))) != null) {
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
    public boolean save(Administrador admin, Usuario usuario, Date fechaRegistro) {
        try {
            PreparedStatement statement = this.driverManager.getConnection().prepareStatement("INSERT OR REPLACE INTO REGISTRA_USUARIO (USUARIO_RUT,ADMINISTRADOR_RUT,FECHA_REGISTRO) VALUES (?, ? , ?);");
            statement.setString(1,usuario.getRut());
            statement.setString(2,admin.getRut());
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
