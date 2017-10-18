/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.aguapotablerural.services;

import java.time.LocalDate;
import main.java.aguapotablerural.dao.impl.MedidorRepositoryImpl;
import main.java.aguapotablerural.dao.impl.SubsidioRepositoryImpl;
import main.java.aguapotablerural.dao.impl.UsuarioRepositoryImpl;
import main.java.aguapotablerural.dao.repository.SubsidioRepository;
import main.java.aguapotablerural.database.contract.DBDriverManager;
import main.java.aguapotablerural.database.impl.SqliteDriverManager;
import main.java.aguapotablerural.model.Subsidio;
import main.java.aguapotablerural.model.Usuario;

/**
 *
 * @author carlo
 */
public class SubsidioService {
    
    private final SubsidioRepository subsidioRepository;
    public SubsidioService() {
        DBDriverManager driver = new SqliteDriverManager();
        this.subsidioRepository = new SubsidioRepositoryImpl(driver,new UsuarioRepositoryImpl(driver),new MedidorRepositoryImpl(driver));
    }
    
    public Subsidio getSubsidio(Usuario usuario,LocalDate fecha) {
        return this.subsidioRepository.getSubsidio(usuario, fecha);
    }
    
    public Subsidio getSubsidioActual(Usuario usuario) {
        return this.subsidioRepository.getSubsidio(usuario,LocalDate.now());
    }

    
}
