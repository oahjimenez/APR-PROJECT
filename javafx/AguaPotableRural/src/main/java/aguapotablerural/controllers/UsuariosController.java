/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.aguapotablerural.controllers;

import main.java.aguapotablerural.dao.repository.UsuarioRepository;
import main.java.aguapotablerural.dao.impl.UsuarioRepositoryImpl;
import main.java.aguapotablerural.database.contract.DBDriverManager;
import main.java.aguapotablerural.database.impl.SqliteDriverManager;
import main.java.aguapotablerural.model.Usuario;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import main.java.aguapotablerural.dao.repository.MedidorRepository;
import main.java.aguapotablerural.dao.impl.MedidorRepositoryImpl;
import main.java.aguapotablerural.model.validator.UsuarioValidator;
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
    
    private UsuarioValidator usuarioValidator;
    
    
    private ObservableList<Usuario> usuarios = FXCollections.observableArrayList();
    public UsuariosController() {
        DBDriverManager driverManager = new SqliteDriverManager();
        this.usuarioRepository = new UsuarioRepositoryImpl(driverManager);
        this.medidorRepository = new MedidorRepositoryImpl(driverManager);
        this.usuarioValidator = new UsuarioValidator();
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
       rutColumn.setCellValueFactory(cellData -> {
           return new SimpleStringProperty(this.usuarioValidator.formatRut(cellData.getValue().getRut()));
       });
       nombresColumn.setCellValueFactory(cellData -> cellData.getValue().getNombresProperty());
       apellidosColumn.setCellValueFactory(cellData -> cellData.getValue().getApellidosProperty());
       direccionColumn.setCellValueFactory(cellData -> cellData.getValue().getDireccionProperty());
       telefonoColumn.setCellValueFactory(cellData -> cellData.getValue().getTelefonoProperty());
       
        usuarios.addAll(usuarioRepository.getActiveUsuarios());
        tableViewUsuarios.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
         this.tableViewUsuarios.setRowFactory( tv -> {
            TableRow<Usuario> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    openEditUsuarioLayout();
                }
            });
            return row ;
        });
                
        tableViewUsuarios.setItems(usuarios);
        
        // Wrap ObservableList in a FilteredList
        FilteredList<Usuario> usuariosFiltrados = new FilteredList<>(usuarios,u -> true);
        //Set filter Predicate whenever the filter changes
        filtroText.textProperty().addListener((observable, oldValue, newValue) -> {
            usuariosFiltrados.setPredicate(usuario -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty() || newValue.trim().isEmpty() ) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase().trim();
                if (usuario.getRut().toLowerCase().contains(lowerCaseFilter) || usuario.getApellidos().toLowerCase().contains(lowerCaseFilter) ||
                        usuario.getDireccion().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                String[] keywords = newValue.toLowerCase().trim().split(" ");
                for (String keyword: keywords) {
                    for (String nombre : usuario.getNombres().toLowerCase().split(" ")){
                        if (nombre.contains(keyword)) {
                            return true;
                        }
                    }
                }
                
                for (String keyword: keywords) {
                    for (String apellidos : usuario.getApellidos().toLowerCase().split(" ")){
                        if (apellidos.contains(keyword)) {
                            return true;
                        }
                    }
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
                        return new EditUsuarioController(tableViewUsuarios.getSelectionModel().getSelectedItem(),usuarioRepository,tableViewUsuarios);
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
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText(null);
        alert.setContentText(String.format("¿Estás seguro de eliminar al usuario %s %s? Esto es irreversible",usuario.getNombres(),usuario.getApellidos()));
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            usuarios.remove(usuario);
            System.out.println("Eliminando usuario de BD:" + usuario);
            usuarioRepository.delete(usuario);
        } else {
            System.out.println("usuario cancela eliminacion");
        }
    }
    
    
    @FXML
    private void openAddUsuarioLayoutAction(ActionEvent event) {
         try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("main/resources/layouts/AddUsuario.fxml"));
            fxmlLoader.setControllerFactory(controllerFactory);
            Parent root1 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));  
            AddUsuarioController addUsuarioController = fxmlLoader.getController();
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    if (!addUsuarioController.isDirty()) {
                        return;
                    }
                    event.consume();

                    Alert alert = new Alert(AlertType.CONFIRMATION);
                    alert.setTitle("Cancelar Ingreso");
                    alert.setHeaderText(null);
                    alert.setContentText("Tiene cambios sin guardar para este usuario. ¿Seguro que desea continuar?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK){
                        System.err.println("Usuario cancela insercion");
                        stage.close();
                    }
                }        
            });
            stage.show();
        } catch(Exception e) {
           e.printStackTrace();
        }
    }
    
    
    @FXML
    private void openEditUsuarioLayoutAction(ActionEvent event) {
        this.openEditUsuarioLayout();
    }
    
    @FXML
    private void resetFilterAction(ActionEvent event) {
        this.filtroText.clear();
    }
    
    private void openEditUsuarioLayout() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("main/resources/layouts/EditUsuario.fxml"));
            fxmlLoader.setControllerFactory(controllerFactory);
            Parent root1 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));  
            EditUsuarioController editUsuarioController = fxmlLoader.getController();
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    if (!editUsuarioController.isDirty()) {
                        return;
                    }
                    event.consume();

                    Alert alert = new Alert(AlertType.CONFIRMATION);
                    alert.setTitle("Cancelar Edición");
                    alert.setHeaderText(null);
                    alert.setContentText("Tiene cambios sin guardar para este usuario. ¿Seguro que desea continuar?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK){
                        System.err.println("Usuario cancela edicion");
                        stage.close();
                    }
                }        
            });
            stage.show();
        } catch(Exception e) {
           e.printStackTrace();
        }
    }
    
}
