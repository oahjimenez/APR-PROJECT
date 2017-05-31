/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aguapotablerural.model;

import aguapotablerural.database.SQLiteJDBCDriverConnection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author carlo
 */
public class UsuarioManager {
    
    private SQLiteJDBCDriverConnection dbconnection;

    public UsuarioManager(SQLiteJDBCDriverConnection dbconnection) {
        this.dbconnection = dbconnection;
    }
    
    public Usuario getUsuario(String rut ){
       Usuario usuario = null;
       try {
            PreparedStatement statement = dbconnection.getConnection().prepareStatement("SELECT * FROM USUARIO where rut = ?;");
            statement.setString(1,rut);
            ResultSet usuarioRs = statement.executeQuery();

            String nombre = usuarioRs.getString("nombre");
            String direccion = usuarioRs.getString("direccion");
            String telefono = usuarioRs.getString("telefono");
            Date fechaRegistro = usuarioRs.getDate("fecha_registro");
            Date fechaRetiro = usuarioRs.getDate("fecha_retiro");
            usuario = new Usuario(this.dbconnection).setRut(rut).setNombre(nombre).setDireccion(direccion).setTelefono(telefono).setFechaRegistro(fechaRegistro).setFechaRetiro(fechaRetiro);
            statement.close();
        }catch (Exception e){
            System.err.println(this.getClass()+ ": " +e.getClass().getName() + ": " + e.getMessage() );
        }
        return usuario;
    }
    
    public List<Usuario> getAll(){
        List<Usuario> usuarios = new LinkedList();
        try {
            PreparedStatement statement = dbconnection.getConnection().prepareStatement("SELECT * FROM USUARIO;");
            ResultSet usuariosRs = statement.executeQuery();
            
            while (usuariosRs.next()) {
                String rut = usuariosRs.getString("rut");
                String nombre = usuariosRs.getString("nombre");
                String direccion = usuariosRs.getString("direccion");
                String telefono = usuariosRs.getString("telefono");
                Usuario usuario = new Usuario(this.dbconnection).setRut(rut).setNombre(nombre).setDireccion(direccion).setTelefono(telefono);
                usuarios.add(usuario);
            }
            statement.close();
        }catch (Exception e){
            System.err.println(this.getClass()+ ": " +e.getClass().getName() + ": " + e.getMessage() );
        }
        return usuarios;
    }
    
    public List<Usuario> getActive(){
        List<Usuario> usuarios = new LinkedList();
        try {
            PreparedStatement statement = dbconnection.getConnection().prepareStatement("SELECT * FROM USUARIO WHERE FECHA_RETIRO IS NULL;");
            ResultSet usuariosRs = statement.executeQuery();
            
            while (usuariosRs.next()) {
                String rut = usuariosRs.getString("rut");
                String nombre = usuariosRs.getString("nombre");
                String direccion = usuariosRs.getString("direccion");
                String telefono = usuariosRs.getString("telefono");
                Usuario usuario = new Usuario(this.dbconnection).setRut(rut).setNombre(nombre).setDireccion(direccion).setTelefono(telefono);
                usuarios.add(usuario);
            }
            statement.close();
        }catch (Exception e){
            System.err.println(this.getClass()+ ": " +e.getClass().getName() + ": " + e.getMessage() );
        }
        return usuarios;
    }
    
    
    public void delete(Usuario usuario){
       usuario.delete();
    }
    
    public void save(Usuario usuario){
        usuario.save();
    }
}
