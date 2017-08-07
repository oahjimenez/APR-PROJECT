/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.aguapotablerural.dao.repository;

import main.java.aguapotablerural.model.Usuario;
import java.util.Collection;
/**
 *
 * @author carlo
 */
public interface UsuarioRepository {
    
    public Usuario get(String rut);
    public boolean save(Usuario usuario);
    public boolean delete(Usuario usuario);
    public Collection<Usuario> getAllUsuarios();
    public Collection<Usuario> getActiveUsuarios();
    
}
