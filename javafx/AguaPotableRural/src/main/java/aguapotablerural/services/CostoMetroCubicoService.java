/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.aguapotablerural.services;

import java.time.LocalDate;
import main.java.aguapotablerural.dao.impl.CostoMetroCubicoRepositoryImpl;
import main.java.aguapotablerural.dao.repository.CostoMetroCubicoRepository;
import main.java.aguapotablerural.database.contract.DBDriverManager;
import main.java.aguapotablerural.database.impl.SqliteDriverManager;
import main.java.aguapotablerural.model.CostoMetroCubico;
import main.java.aguapotablerural.model.Subsidio;
import main.java.aguapotablerural.model.Usuario;

/**
 *
 * @author carlo
 */
public class CostoMetroCubicoService {
 
    
    private final CostoMetroCubicoRepository costoMetroCubicoRepository;
    public CostoMetroCubicoService() {
        DBDriverManager driver = new SqliteDriverManager();
        this.costoMetroCubicoRepository = new CostoMetroCubicoRepositoryImpl(driver);
    }
    
    public CostoMetroCubico getCostoMetroCubico(LocalDate fecha) {
        return this.costoMetroCubicoRepository.getCostoMetroCubico(fecha);
    }
    
    public CostoMetroCubico getSubsidioActual(Usuario usuario) {
        return this.costoMetroCubicoRepository.getCostoMetroCubico(LocalDate.now());
    }

}
