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
    private UsuarioValidator usuarioValidator;
    
    private boolean isDirty = false; //indica si formulario ha cambiado 

    
    public AddUsuarioController(ObservableList<Usuario> usuarios,UsuarioRepository usuarioRepository,MedidorRepository medidorRepository) {
        this.usuarios = usuarios;
        this.newUsuario = new Usuario();
        this.usuarioRepository = usuarioRepository;
        this.medidorRepository = medidorRepository;
        this.usuarioService = new UsuarioService();
        this.usuarioValidator = new UsuarioValidator();
        this.usuarioValidator.setRutMandatory(true);
        this.usuarioValidator.setNombresMandatory(true);
        this.usuarioValidator.setApellidosMandatory(true);
        
        formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        formatter.setDecimalFormatSymbols(symbols);
    }

     private String formatRut(String rut){
        rut = rut.toUpperCase();
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
            this.setDirty(true);
            try {
                String rut = this.rutText.getText().replace(".","").replace("-","");
                this.rutLabel.setVisible(!this.usuarioValidator.isValidRut(rut));
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
            this.setDirty(true);
            this.nombreLabel.setVisible(!this.usuarioValidator.isValidNombres(newNombre));
            this.nombresText.setText(newNombre.toUpperCase());
        });
        this.apellidosText.textProperty().addListener((obs, oldApellidos, newApellidos) -> {
            this.setDirty(true);
            this.apellidosLabel.setVisible(!this.usuarioValidator.isValidApellidos(newApellidos));
            this.apellidosText.setText(newApellidos.toUpperCase());
        });
        this.direccionText.textProperty().addListener((obs, oldDireccion, newDireccion) -> {
            this.setDirty(true);
            this.direccionLabel.setVisible(!this.usuarioValidator.isValidDireccion(newDireccion));
            this.direccionText.setText(newDireccion.toUpperCase());
        });
        this.telefonoText.textProperty().addListener((obs, oldTelefono, newTelefono) -> {
            this.setDirty(true);
            this.telefonoLabel.setVisible(!this.usuarioValidator.isValidTelefono(newTelefono));
            this.telefonoText.setText(newTelefono);
        });
        this.idMedidorText.textProperty().addListener((obs, oldTelefono, newIdMedidor) -> {
            this.setDirty(true);
            this.medidorLabel.setVisible(!MedidorValidator.isValidId(newIdMedidor));
            this.idMedidorText.setText(newIdMedidor);
        });
        this.listViewMedidores.setItems(this.newUsuario.getMedidoresObservable());
    }
    @FXML
    private boolean registraUsuarioAction(ActionEvent event) {      
        boolean registradoConExito = false;
        Usuario _newUsuario = new Usuario();
        _newUsuario.setRut(this.cleanRut(rutText.getText()));
        _newUsuario.setNombres(nombresText.getText());
        _newUsuario.setApellidos(apellidosText.getText());
        _newUsuario.setDireccion(direccionText.getText());
        _newUsuario.setTelefono(telefonoText.getText());  
        Usuario usuarioRut = usuarioService.getUsuario(this.cleanRut(rutText.getText()));
        boolean existeOtroUsuarioConRut = ((usuarioRut!=null) && (usuarioRut.getFechaRetiro()==null));
        System.err.println("existeOtroUsuarioConRut:<"+existeOtroUsuarioConRut+">,id newuser:"+_newUsuario.getId());
        System.err.println("query usuario:"+usuarioRut);
        if (existeOtroUsuarioConRut) {
            this.rutLabel.setText(ERROR_MSG_RUT_EXISTENTE);
            this.rutLabel.setVisible(true);
        } 
        this.rutLabel.setVisible(existeOtroUsuarioConRut || !this.usuarioValidator.isValidRut(_newUsuario.getRut()));
        
        if (this.usuarioValidator.isValid(_newUsuario) && !existeOtroUsuarioConRut) {   
            this.newUsuario.setRut(_newUsuario.getRut());
            this.newUsuario.setNombres(_newUsuario.getNombres());
            this.newUsuario.setApellidos(_newUsuario.getApellidos());
            this.newUsuario.setDireccion(_newUsuario.getDireccion());
            this.newUsuario.setTelefono(_newUsuario.getTelefono());  
            registradoConExito = usuarioService.crearUsuario(this.newUsuario);
            this.newUsuario.setId(usuarioService.getUsuario(this.newUsuario.getRut()).getId());
            registradoConExito = registradoConExito && usuarios.add(this.newUsuario);
            Stage stage = (Stage) addUsuarioButton.getScene().getWindow();
            stage.close();
        }
        else {
            if ((_newUsuario.getRut()==null || _newUsuario.getRut().isEmpty()) && this.usuarioValidator.isRutMandatory()) {
                this.rutLabel.setText(ERROR_MSG_CAMPO_OBLIGATORIO);
                this.rutLabel.setVisible(true);
            }
            
            if ((_newUsuario.getNombres()==null || _newUsuario.getNombres().trim().isEmpty()) && this.usuarioValidator.isNombresMandatory()) {
                this.nombreLabel.setText(ERROR_MSG_CAMPO_OBLIGATORIO);
                this.nombreLabel.setVisible(true);
            }
            
            if ((_newUsuario.getApellidos()==null || _newUsuario.getApellidos().trim().isEmpty()) && this.usuarioValidator.isApellidosMandatory()) {
                this.apellidosLabel.setText(ERROR_MSG_CAMPO_OBLIGATORIO);
                this.apellidosLabel.setVisible(true);
            }
            
            Alert alert= new Alert(AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText(null);
            alert.setContentText("Existen datos incompletos o mal ingresados");
            alert.showAndWait();
            System.err.println("Usuario es invalido. No se registro en la base de datos");
            System.err.println(String.format("%s - editarUsuarioAction(): fallo edicion de usuario. Datos invalidos %s.",this.getClass().getSimpleName(),this.newUsuario));
            System.err.println(String.format("rut valido?%s",this.usuarioValidator.isValidRut(rutText.getText())));
            System.err.println(String.format("nombre valido?%s",this.usuarioValidator.isValidNombres(nombresText.getText())));
            System.err.println(String.format("apellidos valido?%s",this.usuarioValidator.isValidApellidos(apellidosText.getText())));
            System.err.println(String.format("direccion valido?%s",this.usuarioValidator.isValidDireccion(direccionText.getText())));
            System.err.println(String.format("telefono valido?%s",this.usuarioValidator.isValidTelefono(telefonoText.getText())));
        }
        return registradoConExito;
    }
    
    @FXML
    private boolean agregaMedidorAction(ActionEvent event) {
        boolean addedSucess = false;
        boolean medidorIngresadoIsValid = false;
        Medidor medidor = new Medidor();
        medidor.setId(this.idMedidorText.getText());
        medidorIngresadoIsValid = !medidor.getId().isEmpty() &&MedidorValidator.isValid(medidor);
        this.medidorLabel.setVisible(!medidorIngresadoIsValid);
        if (medidorIngresadoIsValid && !this.newUsuario.getMedidoresObservable().contains(medidor)){
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
    
    public boolean isDirty(){
        return this.isDirty;
    }
    
    private void setDirty(boolean isDirty) {
        this.isDirty = isDirty;
    }

    @FXML
    private void resetMedidorIdAction(ActionEvent event) {
        this.idMedidorText.clear();
    }
}
