/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.aguapotablerural.dao.impl;

import java.sql.Date;
import main.java.aguapotablerural.dao.repository.UsuarioRepository;
import main.java.aguapotablerural.database.contract.DBDriverManager;
import main.java.aguapotablerural.model.Usuario;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
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
    public Usuario getActive(String rut) {
        
        Usuario usuario = null;
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");

       try {
            PreparedStatement statement = driverManager.getConnection().prepareStatement("SELECT ID,NOMBRES,APELLIDOS,DIRECCION,TELEFONO,FECHA_REGISTRO,FECHA_RETIRO FROM USUARIO where rut = ? AND FECHA_RETIRO IS NULL;");
            statement.setString(1,rut);
            ResultSet usuarioRs = statement.executeQuery();
            if (usuarioRs.next()) {
                int id = usuarioRs.getInt("id");
                String nombres = usuarioRs.getString("nombres");
                String apellidos = usuarioRs.getString("apellidos");
                String direccion = usuarioRs.getString("direccion");
                String telefono = usuarioRs.getString("telefono");
                //TODO parsear fechas correctamente
                Date fechaRegistro = new java.sql.Date(dateformat.parse(usuarioRs.getString("fecha_registro")).getTime());
                String fechaRetiroStr = usuarioRs.getString("fecha_retiro");
                Date fechaRetiro = (fechaRetiroStr==null)? null : new java.sql.Date(dateformat.parse(fechaRetiroStr).getTime());
                System.out.println("fechaRegistro:"+fechaRegistro+" fecharetiro:"+fechaRetiro);
                usuario = new Usuario();
                usuario.setId(id);
                usuario.setRut(rut);
                usuario.setNombres(nombres);
                usuario.setApellidos(apellidos);
                usuario.setDireccion(direccion);
                usuario.setTelefono(telefono);
                usuario.setFechaRegistro(fechaRegistro);
                usuario.setFechaRetiro(fechaRetiro);
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
            PreparedStatement statement = this.driverManager.getConnection().prepareStatement("UPDATE USUARIO SET rut=?,nombres=?,apellidos=?,direccion=?,telefono=?,fecha_registro=CURRENT_DATE WHERE id=?;");
            statement.setString(1,usuario.getRut());
            statement.setString(2,usuario.getNombres());
            statement.setString(3,usuario.getApellidos());
            statement.setString(4,usuario.getDireccion());
            statement.setString(5,usuario.getTelefono());
            statement.setInt(6,usuario.getId());
            int rowsAffected = statement.executeUpdate();
            statement.close();
            
            if (rowsAffected==1){
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
    public boolean create(Usuario usuario) {
      boolean guardadoConExito = false;
      try {
            PreparedStatement statement = this.driverManager.getConnection().prepareStatement("INSERT INTO USUARIO (rut,nombres,apellidos,direccion,telefono,fecha_registro) VALUES (?, ? , ?, ?, ? , CURRENT_DATE );");
            statement.setString(1,usuario.getRut());
            statement.setString(2,usuario.getNombres());
            statement.setString(3,usuario.getApellidos());
            statement.setString(4,usuario.getDireccion());
            statement.setString(5,usuario.getTelefono());
            statement.executeUpdate();
            statement.close();
            
            guardadoConExito = this.medidorRepository.saveAll(usuario.getMedidoresObservable()) &&
            this.tieneMedidorRepository.save(usuario,usuario.getMedidoresObservable());

        }catch (Exception e){
           System.err.println("Error al guardar mas de un registro encontrado con id"+usuario.getId());
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
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
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
                Date fechaRegistro = new java.sql.Date(dateformat.parse(usuariosRs.getString("fecha_registro")).getTime());
                String fechaRetiroStr = usuariosRs.getString("fecha_retiro");
                Date fechaRetiro = (fechaRetiroStr==null)? null : new java.sql.Date(dateformat.parse(fechaRetiroStr).getTime());
                System.out.println("fechaRegistro:"+fechaRegistro+" fecharetiro:"+fechaRetiro);
                Usuario usuario = new Usuario();
                usuario.setId(id);
                usuario.setRut(rut);
                usuario.setNombres(nombre);
                usuario.setApellidos(apellidos);
                usuario.setDireccion(direccion);
                usuario.setTelefono(telefono);
                usuario.setFechaRegistro(fechaRegistro);
                usuario.setFechaRetiro(fechaRetiro);
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
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            PreparedStatement statement = this.driverManager.getConnection().prepareStatement("SELECT ID,RUT,NOMBRES,APELLIDOS,DIRECCION,TELEFONO,FECHA_REGISTRO,FECHA_RETIRO FROM USUARIO WHERE FECHA_RETIRO IS NULL;");
            ResultSet usuariosRs = statement.executeQuery();
            
            while (usuariosRs.next()) {
                int id = usuariosRs.getInt("id");
                String rut = usuariosRs.getString("rut");
                String nombre = usuariosRs.getString("nombres");
                String apellidos = usuariosRs.getString("apellidos");
                String direccion = usuariosRs.getString("direccion");
                String telefono = usuariosRs.getString("telefono");
                Date fechaRegistro = new java.sql.Date(dateformat.parse(usuariosRs.getString("fecha_registro")).getTime());
                String fechaRetiroStr = usuariosRs.getString("fecha_retiro");
                Date fechaRetiro = (fechaRetiroStr==null)? null : new java.sql.Date(dateformat.parse(fechaRetiroStr).getTime());
                System.out.println("fechaRegistro:"+fechaRegistro+" fecharetiro:"+fechaRetiro);
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
