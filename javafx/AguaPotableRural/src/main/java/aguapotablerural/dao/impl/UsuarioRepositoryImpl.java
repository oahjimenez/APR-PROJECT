/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.aguapotablerural.dao.impl;

import main.java.aguapotablerural.dao.repository.UsuarioRepository;
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
import main.java.aguapotablerural.dao.repository.MedidorRepository;
import main.java.aguapotablerural.dao.repository.TieneMedidorRepository;

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
            PreparedStatement statement = driverManager.getConnection().prepareStatement("SELECT ID,NOMBRES,APELLIDOS,DIRECCION,TELEFONO,FECHA_REGISTRO,FECHA_RETIRO FROM USUARIO where rut = ?;");
            statement.setString(1,rut);
            ResultSet usuarioRs = statement.executeQuery();
            if (usuarioRs.next()) {
                int id = usuarioRs.getInt("id");
                String nombres = usuarioRs.getString("nombres");
                String apellidos = usuarioRs.getString("apellidos");
                String direccion = usuarioRs.getString("direccion");
                String telefono = usuarioRs.getString("telefono");
                //TODO parsear fechas correctamente
                //Date fechaRegistro = usuarioRs.getDate("fecha_registro");
                //Date fechaRetiro = usuarioRs.getDate("fecha_retiro");
                usuario = new Usuario();
                usuario.setId(id);
                usuario.setRut(rut);
                usuario.setNombres(nombres);
                usuario.setApellidos(apellidos);
                usuario.setDireccion(direccion);
                usuario.setTelefono(telefono);
                //usuario.setFechaRegistro(fechaRegistro);
                //usuario.setFechaRetiro(fechaRetiro);
            }
            statement.close();
        }catch (Exception e){
            System.err.println(this.getClass()+ ": " +e.getClass().getName() + ": " + e.getMessage() );
        }
        return usuario;
    }

    @Override
    public boolean save(Usuario usuario) {
      boolean guardadoConExito = false;
      try {
            PreparedStatement statement = this.driverManager.getConnection().prepareStatement("INSERT OR REPLACE INTO USUARIO (id,rut,nombres,apellidos,direccion,telefono,fecha_registro) VALUES (?,?, ? , ?, ?, ? , CURRENT_DATE );");
            statement.setInt(1,usuario.getId());
            statement.setString(2,usuario.getRut());
            statement.setString(3,usuario.getNombres());
            statement.setString(4,usuario.getApellidos());
            statement.setString(5,usuario.getDireccion());
            statement.setString(6,usuario.getTelefono());
            int rowsAffected = statement.executeUpdate();
            statement.close();
            
            if (!(rowsAffected>0)){
                guardadoConExito = this.medidorRepository.saveAll(usuario.getMedidoresObservable()) &&
                this.tieneMedidorRepository.save(usuario,usuario.getMedidoresObservable());
            } else {
               System.err.println("Error al guardar mas de un registro encontrado con id"+usuario.getId());

            }
        }catch (Exception e){
            System.err.println(this.getClass()+ ": " + e.getClass().getName() + ": " + e.getMessage() );
        }  
        return guardadoConExito;
    }

    @Override
    public boolean delete(Usuario usuario) {
       boolean borradoConExito = false;
       try {
            PreparedStatement statement = this.driverManager.getConnection().prepareStatement("UPDATE USUARIO SET FECHA_RETIRO = CURRENT_DATE WHERE ID = ?;");
            statement.setInt(1,usuario.getId());
            statement.executeUpdate();
            int rowsAffected = statement.executeUpdate();
            statement.close();
            borradoConExito = (rowsAffected==1);
        }catch (Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
       return borradoConExito;
    }

    @Override
    public Collection<Usuario> getAllUsuarios() {
        List<Usuario> usuarios = new LinkedList();
        try {
            PreparedStatement statement = this.driverManager.getConnection().prepareStatement("SELECT ID,RUT,NOMBRES,APELLIDOS,DIRECCION,TELEFONO FROM USUARIO;");
            ResultSet usuariosRs = statement.executeQuery();
            
            while (usuariosRs.next()) {
                int id = usuariosRs.getInt("id");
                String rut = usuariosRs.getString("rut");
                String nombre = usuariosRs.getString("nombres");
                String apellidos = usuariosRs.getString("apellidos");
                String direccion = usuariosRs.getString("direccion");
                String telefono = usuariosRs.getString("telefono");
                Usuario usuario = new Usuario();
                usuario.setId(id);
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
            PreparedStatement statement = this.driverManager.getConnection().prepareStatement("SELECT ID,RUT,NOMBRES,APELLIDOS,DIRECCION,TELEFONO FROM USUARIO WHERE FECHA_RETIRO IS NULL;");
            ResultSet usuariosRs = statement.executeQuery();
            
            while (usuariosRs.next()) {
                int id = usuariosRs.getInt("id");
                String rut = usuariosRs.getString("rut");
                String nombre = usuariosRs.getString("nombres");
                String apellidos = usuariosRs.getString("apellidos");
                String direccion = usuariosRs.getString("direccion");
                String telefono = usuariosRs.getString("telefono");
                Usuario usuario = new Usuario();
                usuario.setId(id);
                usuario.setRut(rut);
                usuario.setNombres(nombre);
                usuario.setApellidos(apellidos);
                usuario.setDireccion(direccion);
                usuario.setTelefono(telefono);
                usuario.addMedidores(this.tieneMedidorRepository.getAllMedidores(usuario));
                usuarios.add(usuario);
            }
            statement.close();
        }catch (Exception e){
            System.err.println(this.getClass()+ ": " +e.getClass().getName() + ": " + e.getMessage() );
        }
        return usuarios;
    }

    
}
