/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.aguapotablerural.dao.impl;

import main.java.aguapotablerural.dao.contract.UsuarioRepository;
import main.java.aguapotablerural.database.contract.DBDriverManager;
import main.java.aguapotablerural.database.impl.SqliteDriverManager;
import main.java.aguapotablerural.model.Administrador;
import main.java.aguapotablerural.model.Usuario;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import main.java.aguapotablerural.dao.contract.MedidorRepository;
import main.java.aguapotablerural.dao.contract.TieneMedidorRepository;

/**
 *
 * @author carlo
 */
public class UsuarioRepositoryImpl implements UsuarioRepository{

    protected DBDriverManager driverManager;
    
    private MedidorRepository medidorRepository;
    private TieneMedidorRepository tieneMedidorRepository;

    public UsuarioRepositoryImpl() {}
    
    public UsuarioRepositoryImpl(DBDriverManager driverManager) {
        this.driverManager = driverManager;
        this.medidorRepository = new MedidorRepositoryImpl(driverManager);
        this.tieneMedidorRepository = new TieneMedidorRepositoryImpl(driverManager,this,this.medidorRepository);
    }
    
    @Override
    public Usuario get(String rut) {
        
       Usuario usuario = null;
       try {
            PreparedStatement statement = driverManager.getConnection().prepareStatement("SELECT * FROM USUARIO where rut = ? AND FECHA_RETIRO IS NULL;");
            statement.setString(1,rut);
            ResultSet usuarioRs = statement.executeQuery();

            String nombres = usuarioRs.getString("nombres");
            String apellidos = usuarioRs.getString("apellidos");
            String direccion = usuarioRs.getString("direccion");
            String telefono = usuarioRs.getString("telefono");
            Date fechaRegistro = usuarioRs.getDate("fecha_registro");
            Date fechaRetiro = usuarioRs.getDate("fecha_retiro");
            usuario = new Usuario();
            usuario.setRut(rut);
            usuario.setNombres(nombres);
            usuario.setApellidos(apellidos);
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
            PreparedStatement statement = this.driverManager.getConnection().prepareStatement("INSERT OR REPLACE INTO USUARIO (rut,nombres,apellidos,direccion,telefono,fecha_registro) VALUES (?, ? , ?, ?, ? , CURRENT_DATE );");
            statement.setString(1,usuario.getRut());
            statement.setString(2,usuario.getNombres());
            statement.setString(3,usuario.getApellidos());
            statement.setString(4,usuario.getDireccion());
            statement.setString(5,usuario.getTelefono());
            int rowsAffected = statement.executeUpdate();
            statement.close();
            
            if (!(rowsAffected>0)){
                return false;
            }
            return this.medidorRepository.saveAll(usuario.getMedidoresObservable()) &&
            this.tieneMedidorRepository.save(usuario,usuario.getMedidoresObservable());
        }catch (Exception e){
            System.err.println(this.getClass()+ ": " + e.getClass().getName() + ": " + e.getMessage() );
        }  
        return false;
    }

    @Override
    public boolean delete(Usuario usuario) {
       try {
            PreparedStatement statement = this.driverManager.getConnection().prepareStatement("UPDATE USUARIO SET FECHA_RETIRO = CURRENT_DATE WHERE rut = ?;");
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
                String nombre = usuariosRs.getString("nombres");
                String apellidos = usuariosRs.getString("apellidos");
                String direccion = usuariosRs.getString("direccion");
                String telefono = usuariosRs.getString("telefono");
                Usuario usuario = new Usuario();
                usuario.setRut(rut);
                usuario.setNombres(nombre);
                usuario.setApellidos(apellidos);
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
                String nombre = usuariosRs.getString("nombres");
                String apellidos = usuariosRs.getString("apellidos");
                String direccion = usuariosRs.getString("direccion");
                String telefono = usuariosRs.getString("telefono");
                Usuario usuario = new Usuario();
                usuario.setRut(rut);
                usuario.setNombres(nombre);
                usuario.setApellidos(apellidos);
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
