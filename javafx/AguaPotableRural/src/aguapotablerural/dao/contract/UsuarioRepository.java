/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aguapotablerural.dao.contract;

import aguapotablerural.model.Usuario;
import java.util.Collection;
/**
 *
 * @author carlo
 */
public interface UsuarioRepository<T extends Usuario> {
    
    public T get(String rut);
    public boolean save(T usuario);
    public boolean delete(T usuario);
    public Collection<T> getAllUsuarios();
    public Collection<T> getActiveUsuarios();
    
}
