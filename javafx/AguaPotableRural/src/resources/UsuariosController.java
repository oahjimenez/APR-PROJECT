/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resources;

import aguapotablerural.dao.contract.UsuarioRepository;
import aguapotablerural.dao.impl.UsuarioRepositoryImpl;
import aguapotablerural.database.contract.DBDriverManager;
import aguapotablerural.database.impl.SqliteDriverManager;
import aguapotablerural.model.Usuario;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author Sebastián
 */
public class UsuariosController implements Initializable {
    
    @FXML
    private TableView<Usuario> tableViewUsuarios;
    
    @FXML
    private TableColumn<Usuario,String> rutColumn;
    
    @FXML
    private TableColumn<Usuario,String> nombreColumn;
    
    @FXML
    private TableColumn<Usuario,String> direccionColumn;
    
    @FXML
    private TableColumn<Usuario,String> telefonoColumn;
    
    private final UsuarioRepository usuarioRepository;
    
    
    private ObservableList<Usuario> usuarios = FXCollections.observableArrayList();
    public UsuariosController() {
        DBDriverManager driverManager = new SqliteDriverManager();
        usuarioRepository = new UsuarioRepositoryImpl(driverManager);
    }

    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
       rutColumn.setCellValueFactory(cellData -> cellData.getValue().getRutColumnProperty());
       nombreColumn.setCellValueFactory(cellData -> cellData.getValue().getNombreColumnProperty());
       direccionColumn.setCellValueFactory(cellData -> cellData.getValue().getDireccionColumnProperty());
       telefonoColumn.setCellValueFactory(cellData -> cellData.getValue().getTelefonoColumnProperty());
       
       System.out.println("usuarios en la bd: "+usuarioRepository.getAllUsuarios());
       usuarios.addAll(usuarioRepository.getAllUsuarios());
       tableViewUsuarios.setItems(usuarios);
    }
}
