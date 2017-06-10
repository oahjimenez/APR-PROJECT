/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aguapotablerural.dao.impl;

import aguapotablerural.dao.contract.MedidorRepository;
import aguapotablerural.database.contract.DBDriverManager;
import aguapotablerural.database.impl.SqliteDriverManager;
import aguapotablerural.model.Medidor;
import java.sql.PreparedStatement;
import java.util.Collection;

/**
 *
 * @author carlo
 */
public class MedidorRepositoryImpl implements MedidorRepository {

     private DBDriverManager driverManager;

    public MedidorRepositoryImpl(SqliteDriverManager driverManager) {
        this.driverManager = driverManager;
    }
    
    @Override
    public Medidor get(String id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean save(Medidor medidor) {
       try {
            PreparedStatement statement = this.driverManager.getConnection().prepareStatement("INSERT OR REPLACE INTO MEDIDOR (id,nombre,fecha_registro,fecha_retiro) " +
                     "  VALUES (?, ? , CURRENT_DATE, ?  );");
            statement.setString(1,medidor.getId());
            statement.setString(2,medidor.getNombre());
            statement.setDate(3,medidor.getFechaRetiro());
            int rowsAffected = statement.executeUpdate();
            statement.close();
            return rowsAffected>0;
        }catch (Exception e){
            System.err.println(this.getClass()+ ": " + e.getClass().getName() + ": " + e.getMessage() );
        }
       return false;
    }

    @Override
    public boolean delete(Medidor medidor) {
        System.out.println("Eliminando medidor en base sqlite3");
        try {
            PreparedStatement statement = this.driverManager.getConnection().prepareStatement("UPDATE MEDIDOR SET FECHA_RETIRO = CURRENT_DATE WHERE ID = ?;");
            statement.setString(1,medidor.getId());
            int rowsAffected = statement.executeUpdate();
            statement.close();
            return rowsAffected > 0;
        }catch (Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
        return false;
    }

    @Override
    public Collection<Medidor> getAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<Medidor> getAllActive() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
