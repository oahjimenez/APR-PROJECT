/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aguapotablerural.model;

import aguapotablerural.database.SQLiteJDBCDriverConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 *
 * @author carlo
 */
public class UsuarioManager {
    
    private SQLiteJDBCDriverConnection dbconnection;

    public UsuarioManager(SQLiteJDBCDriverConnection dbconnection) {
        this.dbconnection = dbconnection;
    }
    
    public Usuario getUsuario(String rut ){
        return null;
    }
    
    public List<Usuario> getAll(){
        Connection c = null;
        Statement statement = null;
        try {
            statement = c.createStatement();
            String sql = "SELECT * FROM USUARIO;";
            statement.execute(sql);
            statement.close();
        }catch (Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            return null;
        }
        return null;
    }
    
    public void delete(Usuario usuario){
        Connection conn = null;
         try {
            // db parameters
            String url = "jdbc:sqlite:C:/Users/carlo/Desktop/gitrepo/APR-PROJECT/sqlite3/AGUA_POTABLE_RURAL.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            
            System.out.println("Connection to SQLite has been established.");
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
      //  usuario.delete();
    }
    
    public void save(Usuario usuario){
        usuario.save();
    }
}
