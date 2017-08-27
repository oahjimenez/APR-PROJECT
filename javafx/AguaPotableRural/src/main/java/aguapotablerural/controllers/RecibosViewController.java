 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.aguapotablerural.controllers;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import main.java.aguapotablerural.dao.repository.UsuarioRepository;
import main.java.aguapotablerural.dao.impl.UsuarioRepositoryImpl;
import main.java.aguapotablerural.database.contract.DBDriverManager;
import main.java.aguapotablerural.database.impl.SqliteDriverManager;
import main.java.aguapotablerural.model.LecturaMensual;
import main.java.aguapotablerural.model.Medidor;
import main.java.aguapotablerural.model.Usuario;
import main.java.aguapotablerural.services.LecturaService;
import main.java.aguapotablerural.services.MedidorService;

/**
 * FXML Controller class
 *
 * @author Sebastián
 */
public class RecibosViewController implements Initializable {
    
    private final static String UNIDAD_PAGO = "$";
    
    @FXML
    public Button lecturaViewButton;
    
    @FXML
    public ListView listViewUsuarios;
    
    
    @FXML
    public ListView listViewUsuariosIngresados;
    
    @FXML
    private MenuButton anoMenu;
    
    @FXML
    private MenuButton mesMenu;
    
    @FXML
    private Tab mesTab;
    
    @FXML 
    public TableView<LecturaMensual> lecturasMensualesTableView;
    private ObservableList<LecturaMensual> lecturas = FXCollections.observableArrayList();
    
    
    @FXML
    private TableColumn<LecturaMensual,String> medidorIdTableColumn;
    
    @FXML
    private TableColumn<LecturaMensual,String> lecturaTableColumn;
    
    @FXML
    private TableColumn<LecturaMensual,String> costoTableColumn;
    
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
    
    @FXML
    public Label totalMensualLabel;
    
    @FXML
    public Button guardarLecturasMensualesButton;
    
    private UsuarioRepository usuarioRepository;
    
    private ObservableList<Usuario> usuarios = FXCollections.observableArrayList();
    
    private MedidorService medidorService;
    private LecturaService lecturaService;
    
    private List<TextField> lecturasMedidoresTextFields;
    
    private List<? extends Medidor> medidoresDelMes;
    
    public RecibosViewController () {
        DBDriverManager driverManager = new SqliteDriverManager();
        this.usuarioRepository = new UsuarioRepositoryImpl(driverManager);
        this.medidorService = new MedidorService();
        this.lecturaService = new LecturaService();
        this.lecturasMedidoresTextFields = new ArrayList<>();
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lecturasMensualesTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        this.lecturasMensualesTableView.setItems(lecturas);
        this.lecturasMensualesTableView.setEditable(true);
       medidorIdTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty((cellData.getValue().getMedidor().getId())));
       lecturaTableColumn.setCellValueFactory(cellData -> {
           String lecturaValue = (cellData.getValue().getLectura()==0.0)? "Ingrese un valor" : String.valueOf(cellData.getValue().getLectura());
           return new SimpleStringProperty(lecturaValue);
        });
        lecturaTableColumn.setCellFactory(TextFieldTableCell.<LecturaMensual>forTableColumn());
        lecturaTableColumn.setOnEditCommit(t -> {
            double lecturaOld = !isNumeric(t.getOldValue()) ? 0.0 : Double.parseDouble(t.getOldValue());
            double lecturaNew = !isNumeric(t.getNewValue()) ? 0.0 : Double.parseDouble(t.getNewValue());
            String lecturaTotalStr = totalMensualLabel.getText().replace(UNIDAD_PAGO,"").trim();
            double lecturaTotal = !isNumeric(lecturaTotalStr) ? 0 : Double.parseDouble(lecturaTotalStr);
            System.out.println(String.format("old:%snew%s",t.getOldValue(),t.getNewValue()));
            totalMensualLabel.setText(String.format("%s %s",String.valueOf(lecturaTotal - lecturaOld + lecturaNew),UNIDAD_PAGO));
        });
        costoTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getCosto())));
        
        
        this.clearMedidoresUsuarioMensual();
        LocalDateTime now = LocalDateTime.now();
        
        
        String mesName =now.format(DateTimeFormatter.ofPattern("MMMM",new Locale("es", "ES")));
        this.mesMenu.setText(this.capitalize(mesName));
        this.anoMenu.setText(String.valueOf(now.getYear()));
        this.mesTab.setText(this.mesMenu.getText());
        for (MenuItem ano : this.anoMenu.getItems()){
            ano.setOnAction(action->{
                this.anoMenu.setText(ano.getText());
                this.mesTab.setText(String.format("%s %s",this.mesMenu.getText(),ano.getText()));
                this.actualizarMedidoresAnoMes((Usuario)listViewUsuarios.getSelectionModel().getSelectedItem());
                this.actualizarUsuariosIngresados(this.getSelectedMonthYear());
            });
        }
        for (MenuItem mes: this.mesMenu.getItems()) {
            mes.setOnAction(action-> {
                this.mesMenu.setText(mes.getText());
                this.mesTab.setText(String.format("%s %s",mes.getText(),this.anoMenu.getText()));
                this.actualizarMedidoresAnoMes((Usuario)listViewUsuarios.getSelectionModel().getSelectedItem());
                this.actualizarUsuariosIngresados(this.getSelectedMonthYear());
            });
        }
        
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
        this.listViewUsuariosIngresados.setCellFactory(cellFactory -> new ListCell<Usuario>() {
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
                actualizarMedidoresAnoMes(getUsuarioSeleccionado());
             }
        });
        this.listViewUsuariosIngresados.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                actualizarMedidoresAnoMes(getUsuarioIngresadoSeleccionado());
             }
        });
        this.actualizarUsuariosIngresados(getSelectedMonthYear());
    }    
    
    private LocalDate getSelectedMonthYear() {
        if (this.mesMenu.getText().isEmpty() || this.anoMenu.getText().isEmpty()) {
            return null;
        }
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .append(DateTimeFormatter.ofPattern("MMMM yyyy"))
            .toFormatter(new Locale("es", "ES"));
        TemporalAccessor parsed = formatter.parse(String.format("%s %s", this.mesMenu.getText(),this.anoMenu.getText()));
        return YearMonth.from(parsed).atEndOfMonth();
    }
    
    private void actualizarMedidoresAnoMes(Usuario usuario) {
        
                if (usuario==null) {
                    return;
                }
                medidoresDelMes = medidorService.getMedidoresOf(usuario,getSelectedMonthYear());
                lecturasMensualesTableView.getItems().clear();
                nombreLabel.setText(new StringBuilder().append(usuario.getNombres()).append(" ").append(usuario.getApellidos()).toString());
                rutLabel.setText(usuario.getRut());
                direccionLabel.setText(usuario.getDireccion());
                telefonoLabel.setText(usuario.getTelefono());
                if (medidoresDelMes.isEmpty()) {
                    Label noPoseeMedidorLabel = new Label();
                   // noPoseeMedidorLabel.setText("Sin registros");
                } else {
                    double totalMensual = 0.0;
                    for (int fila=0; fila < medidoresDelMes.size(); fila++) {
                        LecturaMensual lecturaMensual = new LecturaMensual();
                        lecturas.add(lecturaMensual);
                        Medidor medidor = medidoresDelMes.get(fila);
                        lecturaMensual.setMedidor(medidor);
                        double lecturaMedidor = lecturaService.obtenerLectura(getUsuarioSeleccionado(),this.medidoresDelMes.get(fila),getSelectedMonthYear());
                        if (lecturaMedidor!=-1) {
                            lecturaMensual.setLectura(lecturaMedidor);
                            totalMensual+=lecturaMedidor;
                        }
                        /*

                        TextField lecturaTextField = new TextField();
                        this.lecturasMedidoresTextFields.add(lecturaTextField);
                        medidoresUsuarioMensual.add(lecturaTextField,2,fila);
                        lecturaTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                            double lecturaOld = !this.isNumeric(oldValue) ? 0.0 : Double.parseDouble(oldValue);
                            double lecturaNew = !this.isNumeric(newValue) ? 0.0 : Double.parseDouble(newValue);;
                            String lecturaTotalStr = totalMensualLabel.getText().replace(unidadMedicion,"").trim();
                            double lecturaTotal = !this.isNumeric(lecturaTotalStr) ? 0 : Double.parseDouble(lecturaTotalStr);
                            System.out.println(String.format("old:%snew%s",oldValue,newValue));
                            totalMensualLabel.setText(String.format("%s %s",String.valueOf(lecturaTotal - lecturaOld + lecturaNew),unidadMedicion));
                        });
                        double lecturaMedidor = lecturaService.obtenerLectura(getUsuarioSeleccionado(),this.medidoresDelMes.get(fila),getSelectedMonthYear());
                        if (lecturaMedidor!=-1) {
                            totalMensual+=lecturaMedidor;
                            lecturaTextField.setText(String.valueOf(lecturaMedidor));
                        }
                        
                        Label pesosLabel = new Label();
                        pesosLabel.setText(String.format("%s %s","Sub Total",unidadMedicion));
                        medidoresUsuarioMensual.add(pesosLabel,3,fila);           */
                    }
                    totalMensualLabel.setText(String.format("%s %s",String.valueOf(totalMensual),UNIDAD_PAGO));
                
                }
    }
    
    private String capitalize(String text) {
        return new StringBuilder().append(text.substring(0, 1).toUpperCase()).append(text.substring(1)).toString();
    }
    
    private void clearMedidoresUsuarioMensual() {
        lecturasMensualesTableView.getItems().clear();
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
    
    private Usuario getUsuarioSeleccionado() {
        return (Usuario)this.listViewUsuarios.getSelectionModel().getSelectedItem();
    }
        
    private Usuario getUsuarioIngresadoSeleccionado() {
        return (Usuario)this.listViewUsuariosIngresados.getSelectionModel().getSelectedItem();
    }
    
    @FXML
    public void guardarLecturasMensualesAction(ActionEvent event){
        for (int i=0; i < this.medidoresDelMes.size(); i++) {
            if (this.lecturasMedidoresTextFields.get(i).getText().isEmpty()) continue;
            if (lecturaService.guardarLectura(getUsuarioSeleccionado(),this.medidoresDelMes.get(i),getSelectedMonthYear(),Double.parseDouble(this.lecturasMedidoresTextFields.get(i).getText()))) {
                if (!this.listViewUsuariosIngresados.getItems().contains(getUsuarioSeleccionado())) {
                    this.listViewUsuariosIngresados.getItems().add(getUsuarioSeleccionado());
                }
            }
        }
    }
    
    private void actualizarUsuariosIngresados(LocalDate fecha) {
        this.listViewUsuariosIngresados.getItems().clear();
        List<String> ruts = lecturaService.getRutUsuariosConLecturaMensual(fecha);
        ruts.forEach(rut-> {
            Usuario usuarioLectura = new Usuario();
            usuarioLectura.setRut(rut);
            int index = -1;
            if (((index = this.usuarios.indexOf(usuarioLectura)) != -1) && !this.listViewUsuariosIngresados.getItems().contains(this.usuarios.get(index))){
               this.listViewUsuariosIngresados.getItems().add(this.usuarios.get(index));
            }
        });
    }
    
    private boolean isNumeric(String charset) {
        return charset.matches("-?\\d+(\\.\\d*)?");  //match a number with optional '-' and decimal.
    }
    
}
