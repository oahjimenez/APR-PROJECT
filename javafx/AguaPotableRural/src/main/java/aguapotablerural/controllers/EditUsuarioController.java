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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.java.aguapotablerural.dao.repository.UsuarioRepository;
import main.java.aguapotablerural.model.Medidor;
import main.java.aguapotablerural.model.Usuario;
import main.java.aguapotablerural.model.validator.MedidorValidator;
import main.java.aguapotablerural.model.validator.UsuarioValidator;
import main.java.aguapotablerural.services.UsuarioService;
import main.java.aguapotablerural.ui.LimitedTextField;

/**
 * FXML Controller class
 *
 * @author Sebastián
 */
public class EditUsuarioController implements Initializable {
    
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
    private TextField idMedidorText;
    
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
    public Label idMedidorLabel;
    
    @FXML
    public Button editUsuarioButton;
    
    @FXML
    public Button removeMedidorButton;
    
    private Usuario usuarioEditable;
    private UsuarioRepository usuarioRepository;

    private DecimalFormat formatter;
    
    private UsuarioService usuarioService;
    private UsuarioValidator usuarioValidator;
    private TableView<Usuario> tableViewUsuarios;
    
    public EditUsuarioController(Usuario usuarioEditable,UsuarioRepository usuarioRepository,TableView tableViewUsuarios) {
        this.usuarioEditable = usuarioEditable;
        this.usuarioRepository = usuarioRepository;
        this.usuarioService = new UsuarioService();
        this.usuarioValidator = new UsuarioValidator();
        this.usuarioValidator.setRutMandatory(true);
        this.usuarioValidator.setNombresMandatory(true);
        this.usuarioValidator.setApellidosMandatory(true);
        this.tableViewUsuarios = tableViewUsuarios;
        
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
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.rutText.setMaxLength(UsuarioValidator.RUT_MAXCHAR);
        this.nombresText.setMaxLength(UsuarioValidator.NOMBRES_MAXCHAR);
        this.apellidosText.setMaxLength(UsuarioValidator.APELLIDOS_MAXCHAR);
        this.direccionText.setMaxLength(UsuarioValidator.DIRECCION_MAXCHAR);
        this.telefonoText.setMaxLength(UsuarioValidator.TELEFONO_MAXCHAR);

        this.rutText.setText(this.formatRut(usuarioEditable.getRut().replace(".","").replace("-","")));
        this.rutText.textProperty().addListener((observable, oldRut, newRut) -> {
            try {
                String rut = newRut.replace(".","").replace("-","");
                this.rutLabel.setVisible(!this.usuarioValidator.isValidRut(rut));
                if (this.rutLabel.isVisible()) { //necesario para restituir mesaje de rut invalido cuando se arroja error cuando rut ya existe
                    this.rutLabel.setText(ERROR_MSG_RUT_INVALIDO);
                }
                rutText.setText(this.formatRut(rut));
            } catch (NumberFormatException e) {
                this.rutLabel.setVisible(true);
            }
        });
        this.nombresText.setText(usuarioEditable.getNombres().toUpperCase());
        this.nombresText.textProperty().addListener((obs, oldNombre, newNombre) -> { 
                this.nombreLabel.setVisible(!this.usuarioValidator.isValidNombres(newNombre));
                this.nombresText.setText(newNombre.toUpperCase());
        });
        
        this.apellidosText.setText(usuarioEditable.getApellidos().toUpperCase());
        this.apellidosText.textProperty().addListener((obs, oldApellidos, newApellidos) -> {
            this.apellidosLabel.setVisible(!this.usuarioValidator.isValidApellidos(newApellidos));
            this.apellidosText.setText(newApellidos.toUpperCase());
        });
        
        this.direccionText.setText(usuarioEditable.getDireccion().toUpperCase());
        this.direccionText.textProperty().addListener((obs, oldDireccion, newDireccion) -> {
            this.direccionLabel.setVisible(!this.usuarioValidator.isValidDireccion(newDireccion));
            this.direccionText.setText(newDireccion.toUpperCase());
        });

        this.telefonoText.setText(usuarioEditable.getTelefono());
        this.telefonoText.textProperty().addListener((obs, oldTelefono, newTelefono) -> {
            this.telefonoLabel.setVisible(!this.usuarioValidator.isValidTelefono(newTelefono));
            this.telefonoText.setText(newTelefono);
        });
        
        this.idMedidorText.textProperty().addListener((obs, oldTelefono, newIdMedidor) -> {
            this.idMedidorLabel.setVisible(!MedidorValidator.isValidId(newIdMedidor));
            this.idMedidorText.setText(newIdMedidor);
        });
      
        this.rutLabel.setVisible(!this.usuarioValidator.isValidRut(this.rutText.getText()));
        this.nombreLabel.setVisible(!this.usuarioValidator.isValidNombres(this.nombresText.getText()));
        this.apellidosLabel.setVisible(!this.usuarioValidator.isValidApellidos(this.apellidosText.getText()));
        this.direccionLabel.setVisible(!this.usuarioValidator.isValidDireccion(this.direccionText.getText()));
        this.telefonoLabel.setVisible(!this.usuarioValidator.isValidTelefono(this.telefonoText.getText()));   
        this.idMedidorLabel.setVisible(false); //porque es opcional
        this.listViewMedidores.setItems(this.usuarioEditable.getMedidoresObservable());
    }

    public void setUsuarioRepository(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public void setUsuarioEditable(Usuario usuarioEditable) {
        this.usuarioEditable = usuarioEditable;
    }
    
    @FXML
    private boolean agregaMedidorAction(ActionEvent event) {
        boolean addedSucess = false;
        Medidor medidor = new Medidor();
        medidor.setId(this.idMedidorText.getText());
        this.idMedidorLabel.setVisible(!MedidorValidator.isValid(medidor));
        if (MedidorValidator.isValid(medidor) && !this.usuarioEditable.getMedidoresObservable().contains(medidor)){
            addedSucess = this.usuarioEditable.getMedidoresObservable().add(medidor);
            if (addedSucess) { 
                this.idMedidorText.setText(""); 
            } //limpia campo de id medidor despues de ingresado
        }
        return addedSucess;
    }
    
    @FXML
    private boolean eliminaMedidorAction(ActionEvent event) {
        Medidor medidor = this.listViewMedidores.getSelectionModel().getSelectedItem();
        return this.usuarioEditable.getMedidoresObservable().remove(medidor);
    }
    
    private String cleanRut(String rut){
        return rut.replace(".","");
    }
    
    @FXML
    public void editarUsuarioAction(ActionEvent event){        
        Usuario _usuarioEditable = new Usuario();
        _usuarioEditable.setRut(cleanRut(rutText.getText()));
        _usuarioEditable.setNombres(nombresText.getText());
        _usuarioEditable.setApellidos(apellidosText.getText());
        _usuarioEditable.setDireccion(direccionText.getText());
        _usuarioEditable.setTelefono(telefonoText.getText());
        Usuario usuarioRut = usuarioService.getUsuario(cleanRut(rutText.getText()));
        boolean existeOtroUsuarioConRut = ((usuarioRut!=null) && (usuarioRut.getFechaRetiro()==null) && (this.usuarioEditable.getId()!=usuarioRut.getId()));
        if (usuarioRut!=null) {
        System.err.println("existeOtroUsuarioConRut:<"+existeOtroUsuarioConRut+">,id editable:"+this.usuarioEditable.getId() + "id usuario existent"+usuarioRut.getId());
        }
        System.err.println("query usuario:"+usuarioRut);
        if (existeOtroUsuarioConRut) {
            this.rutLabel.setText(ERROR_MSG_RUT_EXISTENTE);
            this.rutLabel.setVisible(true);
        } 
        this.rutLabel.setVisible(existeOtroUsuarioConRut || !this.usuarioValidator.isValidRut(_usuarioEditable.getRut()));
        
        if (this.usuarioValidator.isValid(_usuarioEditable) && !existeOtroUsuarioConRut) {    
            this.usuarioEditable.setRut(_usuarioEditable.getRut());
            this.usuarioEditable.setNombres(_usuarioEditable.getNombres());
            this.usuarioEditable.setApellidos(_usuarioEditable.getApellidos());
            this.usuarioEditable.setDireccion(_usuarioEditable.getDireccion());
            this.usuarioEditable.setTelefono(_usuarioEditable.getTelefono());
            this.usuarioRepository.save(this.usuarioEditable);
            this.tableViewUsuarios.refresh();
            Stage stage = (Stage) editUsuarioButton.getScene().getWindow();
            stage.close();
        } else {
            if ((_usuarioEditable.getRut()==null || _usuarioEditable.getRut().isEmpty()) && this.usuarioValidator.isRutMandatory()) {
                this.rutLabel.setText(ERROR_MSG_CAMPO_OBLIGATORIO);
                this.rutLabel.setVisible(true);
            }
            
            if ((_usuarioEditable.getNombres()==null || _usuarioEditable.getNombres().isEmpty()) && this.usuarioValidator.isNombresMandatory()) {
                this.nombreLabel.setText(ERROR_MSG_CAMPO_OBLIGATORIO);
                this.nombreLabel.setVisible(true);
            }
            
            if ((_usuarioEditable.getApellidos()==null || _usuarioEditable.getApellidos().isEmpty()) && this.usuarioValidator.isApellidosMandatory()) {
                this.apellidosLabel.setText(ERROR_MSG_CAMPO_OBLIGATORIO);
                this.apellidosLabel.setVisible(true);
            }
            
            Alert alert= new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText(null);
            alert.setContentText("Existen datos incompletos o mal ingresados");
            alert.showAndWait();
            System.err.println(String.format("%s - editarUsuarioAction(): fallo edicion de usuario. Datos invalidos %s.",this.getClass().getSimpleName(),this.usuarioEditable));
            System.err.println(String.format("rut valido?%s",this.usuarioValidator.isValidRut(rutText.getText())));
            System.err.println(String.format("nombre valido?%s",this.usuarioValidator.isValidNombres(nombresText.getText())));
            System.err.println(String.format("apellidos valido?%s",this.usuarioValidator.isValidApellidos(apellidosText.getText())));
            System.err.println(String.format("direccion valido?%s",this.usuarioValidator.isValidDireccion(direccionText.getText())));
            System.err.println(String.format("telefono valido?%s",this.usuarioValidator.isValidTelefono(telefonoText.getText())));
            System.err.println("usuario valid?"+this.usuarioValidator.isValid(this.usuarioEditable));
        }
    }
}
