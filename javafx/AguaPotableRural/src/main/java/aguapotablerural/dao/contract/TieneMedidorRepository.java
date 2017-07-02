/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.aguapotablerural.dao.contract;

import main.java.aguapotablerural.model.Medidor;
import main.java.aguapotablerural.model.Usuario;
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
    
    public boolean save(Usuario usuario,Medidor medidor);
    public boolean save(Usuario usuario,Collection<? extends Medidor> medidores);
    
}
