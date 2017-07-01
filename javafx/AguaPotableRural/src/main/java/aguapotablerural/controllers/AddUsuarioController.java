/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.aguapotablerural.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.java.aguapotablerural.dao.contract.UsuarioRepository;
import main.java.aguapotablerural.model.Usuario;

/**
 *
 * @author cmardones
 */
public class AddUsuarioController implements Initializable{
    
    @FXML
    private TextField rutText;
    
    @FXML
    private TextField nombreText;
    
    @FXML
    private TextField direccionText;
    
    @FXML
    private TextField telefonoText;
    
    @FXML
    public Button addUsuarioButton;
    
    private ObservableList<Usuario> usuarios;
    private UsuarioRepository usuarioRepository;
    
    public AddUsuarioController(ObservableList<Usuario> usuarios,UsuarioRepository usuarioRepository) {
        this.usuarios = usuarios;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //To change body of generated methods, choose Tools | Templates.
    }
    @FXML
    private boolean registraUsuarioAction(ActionEvent event) {
        System.out.println(String.format("Valor de rutText:%s",this.rutText.getText()));
        if (this.rutText==null || this.rutText.getText().isEmpty()) {
            System.err.println("No se registró usuario pues campo Rut está vacío");
            return false;
        }
         if (this.nombreText==null || this.nombreText.getText().isEmpty()) {
            System.err.println("No se registró usuario pues campo Nombre está vacío");
            return false;
        }
        
        Usuario usuario = new Usuario();
        usuario.setRut(rutText.getText());
        usuario.setNombre(nombreText.getText());
        usuario.setDireccion(direccionText.getText());
        usuario.setTelefono(telefonoText.getText());
        
        boolean registradoConExito = usuarioRepository.save(usuario) && usuarios.add(usuario);
        Stage stage = (Stage) addUsuarioButton.getScene().getWindow();
        stage.close();
        return registradoConExito;
    }

    public void setUsuariosObservable(ObservableList<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
    
    public void setUsuarioRepository(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    
}
