/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aguapotablerural.dao.contract;

import aguapotablerural.model.Medidor;
import java.util.Collection;

/**
 *
 * @author carlo
 */
public interface MedidorRepository {
   
    public Medidor get(String id);
    public boolean save(Medidor medidor);
    public boolean delete(Medidor medidor);
    public Collection<Medidor> getAll();
    public Collection<Medidor> getAllActive();
    
    
}
