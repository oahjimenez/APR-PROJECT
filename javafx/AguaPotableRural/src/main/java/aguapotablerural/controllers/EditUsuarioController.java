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
import java.util.function.UnaryOperator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import main.java.aguapotablerural.dao.contract.UsuarioRepository;
import main.java.aguapotablerural.model.Medidor;
import main.java.aguapotablerural.model.Usuario;
import main.java.aguapotablerural.ui.LimitedTextField;

/**
 * FXML Controller class
 *
 * @author Sebastián
 */
public class EditUsuarioController implements Initializable {
    
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
    public Label medidorLabel;
    
    @FXML
    public Button editUsuarioButton;
    
    @FXML
    public Button removeMedidorButton;
    
    private Usuario usuarioEditable;
    private UsuarioRepository usuarioRepository;

    private static final int RUT_MAXCHAR = 12;
    private static final int NOMBRES_MAXCHAR = 40;
    private static final int APELLIDOS_MAXCHAR = 40;
    private static final int DIRECCION_MAXCHAR = 50;
    private static final int TELEFONO_MAXCHAR = 8;

    private DecimalFormat formatter;

    public EditUsuarioController(Usuario usuarioEditable,UsuarioRepository usuarioRepository) {
        this.usuarioEditable = usuarioEditable;
        this.usuarioRepository = usuarioRepository;
        
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
    
    private void validateAndSetRutText(String rut) {
        rut = rut.replace(".","").replace("-","");
        this.rutLabel.setVisible(!rut.matches("[0-9]*[kK]?"));
        rutText.setText(this.formatRut(rut));
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.rutText.setMaxLength(RUT_MAXCHAR);
        this.nombresText.setMaxLength(NOMBRES_MAXCHAR);
        this.apellidosText.setMaxLength(APELLIDOS_MAXCHAR);
        this.direccionText.setMaxLength(DIRECCION_MAXCHAR);
        this.telefonoText.setMaxLength(TELEFONO_MAXCHAR);

        this.rutText.setText(usuarioEditable.getRut());
        this.rutText.textProperty().addListener((observable, oldRut, newRut) -> {
            try {
                this.validateAndSetRutText(this.rutText.getText());
            } catch (NumberFormatException e) {
                this.rutLabel.setVisible(true);
            }
        });
        this.nombresText.setText(usuarioEditable.getNombres().toUpperCase());
        this.nombresText.textProperty().addListener((obs, oldNombre, newNombre) -> { 
                this.nombreLabel.setVisible(newNombre.isEmpty());
                this.nombresText.setText(newNombre.toUpperCase().replaceAll(" +", " "));
        });
        
        this.apellidosText.setText(usuarioEditable.getApellidos().toUpperCase());
        this.apellidosText.textProperty().addListener((obs, oldApellidos, newApellidos) -> {
            this.apellidosLabel.setVisible(newApellidos.isEmpty());
            this.apellidosText.setText(newApellidos.toUpperCase().replaceAll(" +", " "));
        });
        
        this.direccionText.setText(usuarioEditable.getDireccion().toUpperCase());
        this.direccionText.textProperty().addListener((obs, oldDireccion, newDireccion) -> {
            this.direccionLabel.setVisible(newDireccion.isEmpty());
            this.direccionText.setText(newDireccion.toUpperCase().replaceAll(" +", " "));
        });

        this.telefonoText.setText(usuarioEditable.getTelefono());
        this.telefonoText.textProperty().addListener((obs, oldTelefono, newTelefono) -> {
            this.telefonoLabel.setVisible(newTelefono.isEmpty() || (newTelefono.length() != TELEFONO_MAXCHAR) || !newTelefono.matches("[0-9]*"));
            this.telefonoText.setText(newTelefono);
        });
        
        this.idMedidorText.textProperty().addListener((obs, oldTelefono, newIdMedidor) -> {
            this.medidorLabel.setVisible(!isValidIdMedidor(newIdMedidor));
            this.idMedidorText.setText(newIdMedidor);
        });
        this.listViewMedidores.setItems(this.usuarioEditable.getMedidoresObservable());
    }
    
    private boolean isValidIdMedidor(String id) {
        return !id.isEmpty() && id.matches("[0-9]+");
    }

    public void setUsuarioRepository(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public void setUsuarioEditable(Usuario usuarioEditble) {
        this.usuarioEditable = usuarioEditable;
    }
    
    @FXML
    private boolean agregaMedidorAction(ActionEvent event) {
        this.medidorLabel.setVisible(!isValidIdMedidor(this.idMedidorText.getText()));
        Medidor medidor = new Medidor();
        medidor.setId(this.idMedidorText.getText());
        if (this.isValidIdMedidor(this.idMedidorText.getText()) && !this.usuarioEditable.getMedidoresObservable().contains(medidor)){
            return this.usuarioEditable.getMedidoresObservable().add(medidor);
        }
        return false;
    }
    
    @FXML
    private boolean eliminaMedidorAction(ActionEvent event) {
        Medidor medidor = this.listViewMedidores.getSelectionModel().getSelectedItem();
        return this.usuarioEditable.getMedidoresObservable().remove(medidor);
    }

    
    @FXML
    public void editarUsuarioAction(ActionEvent event){
        this.usuarioEditable.setNombres(nombresText.getText());
        this.usuarioEditable.setApellidos(apellidosText.getText());
        this.usuarioEditable.setDireccion(direccionText.getText());
        this.usuarioEditable.setTelefono(telefonoText.getText());
        this.usuarioRepository.save(usuarioEditable);
        Stage stage = (Stage) editUsuarioButton.getScene().getWindow();
        stage.close();
    }
}
