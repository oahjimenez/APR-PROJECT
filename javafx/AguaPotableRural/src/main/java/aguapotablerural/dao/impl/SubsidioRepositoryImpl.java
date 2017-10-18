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
import java.time.LocalDate;
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
    public Subsidio getSubsidio(Usuario usuario, LocalDate fecha) {
      Subsidio subsidio = null;
       try {
            PreparedStatement statement = driverManager.getConnection().prepareStatement("SELECT ID,USUARIO_ID,PORCENTAJE_SUBSIDIO,TOPE FROM SUBSIDIO WHERE USUARIO_ID = ?  AND CAST(strftime('%m',FECHA) as integer) = ? AND CAST(strftime('%Y',FECHA) as integer) = ?;");
            statement.setInt(1,usuario.getId());
            statement.setInt(2,fecha.getMonthValue());
            statement.setInt(3,fecha.getYear());
            ResultSet subsidioRs = statement.executeQuery();
            
            if (subsidioRs.next()) {
                subsidio = new Subsidio();
                subsidio.setUsuario(usuario);
                subsidio.setId(subsidioRs.getInt("ID"));
                subsidio.setPorcentaje(subsidioRs.getDouble("PORCENTAJE_SUBSIDIO"));
                subsidio.setTope(subsidioRs.getDouble("TOPE"));
            }
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
                Subsidio subsidio = new Subsidio();
                subsidio.setId(subsidiosRs.getInt("ID"));
                subsidio.setUsuario(usuario);
                subsidio.setPorcentaje(subsidiosRs.getDouble("PORCENTAJE_SUBSIDIO"));
                subsidio.setTope(subsidiosRs.getDouble("TOPE"));
                subsidio.setFecha(subsidiosRs.getDate("FECHA"));
                subsidios.add(subsidio);
            }
            statement.close();
        }catch (Exception e){
            System.err.println(this.getClass()+ ": " +e.getClass().getName() + ": " + e.getMessage() );
        }
        return subsidios;
    }
    
}
