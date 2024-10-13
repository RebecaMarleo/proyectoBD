package org.example.proyecto_2formularios.Controller;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.apache.commons.codec.digest.DigestUtils;
import org.example.proyecto_2formularios.DAO.EmpleadoDAO;
import org.example.proyecto_2formularios.domain.Empleado;
import org.example.proyecto_2formularios.util.AlertUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AppController {

    public TextField txtUsuario, txtContrasena;
    public Label lblEstado;
    public Button btnEntrar;

    private final EmpleadoDAO empleadoDAO;

    public AppController() {
        empleadoDAO = new EmpleadoDAO();
        try {
            empleadoDAO.conectar();
        } catch (SQLException sqle) {
            AlertUtils.mostrarError("Error al conectar con la base de datos");
        } catch (ClassNotFoundException cnfe) {
            AlertUtils.mostrarError("Error al iniciar la aplicación");
        } catch (IOException ioe) {
            AlertUtils.mostrarError("Error al cargar la configuración");
        }

        System.out.println(System.getProperty("user.home"));
    }

    private void limpiarCajas() {
        txtUsuario.setText("");
        txtContrasena.setText("");
        txtUsuario.requestFocus();
    }

    @FXML
    public void iniciarSesion(Event event) {
        try {
            String usuario = txtUsuario.getText();
            if (usuario.equals("")) {
                AlertUtils.mostrarError("El usuario es un campo obligatorio");
                return;
            }
            String contrasena = DigestUtils.sha256Hex(txtContrasena.getText());

            boolean empleadoRegistrado = empleadoDAO.existeEmpleado(usuario);

            if(!empleadoRegistrado) {
                Empleado empleado = new Empleado(usuario, contrasena);
                empleadoDAO.guardarEmpleado(empleado);
            }

            cambiarVentana();
        } catch (SQLException sqle) {
            AlertUtils.mostrarError("Error al iniciar sesión");
        }
    }

    @FXML
    private void cambiarVentana() {
        try {
            FXMLLoader loader = new FXMLLoader (getClass().getResource("../resources/org.example.proyecto_2formularios/flores.fxml"));

            Parent root = loader.load();

            FlorController controlador = loader.getController();

            controlador.cargarDatos();

            Scene scene = new Scene(root);
            Stage stage = new Stage();

            stage.setScene(scene);
            stage.show();

            stage.setOnCloseRequest(e -> controlador.closeWindows());

            Stage myStage = (Stage) this.btnEntrar.getScene().getWindow();
            myStage.close();
        } catch (IOException e) {
            Logger.getLogger(AppController.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
