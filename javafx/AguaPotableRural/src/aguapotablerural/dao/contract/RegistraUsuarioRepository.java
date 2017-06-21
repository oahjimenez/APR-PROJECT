/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aguapotablerural.dao.contract;

import aguapotablerural.model.Administrador;
import aguapotablerural.model.Usuario;
import java.sql.Date;
import java.util.Collection;

/**
 *
 * @author carlo
 */
public interface RegistraUsuarioRepository {
    
    public Collection<Usuario> getUsuarios(Administrador admin);
    public Collection<Administrador> getAdministradores(Usuario usuario);
    
    public boolean save(Administrador admin,Usuario usuario,Date fechaRegistro);
    
}
