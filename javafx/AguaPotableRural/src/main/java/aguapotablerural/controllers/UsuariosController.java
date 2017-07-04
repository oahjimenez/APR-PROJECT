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
import javafx.beans.binding.Bindings;
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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Callback;
import main.java.aguapotablerural.dao.contract.MedidorRepository;
import main.java.aguapotablerural.dao.impl.MedidorRepositoryImpl;
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
    private TableColumn<Usuario,String> nombresColumn;
    
    
    @FXML
    private TableColumn<Usuario,String> apellidosColumn;
    
    @FXML
    private TableColumn<Usuario,String> direccionColumn;
    
    @FXML
    private TableColumn<Usuario,String> telefonoColumn;
        
    @FXML
    public Button editUsuarioButton;
    
    private final UsuarioRepository usuarioRepository;
    
    private final MedidorRepository medidorRepository;
    
    private Callback<Class<?>, Object> controllerFactory;
    
    
    private ObservableList<Usuario> usuarios = FXCollections.observableArrayList();
    public UsuariosController() {
        DBDriverManager driverManager = new SqliteDriverManager();
        usuarioRepository = new UsuarioRepositoryImpl(driverManager);
        medidorRepository = new MedidorRepositoryImpl(driverManager);
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
       rutColumn.setCellValueFactory(cellData -> cellData.getValue().getRutProperty());
       nombresColumn.setCellValueFactory(cellData -> cellData.getValue().getNombresProperty());
       apellidosColumn.setCellValueFactory(cellData -> cellData.getValue().getApellidosProperty());
       direccionColumn.setCellValueFactory(cellData -> cellData.getValue().getDireccionProperty());
       telefonoColumn.setCellValueFactory(cellData -> cellData.getValue().getTelefonoProperty());
       
        usuarios.addAll(usuarioRepository.getActiveUsuarios());
        tableViewUsuarios.setItems(usuarios);
       // Wrap ObservableList in a FilteredList
        FilteredList<Usuario> usuariosFiltrados = new FilteredList<>(usuarios,u -> true);
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
                if (usuario.getNombres().toLowerCase().contains(lowerCaseFilter)) {
                    return true; 
                }
                if (usuario.getApellidos().toLowerCase().contains(lowerCaseFilter)) {
                    return true; 
                }
                if (usuario.getDireccion().toLowerCase().contains(lowerCaseFilter)) {
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
        
        editUsuarioButton.disableProperty().bind(Bindings.isEmpty(tableViewUsuarios.getSelectionModel().getSelectedItems()));
        
        controllerFactory = new Callback<Class<?>, Object>() {
                @Override
                public Object call(Class<?> controllerClass) {
                    if (controllerClass == AddUsuarioController.class) {
                        return new AddUsuarioController(usuarios,usuarioRepository,medidorRepository);
                    } else if (controllerClass == EditUsuarioController.class) {
                        return new EditUsuarioController(tableViewUsuarios.getSelectionModel().getSelectedItem(),usuarioRepository);
                    } else {
                        try {
                            return controllerClass.newInstance();
                        } catch (Exception exc) {
                            System.err.println("Could not create controller for "+controllerClass.getName());
                            throw new RuntimeException(exc);
                        }
                    }
                }
            };
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
    private void openAddUsuarioLayoutAction(ActionEvent event) {
         try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("main/resources/layouts/AddUsuario.fxml"));
            fxmlLoader.setControllerFactory(controllerFactory);
            Parent root1 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));  
            stage.show();
        } catch(Exception e) {
           e.printStackTrace();
        }
    }
    
    
    @FXML
    private void openEditUsuarioLayoutAction(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("main/resources/layouts/EditUsuario.fxml"));
            fxmlLoader.setControllerFactory(controllerFactory);
            Parent root1 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));  
            stage.show();
        } catch(Exception e) {
           e.printStackTrace();
        }
    }
    
}
