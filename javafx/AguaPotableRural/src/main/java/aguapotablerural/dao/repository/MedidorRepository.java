/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.aguapotablerural.dao.repository;

import main.java.aguapotablerural.model.Medidor;
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
    public boolean saveAll(Collection<? extends Medidor> medidores);
    
}
