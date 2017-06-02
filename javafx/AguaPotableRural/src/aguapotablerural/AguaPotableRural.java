/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aguapotablerural;

import aguapotablerural.database.SQLiteJDBCDriverConnection;
import aguapotablerural.model.Administrador;
import aguapotablerural.model.Usuario;
import aguapotablerural.model.UsuarioManager;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author carlo
 */
public class AguaPotableRural extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();
        
        //root.getChildren().add(btn);
        
        //scene is created with the grid pane as the root node
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        
        final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 6);
        Scene scene = new Scene(grid, 600, 400);
        
        primaryStage.setTitle("Agua Potable Rural");
        primaryStage.setScene(scene);
        
       Text scenetitle = new Text("Registro de Usuario");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label userName = new Label("Nombre Usuario:");
        grid.add(userName, 0, 1);

        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);

        Label pw = new Label("Password:");
        grid.add(pw, 0, 2);

        PasswordField pwBox = new PasswordField();
        grid.add(pwBox, 1, 2);
       //  grid.setGridLinesVisible(true);
       
       Label direccionBox = new Label("Direccion:");
       grid.add(direccionBox,0,3);
       
       TextField direccionTextField = new TextField();
       grid.add(direccionTextField,1,3);
       
       Label telefonoBox = new Label("Telefono:");
       grid.add(telefonoBox, 0, 4);
       
       TextField telefonoTextField = new TextField();
       grid.add(telefonoTextField,1,4);
       
       Button btn = new Button("Sign in");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 5);
        
        btn.setOnAction(new EventHandler<ActionEvent>() {
 
        @Override
            public void handle(ActionEvent e) {
                actiontarget.setFill(Color.FIREBRICK);
                actiontarget.setText(userTextField.getText() + " ha sido registrado con éxito.");
                UsuarioManager usuarioManager = new UsuarioManager(new SQLiteJDBCDriverConnection());
                Usuario andres = new Usuario(new SQLiteJDBCDriverConnection());
                andres.setRut("25.773.171.9").setNombre("Oswaldo Andres").setDireccion("Av. San Martin, Santiago. Region Metropolitana 413, Dpto 306").setTelefono("961607765").setFechaRegistro(null);
                andres.save();
                
                Usuario carlos = new Usuario(new SQLiteJDBCDriverConnection());
                carlos.setRut("17.548.054-4").setNombre("Carlos Mardones").setDireccion("Nataniel Cox con Coquimbo 898, Samtiago Region Metropolitana").setTelefono("79373783").setFechaRegistro(null);
                carlos.save();
               // carlos.delete();
               
               Administrador inputUser = new Administrador(new SQLiteJDBCDriverConnection()).setPassword(pwBox.getText());
               inputUser.setRut("1-9").setNombre(userTextField.getText()).setDireccion(direccionTextField.getText()).setTelefono(telefonoTextField.getText());
               inputUser.save();
                System.out.println(usuarioManager.getAll());
            }
        });
       
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
