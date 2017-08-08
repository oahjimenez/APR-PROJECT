/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.aguapotablerural.controllers;

import java.math.BigInteger;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import main.java.aguapotablerural.dao.repository.MedidorRepository;
import main.java.aguapotablerural.dao.repository.UsuarioRepository;
import main.java.aguapotablerural.model.Medidor;
import main.java.aguapotablerural.model.Usuario;
import main.java.aguapotablerural.model.validator.MedidorValidator;
import main.java.aguapotablerural.model.validator.UsuarioValidator;
import main.java.aguapotablerural.ui.LimitedTextField;

/**
 *
 * @author cmardones
 */
public class AddUsuarioController implements Initializable{
    
    @FXML
    private ListView<Medidor> listViewMedidores;
    
    @FXML
    private LimitedTextField rutText;
    
    @FXML
    private LimitedTextField nombresText;
    @FXML
    private LimitedTextField apellidosText;
    
    @FXML
    private LimitedTextField direccionText;
    
    @FXML
    private LimitedTextField telefonoText;
    
    @FXML
    private LimitedTextField idMedidorText;
    
    @FXML
    private Button addUsuarioButton;
    
    @FXML
    private Button addMedidorButton;
    
    @FXML
    private GridPane gridUsuarioForm;
    
        //Etiquetas manejo de error
    @FXML
    public Label nombreLabel;
    @FXML
    public Label apellidosLabel;
    @FXML
    public Label rutLabel;
    @FXML
    public Label direccionLabel;    
    @FXML
    public Label telefonoLabel;
    
    @FXML
    public Label medidorLabel;

    private ObservableList<Usuario> usuarios;
    
    private Usuario newUsuario;
    
    private UsuarioRepository usuarioRepository;
    private MedidorRepository medidorRepository;
    
    
    private DecimalFormat formatter;

    
    public AddUsuarioController(ObservableList<Usuario> usuarios,UsuarioRepository usuarioRepository,MedidorRepository medidorRepository) {
        this.usuarios = usuarios;
        this.newUsuario = new Usuario();
        this.usuarioRepository = usuarioRepository;
        this.medidorRepository = medidorRepository;
        
        formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        formatter.setDecimalFormatSymbols(symbols);
    }

     private String formatRut(String rut){
        StringBuilder builder = new StringBuilder();
        if (rut.length() > 1) { 
            String digitoVerificador = rut.substring(rut.length() - 1); 
            String rutSinDgv = rut.substring(0, rut.length() - 1);
            return builder.append(formatter.format(new BigInteger(rutSinDgv))).append("-").append(digitoVerificador).toString();
        }
        return formatter.format(new BigInteger(rut));
    }
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.rutText.setMaxLength(UsuarioValidator.RUT_MAXCHAR);
        this.nombresText.setMaxLength(UsuarioValidator.NOMBRES_MAXCHAR);
        this.apellidosText.setMaxLength(UsuarioValidator.APELLIDOS_MAXCHAR);
        this.direccionText.setMaxLength(UsuarioValidator.DIRECCION_MAXCHAR);
        this.telefonoText.setMaxLength(UsuarioValidator.TELEFONO_MAXCHAR);
        
        this.rutText.textProperty().addListener((observable, oldRut, newRut) -> {
            try {
                String rut = this.rutText.getText().replace(".","").replace("-","");
                this.rutLabel.setVisible(!UsuarioValidator.isValidRut(rut));
                rutText.setText(this.formatRut(rut));
            } catch (NumberFormatException e) {
                this.rutLabel.setVisible(true);
            }
        });
        this.nombresText.textProperty().addListener((obs, oldNombre, newNombre) -> { 
                this.nombreLabel.setVisible(!UsuarioValidator.isValidNombres(newNombre));
                this.nombresText.setText(newNombre.toUpperCase());
        });
        this.apellidosText.textProperty().addListener((obs, oldApellidos, newApellidos) -> {
            this.apellidosLabel.setVisible(!UsuarioValidator.isValidApellidos(newApellidos));
            this.apellidosText.setText(newApellidos.toUpperCase());
        });
        this.direccionText.textProperty().addListener((obs, oldDireccion, newDireccion) -> {
            this.direccionLabel.setVisible(!UsuarioValidator.isValidDireccion(newDireccion));
            this.direccionText.setText(newDireccion.toUpperCase());
        });
        this.telefonoText.textProperty().addListener((obs, oldTelefono, newTelefono) -> {
            this.telefonoLabel.setVisible(!UsuarioValidator.isValidTelefono(newTelefono));
            this.telefonoText.setText(newTelefono);
        });
        this.idMedidorText.textProperty().addListener((obs, oldTelefono, newIdMedidor) -> {
            this.medidorLabel.setVisible(!MedidorValidator.isValidId(newIdMedidor));
            this.idMedidorText.setText(newIdMedidor);
        });
        this.listViewMedidores.setItems(this.newUsuario.getMedidoresObservable());
    }
    @FXML
    private boolean registraUsuarioAction(ActionEvent event) {        
        this.newUsuario.setRut(rutText.getText());
        this.newUsuario.setNombres(nombresText.getText());
        this.newUsuario.setApellidos(apellidosText.getText());
        this.newUsuario.setDireccion(direccionText.getText());
        this.newUsuario.setTelefono(telefonoText.getText());
        
        if (!(UsuarioValidator.isValid(this.newUsuario))) {
            Alert alert= new Alert(AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText(null);
            alert.setContentText("Existen datos incompletos o mal ingresados");
            alert.showAndWait();
            System.err.println("Usuario es invalido. No se registro en la base de datos");
            return false;
        }
        boolean registradoConExito = usuarioRepository.save(this.newUsuario) && usuarios.add(this.newUsuario);
        Stage stage = (Stage) addUsuarioButton.getScene().getWindow();
        stage.close();
        return registradoConExito;
    }
    
    @FXML
    private boolean agregaMedidorAction(ActionEvent event) {
        boolean addedSucess = false;
        Medidor medidor = new Medidor();
        medidor.setId(this.idMedidorText.getText());
        this.medidorLabel.setVisible(!MedidorValidator.isValid(medidor));
        if (MedidorValidator.isValid(medidor) && !this.newUsuario.getMedidoresObservable().contains(medidor)){
            addedSucess = this.newUsuario.getMedidoresObservable().add(medidor);
        }
        return addedSucess;
    }

    @FXML
    private boolean eliminaMedidorAction(ActionEvent event) {
        Medidor medidor = this.listViewMedidores.getSelectionModel().getSelectedItem();
        return this.newUsuario.getMedidoresObservable().remove(medidor);
    }

    public void setUsuariosObservable(ObservableList<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
    
    public void setUsuarioRepository(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    
}
