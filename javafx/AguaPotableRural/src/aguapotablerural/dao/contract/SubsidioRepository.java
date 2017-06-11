/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aguapotablerural.dao.contract;

import aguapotablerural.model.Medidor;
import aguapotablerural.model.Subsidio;
import aguapotablerural.model.Usuario;
import java.sql.Date;
import java.util.Collection;

/**
 *
 * @author carlo
 */
public interface SubsidioRepository {
    
    public Subsidio getSubsidio(Usuario usuario,Date fecha);
    public Subsidio getSubsidioActual(Usuario usuario);
    public Subsidio getSubsidioActual(Medidor medidor);
    public Collection<Subsidio> getAllSubsidios(Usuario usuario);
    public Collection<Subsidio> getAllSubsidios(Medidor medidor);
    public Subsidio getMostRecentSubsidio(Usuario usuario);
    public Subsidio getMostRecentSubsidio(Medidor usuario);
}
