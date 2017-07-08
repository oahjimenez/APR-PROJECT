/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.aguapotablerural.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import main.java.aguapotablerural.dao.contract.MedidorRepository;
import main.java.aguapotablerural.dao.contract.UsuarioRepository;
import main.java.aguapotablerural.model.Medidor;
import main.java.aguapotablerural.model.Usuario;

/**
 *
 * @author cmardones
 */
public class AddUsuarioController implements Initializable{
    
    @FXML
    private ListView<Medidor> listViewMedidores;
    
    @FXML
    private TextField rutText;
    
    @FXML
    private TextField nombresText;
    @FXML
    private TextField apellidosText;
    
    @FXML
    private TextField direccionText;
    
    @FXML
    private TextField telefonoText;
    
    @FXML
    private TextField idMedidorText;
    
    @FXML
    private Button addUsuarioButton;
    
    @FXML
    private Button addMedidorButton;
    
    @FXML
    private GridPane gridUsuarioForm;
    
    
    private ObservableList<Medidor> medidores = FXCollections.observableArrayList();
    private ObservableList<Usuario> usuarios;
    
    private UsuarioRepository usuarioRepository;
    private MedidorRepository medidorRepository;
    
    public AddUsuarioController(ObservableList<Usuario> usuarios,UsuarioRepository usuarioRepository,MedidorRepository medidorRepository) {
        this.usuarios = usuarios;
        this.usuarioRepository = usuarioRepository;
        this.medidorRepository = medidorRepository;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.listViewMedidores.setItems(medidores);
    }
    @FXML
    private boolean registraUsuarioAction(ActionEvent event) {
        System.out.println(String.format("Valor de rutText:%s",this.rutText.getText()));
        if (this.rutText==null || this.rutText.getText().isEmpty()) {
            System.err.println("No se registró usuario pues campo Rut está vacío");
            return false;
        }
         if (this.nombresText==null || this.nombresText.getText().isEmpty()) {
            System.err.println("No se registró usuario pues campo Nombre está vacío");
            return false;
        }
        
        Usuario usuario = new Usuario();
        usuario.setRut(rutText.getText());
        usuario.setNombres(nombresText.getText());
        usuario.setApellidos(apellidosText.getText());
        usuario.setDireccion(direccionText.getText());
        usuario.setTelefono(telefonoText.getText());
        usuario.addMedidores(medidores);
        
        boolean registradoConExito = usuarioRepository.save(usuario) && usuarios.add(usuario);
        Stage stage = (Stage) addUsuarioButton.getScene().getWindow();
        stage.close();
        return registradoConExito;
    }
    
    @FXML
    private boolean agregaMedidorAction(ActionEvent event) {
        Medidor medidor = new Medidor();
        medidor.setId(this.idMedidorText.getText());
        return medidores.add(medidor);
    }

    public void setUsuariosObservable(ObservableList<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
    
    public void setUsuarioRepository(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    
}
