/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aguapotablerural.dao.contract;

import aguapotablerural.model.Administrador;
import aguapotablerural.model.Medidor;
import java.sql.Date;
import java.util.Collection;

/**
 *
 * @author carlo
 */
public interface RegistraMedidorRepository {
    
    public Collection<Administrador> getAdministradores(Medidor medidor);
    public Collection<Medidor> getMedidores(Administrador administrador);
    
    public boolean save(Administrador admin,Medidor medidor,Date fechaRegistro);

}
