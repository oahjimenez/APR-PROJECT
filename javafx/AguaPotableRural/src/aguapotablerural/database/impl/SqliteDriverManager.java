/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aguapotablerural.database.impl;

import aguapotablerural.database.contract.DBDriverManager;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author carlo
 */
public class SqliteDriverManager implements DBDriverManager {
    
    private Connection connection;
    
    public SqliteDriverManager() {}
    
     /**
     * Connect to a sample database
     */
    public Connection getConnection() {
        /* Connection conn = null;
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
        }*/
        
        if (this.connection!= null) {
            return this.connection;
        }
        try {
            // db parameters
            String url = "jdbc:sqlite:C:/Users/carlo/Desktop/gitrepo/APR-PROJECT/sqlite3/AGUA_POTABLE_RURAL.db";
            // create a connection to the database
            this.connection = DriverManager.getConnection(url);
            
            System.out.println("Connection to SQLite has been established.");
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } 
        return connection;
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       SqliteDriverManager connection = new SqliteDriverManager();
       connection.getConnection();
    }
}
