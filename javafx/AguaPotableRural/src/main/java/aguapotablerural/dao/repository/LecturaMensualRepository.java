/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.aguapotablerural.dao.repository;

import main.java.aguapotablerural.model.Medidor;
import main.java.aguapotablerural.model.Usuario;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author carlo
 */
public interface LecturaMensualRepository {
        public double getLecturaMensual(Usuario usuario,Medidor medidor,LocalDate fecha);
        public boolean save(Usuario usuario,Medidor medidor,LocalDate fecha,double lectura);
        public List<String> getRutUsuariosConLecturaMensual(LocalDate fecha);   
}
