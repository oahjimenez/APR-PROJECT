/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aguapotablerural.dao.impl;

import aguapotablerural.dao.contract.UsuarioRepository;
import aguapotablerural.database.contract.DBDriverManager;
import aguapotablerural.database.impl.SqliteDriverManager;
import aguapotablerural.model.Usuario;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author carlo
 */
public class UsuarioRepositoryImpl implements UsuarioRepository{

    protected DBDriverManager driverManager;

    public UsuarioRepositoryImpl(SqliteDriverManager driverManager) {
        this.driverManager = driverManager;
    }
    
    @Override
    public Usuario get(String rut) {
        
       Usuario usuario = null;
       try {
            PreparedStatement statement = driverManager.getConnection().prepareStatement("SELECT * FROM USUARIO where rut = ? AND FECHA_RETIRO IS NULL;");
            statement.setString(1,rut);
            ResultSet usuarioRs = statement.executeQuery();

            String nombre = usuarioRs.getString("nombre");
            String direccion = usuarioRs.getString("direccion");
            String telefono = usuarioRs.getString("telefono");
            Date fechaRegistro = usuarioRs.getDate("fecha_registro");
            Date fechaRetiro = usuarioRs.getDate("fecha_retiro");
            usuario = new Usuario();
            usuario.setRut(rut);
            usuario.setNombre(nombre);
            usuario.setDireccion(direccion);
            usuario.setTelefono(telefono);
            usuario.setFechaRegistro(fechaRegistro);
            usuario.setFechaRetiro(fechaRetiro);
            statement.close();
        }catch (Exception e){
            System.err.println(this.getClass()+ ": " +e.getClass().getName() + ": " + e.getMessage() );
        }
        return usuario;
    }

    @Override
    public boolean save(Usuario usuario) {
      try {
            PreparedStatement statement = this.driverManager.getConnection().prepareStatement("INSERT OR REPLACE INTO USUARIO (rut,nombre,direccion,telefono,fecha_registro) VALUES (?, ? , ?, ? , CURRENT_DATE );");
            statement.setString(1,usuario.getRut());
            statement.setString(2,usuario.getNombre());
            statement.setString(3,usuario.getDireccion());
            statement.setString(4,usuario.getTelefono());
            int rowsAffected = statement.executeUpdate();
            statement.close();
            return rowsAffected>0;
        }catch (Exception e){
            System.err.println(this.getClass()+ ": " + e.getClass().getName() + ": " + e.getMessage() );
        }  
        return false;
    }

    @Override
    public boolean delete(Usuario usuario) {
       try {
            PreparedStatement statement = this.driverManager.getConnection().prepareStatement("UPDATE USUARIO SET FECHA_FINIQUITO = CURRENT_DATE WHERE rut = ?;");
            statement.setString(1,usuario.getRut());
            statement.executeUpdate();
            int rowsAffected = statement.executeUpdate();
            statement.close();
            return rowsAffected>0;
        }catch (Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
       return false;
    }

    @Override
    public Collection<Usuario> getAllUsuarios() {
        List<Usuario> usuarios = new LinkedList();
        try {
            PreparedStatement statement = this.driverManager.getConnection().prepareStatement("SELECT * FROM USUARIO;");
            ResultSet usuariosRs = statement.executeQuery();
            
            while (usuariosRs.next()) {
                String rut = usuariosRs.getString("rut");
                String nombre = usuariosRs.getString("nombre");
                String direccion = usuariosRs.getString("direccion");
                String telefono = usuariosRs.getString("telefono");
                Usuario usuario = new Usuario();
                usuario.setRut(rut);
                usuario.setNombre(nombre);
                usuario.setDireccion(direccion);
                usuario.setTelefono(telefono);
                usuarios.add(usuario);
            }
            statement.close();
        }catch (Exception e){
            System.err.println(this.getClass()+ ": " +e.getClass().getName() + ": " + e.getMessage() );
        }
        return usuarios;  
    }

    @Override
    public Collection<Usuario> getActiveUsuarios() {
       List<Usuario> usuarios = new LinkedList();
        try {
            PreparedStatement statement = this.driverManager.getConnection().prepareStatement("SELECT * FROM USUARIO WHERE FECHA_RETIRO IS NULL;");
            ResultSet usuariosRs = statement.executeQuery();
            
            while (usuariosRs.next()) {
                String rut = usuariosRs.getString("rut");
                String nombre = usuariosRs.getString("nombre");
                String direccion = usuariosRs.getString("direccion");
                String telefono = usuariosRs.getString("telefono");
                Usuario usuario = new Usuario();
                usuario.setRut(rut);
                usuario.setNombre(nombre);
                usuario.setDireccion(direccion);
                usuario.setTelefono(telefono);
                usuarios.add(usuario);
            }
            statement.close();
        }catch (Exception e){
            System.err.println(this.getClass()+ ": " +e.getClass().getName() + ": " + e.getMessage() );
        }
        return usuarios;
    }
    
}
