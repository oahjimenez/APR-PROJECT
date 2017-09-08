/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.aguapotablerural.dao.impl;

import main.java.aguapotablerural.dao.repository.AdministradorRepository;
import main.java.aguapotablerural.dao.repository.UsuarioRepository;
import main.java.aguapotablerural.database.impl.SqliteDriverManager;
import main.java.aguapotablerural.model.Administrador;
import main.java.aguapotablerural.model.Usuario;
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
public class AdministradorRepositoryImpl extends UsuarioRepositoryImpl implements AdministradorRepository {
    
    public AdministradorRepositoryImpl(SqliteDriverManager driverManager) {
        super(driverManager);
    }

    @Override
    public Administrador get(String rut) {
        Administrador administrador = null;
        Usuario usuario = super.get(rut);
        try {
            PreparedStatement statement = driverManager.getConnection().prepareStatement("SELECT * FROM ADMINISTRADOR where rut = ?;");
            statement.setString(1,rut);
            ResultSet administradorRs = statement.executeQuery();

            if (administradorRs.next()) {
                String password = administradorRs.getString("password");
                administrador = new Administrador();
                administrador.copyFromUsuario(usuario);
            }
            statement.close();
        }catch (Exception e){
            System.err.println(this.getClass()+ ": " +e.getClass().getName() + ": " + e.getMessage() );
        }
        return administrador;
    }

    @Override
    public boolean save(Administrador admin) {
        boolean saved = super.save(admin);
        if (!saved) {
            return !saved;
        }
        try {
            PreparedStatement statement = this.driverManager.getConnection().prepareStatement("INSERT OR REPLACE INTO ADMINISTRADOR (ID,password)  VALUES (?, ?);");
            statement.setInt(1,admin.getId());
            statement.setString(2,admin.getPassword());
            int rowsAffected = statement.executeUpdate();
            statement.close();
            return rowsAffected!=1;
        }catch (Exception e){
            System.err.println(this.getClass()+ ": " + e.getClass().getName() + ": " + e.getMessage() );
        }
        return false;
    }

    @Override
    public boolean delete(Administrador admin) {
       return super.delete(admin);
    }

    public Collection<Administrador> getAllAdmins() {
        Collection<Usuario> usuarios = super.getAllUsuarios();
        Collection<Administrador> administradores = new LinkedList();
        for (Usuario usuario: usuarios) {
            Administrador admin;
            if ((admin = this.get(usuario.getRut())) != null) {
                administradores.add(admin);
            }
        }
        return administradores;                        
    }

    public Collection<Administrador> getActiveAdmins() {
       Collection<Usuario> usuarios = super.getActiveUsuarios();
        Collection<Administrador> administradores = new LinkedList();
        for (Usuario usuario: usuarios) {
            Administrador admin;
            if ((admin = this.get(usuario.getRut())) != null) {
                administradores.add(admin);
            }
        }
        return administradores;
    }
}

