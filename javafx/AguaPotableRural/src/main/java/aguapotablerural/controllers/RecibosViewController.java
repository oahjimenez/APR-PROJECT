 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.aguapotablerural.controllers;

import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
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
import main.java.aguapotablerural.services.ConsumoService;
import main.java.aguapotablerural.services.LecturaService;
import main.java.aguapotablerural.services.MedidorService;

/**
 * FXML Controller class
 *
 * @author Sebastián
 * 
 * Edit Cell workouraround credit to James-d https://gist.github.com/james-d/be5bbd6255a4640a5357
 */
public class RecibosViewController implements Initializable {
    
    private final static String UNIDAD_PAGO = "$";
    private final static String LECTURAS_INGRESADAS_PREFIX = "Lecturas Ingresadas";
    
    @FXML
    public Button lecturaViewButton;
    
    @FXML
    public ListView listViewUsuarios;
    
    
    @FXML
    public ListView listViewUsuariosIngresados;
    
    @FXML
    private MenuButton fechaLecturaMensual;
    
    @FXML 
    public TableView<LecturaMensual> lecturasMensualesTableView;
    private ObservableList<LecturaMensual> lecturas = FXCollections.observableArrayList();
    
    
    @FXML
    private TableColumn<LecturaMensual,String> medidorIdTableColumn;
    
    @FXML
    private TableColumn<LecturaMensual,String> lecturaTableColumn;
    
    @FXML
    private TableColumn<LecturaMensual,String> lecturaAnteriorTableColumn;
    
    
    @FXML
    private TableColumn<LecturaMensual,String> consumoTableColumn;
    
    @FXML
    private TextField filtroUsuario;
    @FXML
    private TextField filtroUsuariosIngresados;
    
    @FXML
    public Label nombreLabelValue; 
    
    @FXML
    public Label rutLabel; 
    @FXML
    public Label rutLabelValue;
    
    @FXML
    public Label direccionLabel;
    
    @FXML
    public Label direccionLabelValue;
    
    @FXML
    public Label telefonoLabel;
    
    @FXML
    public Label telefonoLabelValue;
    
    @FXML
    public Label totalMensualLabel;
    
    @FXML
    public Label totalConsumoLabel;
    
    @FXML
    public Label formulaLabel;
    
    @FXML
    public Button guardarLecturasMensualesButton;
    
    @FXML
    public Label lecturasIngrsadasLabel;
    
    private UsuarioRepository usuarioRepository;
    
    private ObservableList<Usuario> usuarios = FXCollections.observableArrayList();
    private ObservableList<Usuario> usuariosIngresados = FXCollections.observableArrayList();
    
    private final MedidorService medidorService;
    private final LecturaService lecturaService;
    private final ConsumoService consumoService;
    
    private List<TextField> lecturasMedidoresTextFields;
    
    private List<? extends Medidor> medidoresDelMes;
    
    private Usuario usuarioSeleccionado;
    
    public RecibosViewController () {
        DBDriverManager driverManager = new SqliteDriverManager();
        this.usuarioRepository = new UsuarioRepositoryImpl(driverManager);
        this.medidorService = new MedidorService();
        this.lecturaService = new LecturaService();
        this.consumoService = new ConsumoService();
        this.lecturasMedidoresTextFields = new ArrayList<>();
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lecturasMensualesTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        this.setControllerWidgetsVisible(false);
        this.lecturasMensualesTableView.setItems(lecturas);
        this.lecturasMensualesTableView.setEditable(true);
        this.lecturasMensualesTableView.setPlaceholder(new Label("Sin medidores registrados"));
       medidorIdTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty((cellData.getValue().getMedidor().getId())));
       lecturaTableColumn.setCellValueFactory(cellData -> {
           String lecturaValue = (cellData.getValue().getLectura()==0.0)? "Ingrese un valor" : String.valueOf(cellData.getValue().getLectura());
           return new SimpleStringProperty(lecturaValue);
        });
        lecturaTableColumn.setCellFactory(TextFieldTableCell.<LecturaMensual>forTableColumn());
        lecturaTableColumn.setOnEditCommit(t -> {
            double lecturaOld = !isNumeric(t.getOldValue()) ? 0.0 : Double.parseDouble(t.getOldValue());
            double lecturaNew = !isNumeric(t.getNewValue()) ? 0.0 : Double.parseDouble(t.getNewValue());
            t.getRowValue().setLectura(lecturaNew);
            String lecturaTotalStr = totalMensualLabel.getText().replace(UNIDAD_PAGO,"").trim();
            double lecturaTotal = !isNumeric(lecturaTotalStr) ? 0 : Double.parseDouble(lecturaTotalStr);
            System.out.println(String.format("old:%snew%s",t.getOldValue(),t.getNewValue()));
            totalMensualLabel.setText(String.format("%s %s",String.valueOf(lecturaTotal - lecturaOld + lecturaNew),UNIDAD_PAGO));
        });
        lecturaAnteriorTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getCosto())));
        
        
        this.clearMedidoresUsuarioMensual();
        LocalDate now = LocalDate.now();
        
       // String mesName =this.getSpanishMonth(now);
        
        /* Agrega los meses del año actual */

        MenuItem[] meses = {
            new MenuItem(this.getSpanishMonth(now.getMonth().JANUARY)+" "+String.valueOf(now.getYear())),
            new MenuItem(this.getSpanishMonth(now.getMonth().FEBRUARY)+" "+String.valueOf(now.getYear())),
            new MenuItem(this.getSpanishMonth(now.getMonth().MARCH)+" "+String.valueOf(now.getYear())),
            new MenuItem(this.getSpanishMonth(now.getMonth().APRIL)+" "+String.valueOf(now.getYear())),
            new MenuItem(this.getSpanishMonth(now.getMonth().MAY)+" "+String.valueOf(now.getYear())),
            new MenuItem(this.getSpanishMonth(now.getMonth().JUNE)+" "+String.valueOf(now.getYear())),
            new MenuItem(this.getSpanishMonth(now.getMonth().JULY)+" "+String.valueOf(now.getYear())),
            new MenuItem(this.getSpanishMonth(now.getMonth().AUGUST)+" "+String.valueOf(now.getYear())),
            new MenuItem(this.getSpanishMonth(now.getMonth().SEPTEMBER)+" "+String.valueOf(now.getYear())),
            new MenuItem(this.getSpanishMonth(now.getMonth().OCTOBER)+" "+String.valueOf(now.getYear())),
            new MenuItem(this.getSpanishMonth(now.getMonth().NOVEMBER)+" "+String.valueOf(now.getYear())),
            new MenuItem(this.getSpanishMonth(now.getMonth().DECEMBER)+" "+String.valueOf(now.getYear()))
        };
        fechaLecturaMensual.getItems().addAll(Arrays.asList(meses));
        fechaLecturaMensual.setText(this.getSpanishMonth(now)+" "+String.valueOf(now.getYear()));
        for (MenuItem mesAno : this.fechaLecturaMensual.getItems()){
            mesAno.setOnAction(action->{
                this.fechaLecturaMensual.setText(mesAno.getText());
                this.actualizarMedidoresAnoMes((Usuario)listViewUsuarios.getSelectionModel().getSelectedItem());
                this.cargarListasUsuarios(this.getSelectedMonthYear());
                this.actualizaLecturaIngresadaLabel(this.getSelectedMonthYear());
            });
        }
        
        /*this.mesMenu.setText(mesName);
        this.anoMenu.setText(String.valueOf(now.getYear()));
        this.mesTab.setText(this.mesMenu.getText());
        for (MenuItem ano : this.anoMenu.getItems()){
            ano.setOnAction(action->{
                this.anoMenu.setText(ano.getText());
                this.mesTab.setText(String.format("%s %s",this.mesMenu.getText(),ano.getText()));
                this.actualizarMedidoresAnoMes((Usuario)listViewUsuarios.getSelectionModel().getSelectedItem());
                this.cargarListasUsuarios(this.getSelectedMonthYear());
                this.actualizaLecturaIngresadaLabel(this.getSelectedMonthYear());
            });
        }
        for (MenuItem mes: this.mesMenu.getItems()) {
            mes.setOnAction(action-> {
                this.mesMenu.setText(mes.getText());
                this.mesTab.setText(String.format("%s %s",mes.getText(),this.anoMenu.getText()));
                this.actualizarMedidoresAnoMes((Usuario)listViewUsuarios.getSelectionModel().getSelectedItem());
                this.cargarListasUsuarios(this.getSelectedMonthYear());
                this.actualizaLecturaIngresadaLabel(this.getSelectedMonthYear());
            });
        }*/
        this.cargarListasUsuarios(now);
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
        FilteredList<Usuario> usuariosIngresadosFiltrados = new FilteredList<>(usuariosIngresados,u -> true);
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
        this.filtroUsuariosIngresados.textProperty().addListener((observable, oldValue, newValue) -> {
            usuariosIngresadosFiltrados.setPredicate(usuario -> {
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
        SortedList<Usuario> usuariosOrdenados = new SortedList<>(usuariosFiltrados).sorted();
        this.listViewUsuarios.setItems(usuariosOrdenados);
        
        SortedList<Usuario> usuariosIngresadosOrdenados = new SortedList<>(usuariosIngresadosFiltrados).sorted();
        this.listViewUsuariosIngresados.setItems(usuariosIngresadosOrdenados);

        this.listViewUsuarios.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                setControllerWidgetsVisible(true);
                setUsuarioSeleccionado((Usuario)listViewUsuarios.getSelectionModel().getSelectedItem());
                actualizarMedidoresAnoMes(getUsuarioSeleccionado());
             }
        });
        this.listViewUsuariosIngresados.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                setUsuarioSeleccionado((Usuario)listViewUsuariosIngresados.getSelectionModel().getSelectedItem());
                actualizarMedidoresAnoMes(getUsuarioSeleccionado());
             }
        });
        this.cargarListasUsuarios(getSelectedMonthYear());
    }    
    
    private LocalDate getSelectedMonthYear() {
        if (this.fechaLecturaMensual.getText().isEmpty()) {
            return null;
        }
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .append(DateTimeFormatter.ofPattern("MMMM yyyy"))
            .toFormatter(new Locale("es", "ES"));
        TemporalAccessor parsed = formatter.parse(fechaLecturaMensual.getText());
        return YearMonth.from(parsed).atEndOfMonth();
    }
    
    private void actualizarMedidoresAnoMes(Usuario usuario) {
        
                if (usuario==null) {
                    return;
                }
                medidoresDelMes = medidorService.getMedidoresOf(usuario,getSelectedMonthYear());
                lecturasMensualesTableView.getItems().clear();
                nombreLabelValue.setText(new StringBuilder().append(usuario.getNombres()).append(" ").append(usuario.getApellidos()).toString());
                nombreLabelValue.getStyleClass().add("text-bigger");
                rutLabelValue.setText(usuario.getRut());
                direccionLabelValue.setText(usuario.getDireccion());
                telefonoLabelValue.setText(usuario.getTelefono());
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
                        double lecturaMedidor = lecturaService.obtenerLectura(getUsuarioSeleccionado(),medidor,getSelectedMonthYear());
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
                        double lecturaMedidor = lecturaService.obtenerLectura(getUsuarioSeleccionado(),this.medidoresDelMes.getActive(fila),getSelectedMonthYear());
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
    
    private void actualizaLecturaIngresadaLabel(LocalDate fecha) {
        // this.lecturasIngrsadasLabel.setText(String.format("%s (%s %s)",LECTURAS_INGRESADAS_PREFIX,this.getSpanishMonth(fecha),this.getYear(fecha)));
 
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
    
    private void setUsuarioSeleccionado(Usuario usuario) {
        this.usuarioSeleccionado = usuario;
    }
    
    private Usuario getUsuarioSeleccionado() {
        return this.usuarioSeleccionado;
        
    }
    
    @FXML
    public void guardarLecturasMensualesAction(ActionEvent event){
        for (LecturaMensual lectura : this.lecturas) {
            if (lecturaService.guardarLectura(getUsuarioSeleccionado(),lectura.getMedidor(),getSelectedMonthYear(),lectura.getLectura())) {
                System.out.println(String.format("%s - guardarLecturasMensualesAction() : Guardando datos user<%s> medidor<%s> fecha<%s> lectura<%s>",this.getClass().getSimpleName(),getUsuarioSeleccionado().getNombres(),lectura.getMedidor().getId(),getSelectedMonthYear(),lectura.getLectura()));
                if (!this.listViewUsuariosIngresados.getItems().contains(getUsuarioSeleccionado())) {
                    this.listViewUsuariosIngresados.getItems().add(getUsuarioSeleccionado());
                }
            }
        }
        Usuario usuario = this.getUsuarioSeleccionado();
        formulaLabel.setText(this.consumoService.getFormulaValorACancelarConSubsidio(usuario,this.getSelectedMonthYear()));
        totalConsumoLabel.setText(String.valueOf(this.consumoService.getConsumoMensual(usuario,this.getSelectedMonthYear())));
        totalMensualLabel.setText(String.valueOf(this.consumoService.getValorACancelarConSubsidio(usuario,this.getSelectedMonthYear())));
    }
    
    private List<Usuario> obtenerUsuariosLecturaMensual(LocalDate fecha) {
        List<Integer> usuarioIds = lecturaService.getIdUsuariosConLecturaMensual(fecha);
        System.out.println("Ids de usuario con lectura mes:"+fecha+" "+usuarioIds);
        List<Usuario> usuariosConLecturaMensual = this.usuarios.stream().filter(usuario -> usuarioIds.contains(usuario.getId())).collect(Collectors.toList());
        return usuariosConLecturaMensual;
    }
    
    private void cargarUsuariosActivos(LocalDate fecha){  
        this.usuarios.clear();
        this.usuarios.addAll(usuarioRepository.getActiveUsuarios());
    }
    
    
    private void cargarListasUsuarios(LocalDate fecha) {
        this.cargarUsuariosActivos(fecha);
        this.usuariosIngresados.clear();
        List<Usuario> usuariosConLecturaMensual = this.obtenerUsuariosLecturaMensual(fecha);
        this.moverUsuariosListaIngresados(usuariosConLecturaMensual);
    }
    
    private void moverUsuariosListaIngresados(List<Usuario> usuarios) {
        usuarios.forEach((Usuario usuario)-> {
            System.out.println(usuario+"en listaingresados?" + this.usuarios.indexOf(usuario));
            if ((!this.usuariosIngresados.contains(usuario))){
                this.usuarios.remove(usuario);
                this.usuariosIngresados.add(usuario);
            }
        });
    }
    
    private String getSpanishMonth(LocalDate fecha) {
       return this.capitalize(this.getSpanishMonth(fecha.getMonth()));
    }
    
    private String getSpanishMonth(Month month) {
           return this.capitalize(month.getDisplayName(TextStyle.FULL,new Locale("es", "ES")));
    }
    
    private String getYear(LocalDate fecha){
        return String.valueOf(fecha.getYear());
    }
    
    private boolean isNumeric(String charset) {
        return charset.matches("-?\\d+(\\.\\d*)?");  //match a number with optional '-' and decimal.
    }
    
    private void setControllerWidgetsVisible(boolean visible) {
        rutLabel.setVisible(visible);
        telefonoLabel.setVisible(visible);
        direccionLabel.setVisible(visible);
    }
    
}
