/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.aguapotablerural.model;

import main.java.aguapotablerural.database.impl.SqliteDriverManager;
import java.sql.PreparedStatement;

/**
 *
 * @author carlo
 */
public class Administrador extends Usuario {
    
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public void copyFromUsuario(Usuario usuario) {
        this.setRut(usuario.getRut());
        this.setNombre(usuario.getNombre());
        this.setDireccion(usuario.getDireccion());
        this.setTelefono(usuario.getTelefono());
        this.setFechaRegistro(usuario.getFechaRegistro());
        this.setFechaRetiro(usuario.getFechaRegistro());
    }
    
    @Override
    public String toString() {
        return super.toString();
    }
    
}
