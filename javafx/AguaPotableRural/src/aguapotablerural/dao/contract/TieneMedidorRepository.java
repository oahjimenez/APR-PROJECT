/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aguapotablerural.dao.contract;

import aguapotablerural.model.Medidor;
import aguapotablerural.model.Usuario;
import java.util.Collection;

/**
 *
 * @author carlo
 */
public interface TieneMedidorRepository {
    
    
    public Usuario getActiveUsuario(Medidor medidor);
    public Medidor getActiveMedidor(Usuario usuario);
    public Collection<Usuario> getAllUsuarios(Medidor medidor);
    public Collection<Medidor> getAllMedidores(Usuario usuario);
    
    public Usuario getMostRecentUsuario(Medidor medidor);
    public Medidor getMostRecentMedidor(Usuario usuario);
    
}
