 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.aguapotablerural.controllers;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import main.java.aguapotablerural.dao.repository.MedidorRepository;
import main.java.aguapotablerural.dao.repository.UsuarioRepository;
import main.java.aguapotablerural.dao.impl.MedidorRepositoryImpl;
import main.java.aguapotablerural.dao.impl.UsuarioRepositoryImpl;
import main.java.aguapotablerural.database.contract.DBDriverManager;
import main.java.aguapotablerural.database.impl.SqliteDriverManager;
import main.java.aguapotablerural.model.Medidor;
import main.java.aguapotablerural.model.Usuario;
import main.java.aguapotablerural.services.MedidorService;

/**
 * FXML Controller class
 *
 * @author Sebastián
 */
public class RecibosViewController implements Initializable {
    
    @FXML
    public Button lecturaViewButton;
    
    @FXML
    public ListView listViewUsuarios;
    
    @FXML
    private TextField anoText;
    
    @FXML
    private TextField mesText;
    
    @FXML
    private Tab mesTab;
    
    @FXML 
    public GridPane medidoresUsuarioMensual;
    
    @FXML
    private TextField filtroUsuario;
    
    @FXML
    public Label nombreLabel; 
    
    @FXML
    public Label rutLabel;
    
    @FXML
    public Label direccionLabel;
     
    @FXML
    public Label telefonoLabel;
    
    private UsuarioRepository usuarioRepository;
    private MedidorRepository medidorRepository;
    
    private ObservableList<Usuario> usuarios = FXCollections.observableArrayList();
    
    public RecibosViewController () {
        DBDriverManager driverManager = new SqliteDriverManager();
        this.usuarioRepository = new UsuarioRepositoryImpl(driverManager);
        this.medidorRepository = new MedidorRepositoryImpl(driverManager);
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        LocalDateTime now = LocalDateTime.now();
        MedidorService medidorService = new MedidorService();
        
        
        String mesName =now.format(DateTimeFormatter.ofPattern("MMMM",new Locale("es", "ES")));
        this.anoText.setText(String.valueOf(now.getYear()));
        this.mesText.setText(mesName);
        this.mesTab.setText(mesName);
        
        this.usuarios.addAll(usuarioRepository.getActiveUsuarios());
        this.listViewUsuarios.setCellFactory(cellFactory -> new ListCell<Usuario>() {
            @Override
            public void updateItem(Usuario item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    String text = new StringBuilder().append(item.getNombres()).append(" ").append(item.getApellidos()).toString();
                    setText(text);
                }
            }
        });
        
        // Wrap ObservableList in a FilteredList
        FilteredList<Usuario> usuariosFiltrados = new FilteredList<>(usuarios,u -> true);
         //Set filter Predicate whenever the filter changes
        this.filtroUsuario.textProperty().addListener((observable, oldValue, newValue) -> {
            usuariosFiltrados.setPredicate(usuario -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                return (usuario.getRut().toLowerCase().contains(lowerCaseFilter) || 
                        usuario.getNombres().toLowerCase().contains(lowerCaseFilter) ||
                        usuario.getApellidos().toLowerCase().contains(lowerCaseFilter) ||
                        usuario.getDireccion().toLowerCase().contains(lowerCaseFilter));
            });
        });
        
        //Wrap FilteredList in a SortedList
        SortedList<Usuario> usuariosOrdenados = new SortedList<>(usuariosFiltrados);
        this.listViewUsuarios.setItems(usuariosOrdenados);
        this.listViewUsuarios.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Usuario usuario = (Usuario)listViewUsuarios.getSelectionModel().getSelectedItem();
                if (usuario==null) {
                    return;
                }
                List<? extends Medidor> medidoresDelMes = medidorService.getMedidoresOf(usuario,getMonthYear());
                medidoresUsuarioMensual.getChildren().clear();
                nombreLabel.setText(new StringBuilder().append(usuario.getNombres()).append(" ").append(usuario.getApellidos()).toString());
                rutLabel.setText(usuario.getRut());
                direccionLabel.setText(usuario.getDireccion());
                telefonoLabel.setText(usuario.getTelefono());
                if (medidoresDelMes.isEmpty()) {
                    Label noPoseeMedidorLabel = new Label();
                    noPoseeMedidorLabel.setText("Sin registros");
                    medidoresUsuarioMensual.add(noPoseeMedidorLabel,0,0);
                } else {
                    for (int fila=0; fila < medidoresDelMes.size(); fila++) {
                        Medidor medidor = medidoresDelMes.get(fila);
                        Label medidorIndexLabel = new Label();
                        medidorIndexLabel.setText(new StringBuilder().append(String.valueOf(fila+1)).append(".").toString());
                        medidoresUsuarioMensual.add(medidorIndexLabel,0,fila);

                        Label medidorIdLabel = new Label();
                        medidorIdLabel.setText(new StringBuilder().append("Medidor ID").append(" ").append(medidor.getId()).toString());
                        medidoresUsuarioMensual.add(medidorIdLabel,1,fila);

                        TextField lecturaTextField = new TextField();
                        medidoresUsuarioMensual.add(lecturaTextField,2,fila);

                        Label pesosLabel = new Label();
                        pesosLabel.setText("$ Sub Total");
                        medidoresUsuarioMensual.add(pesosLabel,3,fila);           
                    }

                    Label totalLabel = new Label();
                    totalLabel.setText("Total");
                    medidoresUsuarioMensual.add(totalLabel,0,usuario.getMedidoresObservable().size());

                    Label totalPesosLabel = new Label();
                    totalPesosLabel.setText("$ TOTAL");
                    medidoresUsuarioMensual.add(totalPesosLabel,3,usuario.getMedidoresObservable().size());
                }
             }
        });
    }    
    
    private LocalDate getMonthYear() {
        if (this.mesText.getText().isEmpty() || this.anoText.getText().isEmpty()) {
            return null;
        }
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .append(DateTimeFormatter.ofPattern("MMMM yyyy"))
            .toFormatter(new Locale("es", "ES"));
        TemporalAccessor parsed = formatter.parse(String.format("%s %s", this.mesText.getText(),this.anoText.getText()));
        return YearMonth.from(parsed).atEndOfMonth();
    }
    
    @FXML
    private void addLecturaButton(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("main/resources/layouts/AddLectura.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));  
            Stage prevStage = (Stage) lecturaViewButton.getScene().getWindow();
            prevStage.close();
            stage.show();
        } catch(Exception e) {
           e.printStackTrace();
        }
    } 
    
}
