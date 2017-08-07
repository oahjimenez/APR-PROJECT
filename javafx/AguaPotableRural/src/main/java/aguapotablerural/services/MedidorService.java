/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.aguapotablerural.services;
import java.time.LocalDateTime;
import java.util.Collection;
import main.java.aguapotablerural.dao.impl.MedidorRepositoryImpl;
import main.java.aguapotablerural.dao.impl.TieneMedidorRepositoryImpl;
import main.java.aguapotablerural.dao.impl.UsuarioRepositoryImpl;
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
public class MedidorService {
    private final TieneMedidorRepository tieneMedidorRepository;
    private final MedidorRepository medidorRepository;
    private final UsuarioRepository usuarioRepository;
    
    public MedidorService (){
        DBDriverManager driver = new SqliteDriverManager();
        this.medidorRepository = new MedidorRepositoryImpl(driver);
        this.usuarioRepository = new UsuarioRepositoryImpl(driver);
        
        this.tieneMedidorRepository = new TieneMedidorRepositoryImpl(driver,this.usuarioRepository,this.medidorRepository);
    }
    
    public Collection<? extends Medidor> getMedidoresOf(Usuario usuario,LocalDateTime anoMes) {
        return tieneMedidorRepository.getMedidorOf(usuario, anoMes);
    }
    
}
