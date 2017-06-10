/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aguapotablerural.dao.contract;

import aguapotablerural.model.Administrador;
import java.util.Collection;

/**
 *
 * @author carlo
 */
public interface AdministradorRepository {
    
    public Administrador get(String rut);
    public boolean save(Administrador admin);
    public boolean delete(Administrador usuario);
    public Collection<Administrador> getAllAdmins();
    public Collection<Administrador> getActiveAdmins();
    
    
}
