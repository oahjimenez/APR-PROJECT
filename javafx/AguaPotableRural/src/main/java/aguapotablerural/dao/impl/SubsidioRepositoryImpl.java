/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.aguapotablerural.dao.impl;

import main.java.aguapotablerural.dao.repository.MedidorRepository;
import main.java.aguapotablerural.dao.repository.SubsidioRepository;
import main.java.aguapotablerural.dao.repository.UsuarioRepository;
import main.java.aguapotablerural.database.contract.DBDriverManager;
import main.java.aguapotablerural.model.Medidor;
import main.java.aguapotablerural.model.Subsidio;
import main.java.aguapotablerural.model.Usuario;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

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
       List<Subsidio> subsidios = new LinkedList();
        try {
            PreparedStatement statement = this.driverManager.getConnection().prepareStatement("SELECT * FROM SUBSIDIO WHERE USUARIO_RUT = ?;");
            statement.setString(1,usuario.getRut());
            ResultSet subsidiosRs = statement.executeQuery();
            
            while (subsidiosRs.next()) {
                String id = subsidiosRs.getString("ID");
                String medidor_id = subsidiosRs.getString("MEDIDOR_ID");
                Double porcentaje_subsidio = subsidiosRs.getDouble("PORCENTAJE_SUBSIDIO");
                Double tope = subsidiosRs.getDouble("TOPE");
                Date fecha = subsidiosRs.getDate("FECHA");
                Subsidio subsidio = new Subsidio();
                subsidio.setId(id);
                subsidio.setUsuario(usuario);
                subsidio.setMedidor(this.medidorRepository.get(medidor_id));
                subsidio.setPorcentaje(porcentaje_subsidio);
                subsidio.setTope(tope);
                subsidio.setFecha(fecha);
                subsidios.add(subsidio);
            }
            statement.close();
        }catch (Exception e){
            System.err.println(this.getClass()+ ": " +e.getClass().getName() + ": " + e.getMessage() );
        }
        return subsidios;
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
       List<Subsidio> subsidios = new LinkedList();
        try {
            PreparedStatement statement = this.driverManager.getConnection().prepareStatement("SELECT * FROM SUBSIDIO WHERE MEDIDOR_ID = ?;");
            statement.setString(1,medidor.getId());
            ResultSet subsidiosRs = statement.executeQuery();
            
            while (subsidiosRs.next()) {
                String id = subsidiosRs.getString("ID");
                String usuario_rut = subsidiosRs.getString("USUARIO_RUT");
                Double porcentaje_subsidio = subsidiosRs.getDouble("PORCENTAJE_SUBSIDIO");
                Double tope = subsidiosRs.getDouble("TOPE");
                Date fecha = subsidiosRs.getDate("FECHA");
                Subsidio subsidio = new Subsidio();
                subsidio.setId(id);
                subsidio.setUsuario(this.usuarioRepository.get(usuario_rut));
                subsidio.setMedidor(medidor);
                subsidio.setPorcentaje(porcentaje_subsidio);
                subsidio.setTope(tope);
                subsidio.setFecha(fecha);
                subsidios.add(subsidio);
            }
            statement.close();
        }catch (Exception e){
            System.err.println(this.getClass()+ ": " +e.getClass().getName() + ": " + e.getMessage() );
        }
        return subsidios;  
    }

    @Override
    public Subsidio getMostRecentSubsidio(Medidor usuario) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
