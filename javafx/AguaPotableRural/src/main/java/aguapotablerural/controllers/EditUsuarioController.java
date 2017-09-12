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

    public EditUsuarioController(Usuario usuarioEditable,UsuarioRepository usuarioRepository) {
        this.usuarioEditable = usuarioEditable;
        this.usuarioRepository = usuarioRepository;
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
                this.rutLabel.setVisible(!UsuarioValidator.isValidRut(rut));
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
                this.nombreLabel.setVisible(!UsuarioValidator.isValidNombres(newNombre));
                this.nombresText.setText(newNombre.toUpperCase());
        });
        
        this.apellidosText.setText(usuarioEditable.getApellidos().toUpperCase());
        this.apellidosText.textProperty().addListener((obs, oldApellidos, newApellidos) -> {
            this.apellidosLabel.setVisible(!UsuarioValidator.isValidApellidos(newApellidos));
            this.apellidosText.setText(newApellidos.toUpperCase());
        });
        
        this.direccionText.setText(usuarioEditable.getDireccion().toUpperCase());
        this.direccionText.textProperty().addListener((obs, oldDireccion, newDireccion) -> {
            this.direccionLabel.setVisible(!UsuarioValidator.isValidDireccion(newDireccion));
            this.direccionText.setText(newDireccion.toUpperCase());
        });

        this.telefonoText.setText(usuarioEditable.getTelefono());
        this.telefonoText.textProperty().addListener((obs, oldTelefono, newTelefono) -> {
            this.telefonoLabel.setVisible(!UsuarioValidator.isValidTelefono(newTelefono));
            this.telefonoText.setText(newTelefono);
        });
        
        this.idMedidorText.textProperty().addListener((obs, oldTelefono, newIdMedidor) -> {
            this.idMedidorLabel.setVisible(!MedidorValidator.isValidId(newIdMedidor));
            this.idMedidorText.setText(newIdMedidor);
        });
      
        this.rutLabel.setVisible(!UsuarioValidator.isValidRut(this.rutText.getText()));
        this.nombreLabel.setVisible(!UsuarioValidator.isValidNombres(this.nombresText.getText()));
        this.apellidosLabel.setVisible(!UsuarioValidator.isValidApellidos(this.apellidosText.getText()));
        this.direccionLabel.setVisible(!UsuarioValidator.isValidDireccion(this.direccionText.getText()));
        this.telefonoLabel.setVisible(!UsuarioValidator.isValidTelefono(this.telefonoText.getText()));   
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
        Usuario usuarioEditable = new Usuario();
        usuarioEditable.setRut(cleanRut(rutText.getText()));
        usuarioEditable.setNombres(nombresText.getText());
        usuarioEditable.setApellidos(apellidosText.getText());
        usuarioEditable.setDireccion(direccionText.getText());
        usuarioEditable.setTelefono(telefonoText.getText());
        usuarioRepository.save(usuarioEditable);
        Usuario usuarioRut = usuarioService.getUsuario(cleanRut(rutText.getText()));
        boolean existeOtroUsuarioConRut = (usuarioRut!=null) && (this.usuarioEditable.getId()!=usuarioRut.getId());
        System.err.println("existeOtroUsuarioConRut:<"+existeOtroUsuarioConRut+">,id editable:"+usuarioEditable.getId());
        System.err.println("query usuario:"+usuarioRut);
        if (existeOtroUsuarioConRut) {
            this.rutLabel.setText(ERROR_MSG_RUT_EXISTENTE);
            this.rutLabel.setVisible(true);
        } 
        this.rutLabel.setVisible(existeOtroUsuarioConRut);
        
        if (UsuarioValidator.isValid(usuarioEditable) && !existeOtroUsuarioConRut) {    
            this.usuarioEditable.setRut(usuarioEditable.getRut());
            this.usuarioEditable.setNombres(usuarioEditable.getNombres());
            this.usuarioEditable.setApellidos(usuarioEditable.getApellidos());
            this.usuarioEditable.setDireccion(usuarioEditable.getDireccion());
            this.usuarioEditable.setTelefono(usuarioEditable.getTelefono());
            this.usuarioRepository.save(usuarioEditable);
            Stage stage = (Stage) editUsuarioButton.getScene().getWindow();
            stage.close();
        } else {
            Alert alert= new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText(null);
            alert.setContentText("Existen datos incompletos o mal ingresados");
            alert.showAndWait();
            System.err.println(String.format("%s - editarUsuarioAction(): fallo edicion de usuario. Datos invalidos %s.",this.getClass().getSimpleName(),this.usuarioEditable));
            System.err.println(String.format("rut valido?%s",UsuarioValidator.isValidRut(rutText.getText())));
            System.err.println(String.format("nombre valido?%s",UsuarioValidator.isValidNombres(nombresText.getText())));
            System.err.println(String.format("apellidos valido?%s",UsuarioValidator.isValidApellidos(apellidosText.getText())));
            System.err.println(String.format("direccion valido?%s",UsuarioValidator.isValidDireccion(direccionText.getText())));
            System.err.println(String.format("telefono valido?%s",UsuarioValidator.isValidTelefono(telefonoText.getText())));
            System.err.println("usuario valid?"+UsuarioValidator.isValid(this.usuarioEditable));
        }
    }
}
