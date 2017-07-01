/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.aguapotablerural.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.java.aguapotablerural.dao.contract.UsuarioRepository;
import main.java.aguapotablerural.model.Usuario;

/**
 * FXML Controller class
 *
 * @author Sebastián
 */
public class EditUsuarioController implements Initializable {

    @FXML
    private TextField rutText;
    
    @FXML
    private TextField nombreText;
    
    @FXML
    private TextField direccionText;
    
    @FXML
    private TextField telefonoText;
    
    private TableView<Usuario> tableViewUsuarios;
    
    @FXML
    public Button editUsuarioButton;
    
    private Usuario usuarioEditable;
    private UsuarioRepository usuarioRepository;

    public EditUsuarioController(Usuario usuarioEditable,UsuarioRepository usuarioRepository) {
        this.usuarioEditable = usuarioEditable;
        this.usuarioRepository = usuarioRepository;
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.rutText.setText(usuarioEditable.getRut());
        usuarioEditable.getRutColumnProperty().addListener((obs, oldRut, newRut) -> this.rutText.setText(newRut));
        
        this.nombreText.setText(usuarioEditable.getNombre());
        usuarioEditable.getNombreColumnProperty().addListener((obs, oldNombre, newNombre) -> this.nombreText.setText(newNombre));
        
        this.direccionText.setText(usuarioEditable.getDireccion());
        usuarioEditable.getDireccionColumnProperty().addListener((obs, oldDireccion, newDireccion) -> this.direccionText.setText(newDireccion));

        this.telefonoText.setText(usuarioEditable.getTelefono());
        usuarioEditable.getTelefonoColumnProperty().addListener((obs, oldTelefono, newTelefono) -> this.telefonoText.setText(newTelefono));
    }

    public void setUsuarioRepository(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public void setUsuarioEditable(Usuario usuarioEditble) {
        this.usuarioEditable = usuarioEditable;
    }
    
    @FXML
    public void editarUsuarioAction(ActionEvent event){
        this.usuarioEditable.setNombre(nombreText.getText());
        this.usuarioEditable.setDireccion(direccionText.getText());
        this.usuarioEditable.setTelefono(telefonoText.getText());
        this.usuarioRepository.save(usuarioEditable);
        Stage stage = (Stage) editUsuarioButton.getScene().getWindow();
        stage.close();
    }

    public void setUsuarioTableView(TableView<Usuario> tableViewUsuarios) {
        this.tableViewUsuarios = tableViewUsuarios;
    }
    
}
