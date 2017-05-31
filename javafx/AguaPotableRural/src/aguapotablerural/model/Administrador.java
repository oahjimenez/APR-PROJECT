/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aguapotablerural.model;

import aguapotablerural.database.SQLiteJDBCDriverConnection;
import java.sql.PreparedStatement;

/**
 *
 * @author carlo
 */
public class Administrador extends Usuario {
    
   private String password;
    
   public Administrador(SQLiteJDBCDriverConnection dbconnection){
        super(dbconnection);
    }

    public String getPassword() {
        return password;
    }

    public Administrador setPassword(String password) {
        this.password = password;
        return this;
    }
    
    @Override
    public void save(){
        try {
            PreparedStatement statement = this.getDbconnection().getConnection().prepareStatement("INSERT OR REPLACE INTO USUARIO (rut,password,nombre,direccion,telefono,fechaRegistro,fechaRetiro) " +
                     "  VALUES (?, ?, ? , ?, ? , ? );");
            statement.setString(1,this.getRut());
            statement.setString(2,this.password);
            statement.setString(3,this.getNombre());
            statement.setString(4,this.getDireccion());
            statement.setString(5,this.getTelefono());
            statement.setDate(5,this.getFechaRegistro());
            statement.setDate(6,this.getFechaRetiro());
            statement.executeUpdate();
            statement.close();
        }catch (Exception e){
            System.err.println(this.getClass()+ ": " + e.getClass().getName() + ": " + e.getMessage() );
        }
    }
}
