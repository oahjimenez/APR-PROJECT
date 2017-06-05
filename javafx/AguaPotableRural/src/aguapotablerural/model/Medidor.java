/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aguapotablerural.model;

import aguapotablerural.database.SQLiteJDBCDriverConnection;
import java.sql.Date;
import java.sql.PreparedStatement;

/**
 *
 * @author carlo
 */
public class Medidor implements ModeloDominio {
    
    private String id;
    private String nombre;
    private Date fechaRegistro;
    private Date fechaRetiro;
    
    private SQLiteJDBCDriverConnection dbconnection;

    public Medidor(SQLiteJDBCDriverConnection dbconnection){
        this.dbconnection = dbconnection;
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Date getFechaRetiro() {
        return fechaRetiro;
    }

    public void setFechaRetiro(Date fechaRetiro) {
        this.fechaRetiro = fechaRetiro;
    }

    @Override
    public String toString() {
        return "Medidor{" + "id=" + id + ", nombre=" + nombre + ", fechaRegistro=" + fechaRegistro + ", fechaRetiro=" + fechaRetiro + '}';
    }
    
    @Override
    public void save(){
         try {
            PreparedStatement statement = dbconnection.getConnection().prepareStatement("INSERT OR REPLACE INTO MEDIDOR (id,nombre,fecha_registro,fecha_retiro) " +
                     "  VALUES (?, ? , CURRENT_DATE, ?  );");
            statement.setString(1,this.id);
            statement.setString(2,this.nombre);
            statement.setDate(3,this.fechaRetiro);
            statement.executeUpdate();
            statement.close();
        }catch (Exception e){
            System.err.println(this.getClass()+ ": " + e.getClass().getName() + ": " + e.getMessage() );
        }
    }
    public void delete()
    { 
        System.out.println("Eliminando medidor en base sqlite3");
        try {
            PreparedStatement statement = dbconnection.getConnection().prepareStatement("UPDATE MEDIDOR SET FECHA_RETIRO = CURRENT_DATE WHERE ID = ?;");
            statement.setString(1,this.id);
            statement.executeUpdate();
            statement.close();
        }catch (Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
        
    }
}
