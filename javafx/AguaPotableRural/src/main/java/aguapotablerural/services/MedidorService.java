/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.aguapotablerural.services;
import java.time.YearMonth;
import main.java.aguapotablerural.dao.repository.MedidorRepository;
import main.java.aguapotablerural.dao.impl.MedidorRepositoryImpl;
import main.java.aguapotablerural.database.impl.SqliteDriverManager;
import main.java.aguapotablerural.model.Medidor;
import main.java.aguapotablerural.model.Usuario;

/**
 *
 * @author cmardones
 */
public class MedidorService {
    private final MedidorRepository medidorRepository;
    
    public MedidorService (){
        this.medidorRepository = new MedidorRepositoryImpl(new SqliteDriverManager());
    }
    
    public Iterable<Medidor> getMedidoresOf(Usuario usuario,YearMonth anoMes) {
        return null;
    }
    
}
