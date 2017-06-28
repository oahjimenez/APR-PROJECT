/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.aguapotablerural.database.contract;

import java.sql.Connection;

/**
 *
 * @author carlo
 */
public interface DBDriverManager {
    
    public Connection getConnection();
    
}
