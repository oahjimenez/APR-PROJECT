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
            super.save();
            PreparedStatement statement = this.getDbconnection().getConnection().prepareStatement("INSERT OR REPLACE INTO ADMINISTRADOR (rut,password) " +
                     "  VALUES (?, ?);");
            statement.setString(1,this.getRut());
            statement.setString(2,this.password);
            statement.executeUpdate();
            statement.close();
        }catch (Exception e){
            System.err.println(this.getClass()+ ": " + e.getClass().getName() + ": " + e.getMessage() );
        }
    }
    
    @Override
    public void delete(){
       try {
            super.delete();
        }catch (Exception e){
            System.err.println(this.getClass()+ ": " + e.getClass().getName() + ": " + e.getMessage() );
        }  
 
    }
}
