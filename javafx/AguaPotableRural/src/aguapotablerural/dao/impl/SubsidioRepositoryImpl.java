/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aguapotablerural.dao.impl;

import aguapotablerural.dao.contract.MedidorRepository;
import aguapotablerural.dao.contract.SubsidioRepository;
import aguapotablerural.dao.contract.UsuarioRepository;
import aguapotablerural.database.contract.DBDriverManager;
import aguapotablerural.model.Medidor;
import aguapotablerural.model.Subsidio;
import aguapotablerural.model.Usuario;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;

/**
 *
 * @author carlo
 */
public class SubsidioRepositoryImpl implements SubsidioRepository {

    private final UsuarioRepository usuarioRepository;
    private final DBDriverManager driverManager;
    private final MedidorRepository medidorRepository;
   
    public SubsidioRepositoryImpl(DBDriverManager driverManager, UsuarioRepository usuarioRepository, MedidorRepository medidorRepository) {
        this.driverManager = driverManager;
        this.usuarioRepository = usuarioRepository;
        this.medidorRepository = medidorRepository;
    }
   
    
    @Override
    public Subsidio getSubsidio(Usuario usuario, Date fecha) {
      Subsidio subsidio = null;
       try {
            PreparedStatement statement = driverManager.getConnection().prepareStatement("SELECT MEDIDOR_ID,PORCENTAJE_SUBSIDIO,TOPE FROM SUBSIDIO where USUARIO_RUT = ?  AND FECHA = ?;");
            statement.setString(1,usuario.getRut());
            statement.setDate(2,fecha);
            ResultSet subsidioRs = statement.executeQuery();
            subsidio = new Subsidio();
            subsidio.setUsuario(usuario);
            subsidio.setMedidor(this.medidorRepository.get(subsidioRs.getString("MEDIDOR_ID")));
            subsidio.setPorcentaje(subsidioRs.getDouble("PORCENTAJE_SUBSIDIO"));
            subsidio.setTope(subsidioRs.getDouble("TOPE"));
            statement.close();
            return subsidio;
        }catch (Exception e){
            System.err.println(this.getClass()+ ": " +e.getClass().getName() + ": " + e.getMessage() );
        }
        return subsidio;
    }

    @Override
    public Collection<Subsidio> getAllSubsidios(Usuario usuario) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Subsidio getMostRecentSubsidio(Usuario usuario) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Subsidio getSubsidioActual(Usuario usuario) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Subsidio getSubsidioActual(Medidor medidor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<Subsidio> getAllSubsidios(Medidor medidor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Subsidio getMostRecentSubsidio(Medidor usuario) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}