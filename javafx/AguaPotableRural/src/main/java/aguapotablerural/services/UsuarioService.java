/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.aguapotablerural.services;

import main.java.aguapotablerural.dao.impl.UsuarioRepositoryImpl;
import main.java.aguapotablerural.dao.repository.UsuarioRepository;
import main.java.aguapotablerural.database.contract.DBDriverManager;
import main.java.aguapotablerural.database.impl.SqliteDriverManager;
import main.java.aguapotablerural.model.Usuario;

/**
 *
 * @author cmardones
 */
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    
    public UsuarioService() {
        DBDriverManager driver = new SqliteDriverManager();
        this.usuarioRepository = new UsuarioRepositoryImpl(driver);
    }
    
    public Usuario getUsuario(String rut){
        return this.usuarioRepository.getActive(rut);
    }
    
    public boolean existsActiveUsuario(String rut){
        Usuario usuario = this.usuarioRepository.getActive(rut);
        return (usuario!=null) && (usuario.getFechaRetiro()!=null);
    }
    
    public boolean crearUsuario(Usuario usuario) {
        return this.usuarioRepository.create(usuario);
    }
    
}
