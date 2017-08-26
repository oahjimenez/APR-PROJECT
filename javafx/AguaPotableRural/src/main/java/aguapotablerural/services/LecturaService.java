/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.aguapotablerural.services;

import java.time.LocalDate;
import java.util.List;
import main.java.aguapotablerural.dao.impl.LecturaMensualRepositoryImpl;
import main.java.aguapotablerural.dao.repository.LecturaMensualRepository;
import main.java.aguapotablerural.database.contract.DBDriverManager;
import main.java.aguapotablerural.database.impl.SqliteDriverManager;
import main.java.aguapotablerural.model.Medidor;
import main.java.aguapotablerural.model.Usuario;

/**
 *
 * @author cmardones
 */
public class LecturaService {
    private final LecturaMensualRepository lecturaMensualRepository;
    
   public LecturaService() {
        DBDriverManager driver = new SqliteDriverManager();
        this.lecturaMensualRepository = new LecturaMensualRepositoryImpl(driver);
   }
   
    public boolean guardarLectura(Usuario usuario,Medidor medidor,LocalDate fecha,double lectura){
       return this.lecturaMensualRepository.save(usuario,medidor, fecha, lectura);
   }
   
    public double obtenerLectura(Usuario usuario,Medidor medidor,LocalDate fecha){
       return this.lecturaMensualRepository.getLecturaMensual(usuario,medidor, fecha);
   }
    
    public List<String> getRutUsuariosConLecturaMensual(LocalDate fecha){
       return this.lecturaMensualRepository.getRutUsuariosConLecturaMensual(fecha);
    }
}
