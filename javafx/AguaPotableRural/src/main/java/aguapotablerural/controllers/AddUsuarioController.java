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
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import main.java.aguapotablerural.dao.repository.MedidorRepository;
import main.java.aguapotablerural.dao.repository.UsuarioRepository;
import main.java.aguapotablerural.model.Medidor;
import main.java.aguapotablerural.model.Usuario;
import main.java.aguapotablerural.model.validator.MedidorValidator;
import main.java.aguapotablerural.model.validator.UsuarioValidator;
import main.java.aguapotablerural.services.UsuarioService;
import main.java.aguapotablerural.ui.LimitedTextField;

/**
 *
 * @author cmardones
 */
public class AddUsuarioController implements Initializable{
    
    private static final String ERROR_MSG_RUT_EXISTENTE="Rut ya existente";
    private static final String ERROR_MSG_CAMPO_OBLIGATORIO="Información obligatoria.";
    private static final String ERROR_MSG_RUT_INVALIDO="Rut inválido.";
    
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
    
    private final Usuario newUsuario;
    
    private UsuarioRepository usuarioRepository;
    private final MedidorRepository medidorRepository;
    
    
    private final DecimalFormat formatter;
    private final UsuarioService usuarioService;

    
    public AddUsuarioController(ObservableList<Usuario> usuarios,UsuarioRepository usuarioRepository,MedidorRepository medidorRepository) {
        this.usuarios = usuarios;
        this.newUsuario = new Usuario();
        this.usuarioRepository = usuarioRepository;
        this.medidorRepository = medidorRepository;
        this.usuarioService = new UsuarioService();
        
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
     
         
    private String cleanRut(String rut){
        return rut.replace(".","");
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
                if (this.rutLabel.isVisible()) { //necesario para restituir mesaje de rut invalido cuando se arroja error cuando rut ya existe
                    this.rutLabel.setText(ERROR_MSG_RUT_INVALIDO);
                }
                rutText.setText(this.formatRut(rut));
            } catch (NumberFormatException e) {
                this.rutLabel.setVisible(true);
            }
        });
        boolean widgetVisible = false;
        this.rutLabel.setVisible(widgetVisible);
        this.nombreLabel.setVisible(widgetVisible);
        this.apellidosLabel.setVisible(widgetVisible);
        this.direccionLabel.setVisible(widgetVisible);
        this.telefonoLabel.setVisible(widgetVisible);
        this.medidorLabel.setVisible(widgetVisible);
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
        boolean registradoConExito = false;
        Usuario newUsuario = new Usuario();
        newUsuario.setRut(this.cleanRut(rutText.getText()));
        newUsuario.setNombres(nombresText.getText());
        newUsuario.setApellidos(apellidosText.getText());
        newUsuario.setDireccion(direccionText.getText());
        newUsuario.setTelefono(telefonoText.getText());  
        Usuario usuarioRut = usuarioService.getUsuario(this.cleanRut(rutText.getText()));
        boolean existeOtroUsuarioConRut = (usuarioRut!=null) && (this.newUsuario.getId()!=usuarioRut.getId());
        System.err.println("existeOtroUsuarioConRut:<"+existeOtroUsuarioConRut+">,id newuser:"+newUsuario.getId());
        System.err.println("query usuario:"+usuarioRut);
        if (existeOtroUsuarioConRut) {
            this.rutLabel.setText(ERROR_MSG_RUT_EXISTENTE);
            this.rutLabel.setVisible(true);
        } 
        this.rutLabel.setVisible(existeOtroUsuarioConRut || !UsuarioValidator.isValidRut(newUsuario.getRut()));
        
        if (UsuarioValidator.isValid(newUsuario) && !existeOtroUsuarioConRut) {   
            this.newUsuario.setRut(newUsuario.getRut());
            this.newUsuario.setNombres(newUsuario.getNombres());
            this.newUsuario.setApellidos(newUsuario.getApellidos());
            this.newUsuario.setDireccion(newUsuario.getDireccion());
            this.newUsuario.setTelefono(newUsuario.getTelefono());  
            registradoConExito = usuarioService.crearUsuario(this.newUsuario)&& usuarios.add(this.newUsuario);
            Stage stage = (Stage) addUsuarioButton.getScene().getWindow();
            stage.close();
        }
            else {
            Alert alert= new Alert(AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText(null);
            alert.setContentText("Existen datos incompletos o mal ingresados");
            alert.showAndWait();
            System.err.println("Usuario es invalido. No se registro en la base de datos");
            System.err.println(String.format("%s - editarUsuarioAction(): fallo edicion de usuario. Datos invalidos %s.",this.getClass().getSimpleName(),this.newUsuario));
            System.err.println(String.format("rut valido?%s",UsuarioValidator.isValidRut(rutText.getText())));
            System.err.println(String.format("nombre valido?%s",UsuarioValidator.isValidNombres(nombresText.getText())));
            System.err.println(String.format("apellidos valido?%s",UsuarioValidator.isValidApellidos(apellidosText.getText())));
            System.err.println(String.format("direccion valido?%s",UsuarioValidator.isValidDireccion(direccionText.getText())));
            System.err.println(String.format("telefono valido?%s",UsuarioValidator.isValidTelefono(telefonoText.getText())));
        }
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
            if (addedSucess) { 
                this.idMedidorText.setText(""); 
            } //limpia campo de id medidor despues de ingresado
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
