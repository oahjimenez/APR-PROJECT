/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.aguapotablerural.controllers;

import main.java.aguapotablerural.dao.contract.UsuarioRepository;
import main.java.aguapotablerural.dao.impl.UsuarioRepositoryImpl;
import main.java.aguapotablerural.database.contract.DBDriverManager;
import main.java.aguapotablerural.database.impl.SqliteDriverManager;
import main.java.aguapotablerural.model.Usuario;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 *
 * @author Sebastián
 */
public class UsuariosController implements Initializable {
    
    @FXML
    private TextField filtroText;
    
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
       
        
        usuarios.addAll(usuarioRepository.getActiveUsuarios());
        tableViewUsuarios.setItems(usuarios);
       // Wrap ObservableList in a FilteredList
        FilteredList<Usuario> usuariosFiltrados = new FilteredList<>(usuarios, u -> true);
        
         //Set filter Predicate whenever the filter changes
        filtroText.textProperty().addListener((observable, oldValue, newValue) -> {
            usuariosFiltrados.setPredicate(usuario -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (usuario.getRut().toLowerCase().contains(lowerCaseFilter)) {
                    return true; 
                } 
                if (usuario.getNombre().toLowerCase().contains(lowerCaseFilter)) {
                    return true; 
                }
                if (usuario.getDireccion().toLowerCase().contains(lowerCaseFilter)) {
                    return true; 
                }
                if (usuario.getTelefono().toLowerCase().contains(lowerCaseFilter)) {
                    return true; 
                }
                return false;
            });
        });
        
        //Wrap FilteredList in a SortedList
        SortedList<Usuario> usuariosOrdenados = new SortedList<>(usuariosFiltrados);
        
        //Bind SortedList comparator to TableView comparator
        usuariosOrdenados.comparatorProperty().bind(tableViewUsuarios.comparatorProperty());

        //Add filtered data to the table
        tableViewUsuarios.setItems(usuariosOrdenados);
       
    }
    
    @FXML
    private void eliminaUsuarioAction(ActionEvent event) {
        Usuario usuario = this.tableViewUsuarios.getSelectionModel().getSelectedItem();
        usuarios.remove(usuario);
        usuarioRepository.delete(usuario);
    }
    
    
    @FXML
    private void registraUsuarioAction(ActionEvent event) {
        Usuario usuario = this.tableViewUsuarios.getSelectionModel().getSelectedItem();
        usuarios.remove(usuario);
        usuarioRepository.delete(usuario);
    }
    
    @FXML
    private void openNewUsuarioLayoutAction(ActionEvent event) {
         try {
        Parent root1  = FXMLLoader.load(getClass().getClassLoader().getResource("main/resources/layouts/NewUsuario.fxml"));
                Stage stage = new Stage();
                stage.setScene(new Scene(root1));  
                stage.show();
        } catch(Exception e) {
           e.printStackTrace();
        }
    }
    
}
