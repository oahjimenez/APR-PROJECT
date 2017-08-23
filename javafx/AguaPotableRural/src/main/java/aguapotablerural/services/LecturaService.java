/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.aguapotablerural.services;

import java.time.LocalDate;
import main.java.aguapotablerural.dao.impl.LecturaMensualRepositoryImpl;
import main.java.aguapotablerural.dao.impl.MedidorRepositoryImpl;
import main.java.aguapotablerural.dao.impl.TieneMedidorRepositoryImpl;
import main.java.aguapotablerural.dao.impl.UsuarioRepositoryImpl;
import main.java.aguapotablerural.dao.repository.LecturaMensualRepository;
import main.java.aguapotablerural.dao.repository.MedidorRepository;
import main.java.aguapotablerural.dao.repository.TieneMedidorRepository;
import main.java.aguapotablerural.dao.repository.UsuarioRepository;
import main.java.aguapotablerural.database.contract.DBDriverManager;
import main.java.aguapotablerural.database.impl.SqliteDriverManager;
import main.java.aguapotablerural.model.Medidor;
import main.java.aguapotablerural.model.Usuario;

/**
 *
 * @author cmardones
 */
public class LecturaService {
    private final MedidorRepository medidorRepository;
    private final UsuarioRepository usuarioRepository;
    private final LecturaMensualRepository lecturaMensualRepository;
    private final TieneMedidorRepository tieneMedidorRepository;
    
   public LecturaService() {
        DBDriverManager driver = new SqliteDriverManager();
        this.medidorRepository = new MedidorRepositoryImpl(driver);
        this.usuarioRepository = new UsuarioRepositoryImpl(driver);
        this.lecturaMensualRepository = new LecturaMensualRepositoryImpl(driver);
        this.tieneMedidorRepository = new TieneMedidorRepositoryImpl(driver,this.usuarioRepository,this.medidorRepository);
   }
   
   public boolean guardarLectura(Usuario usuario,Medidor medidor,LocalDate fecha,double lectura){
       return this.lecturaMensualRepository.save(usuario,medidor, fecha, lectura);
   }
}
