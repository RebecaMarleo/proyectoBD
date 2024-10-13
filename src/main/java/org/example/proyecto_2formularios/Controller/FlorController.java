package org.example.proyecto_2formularios.Controller;

import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.proyecto_2formularios.DAO.EmpleadoDAO;
import org.example.proyecto_2formularios.DAO.FlorDAO;
import org.example.proyecto_2formularios.domain.Empleado;
import org.example.proyecto_2formularios.domain.Flor;
import org.example.proyecto_2formularios.util.AlertUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FlorController {

    public TextField txtNombre, txtPrecio, txtId;
    public ComboBox<String> cboxColor;
    public ListView<Flor> lvFlores;
    public Label lblEstado;
    public Button btnNuevo, btnGuardar, btnModificar, btnEliminar, btnCancelar;

    private enum Accion {
        NUEVO, MODIFICAR
    }

    private Accion accion;

    private final FlorDAO florDAO;
    private Flor florSeleccionada;

    public FlorController() {
        florDAO = new FlorDAO();
        try {
            florDAO.conectar();
        } catch (SQLException sqle) {
            AlertUtils.mostrarError("Error al conectar con la base de datos");
        } catch (ClassNotFoundException cnfe) {
            AlertUtils.mostrarError("Error al iniciar la aplicación");
        } catch (IOException ioe) {
            AlertUtils.mostrarError("Error al cargar la configuración");
        }

        System.out.println(System.getProperty("user.home"));
    }

    public void cargarDatos() {
        modoEdicion(false);

        lvFlores.getItems().clear();
        try {
            List<Flor> flores = florDAO.obtenerFlores();
            lvFlores.setItems(FXCollections.observableList((flores)));

            String[] colores = new String[]{"<Selecciona color>", "Blanco", "Amarillo", "Rojo", "Azul"};
            cboxColor.setItems(FXCollections.observableArrayList(colores));
        } catch (SQLException sqle) {
            AlertUtils.mostrarError("Error cargando los datos de la aplicación");
        }
    }

    @FXML
    public void nuevaFlor(Event event) {
        limpiarCajas();
        modoEdicion(true);
        accion = Accion.NUEVO;
    }

    @FXML
    public void modificarFlor(Event event) {
        modoEdicion(true);
        accion = Accion.MODIFICAR;
    }

    @FXML
    public void eliminarFlor(Event event) {
        Flor flor = lvFlores.getSelectionModel().getSelectedItem();
        if (flor == null) {
            lblEstado.setText("ERROR: No se ha seleccionado ninguna flor");
            return;
        }

        try {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Eliminar flor");
            confirmacion.setContentText("¿Estás seguro?");
            Optional<ButtonType> respuesta = confirmacion.showAndWait();
            if (respuesta.get().getButtonData() == ButtonBar.ButtonData.CANCEL_CLOSE)
                return;

            florDAO.eliminarFlor(flor);
            lblEstado.setText("MENSAJE: Flor eliminada con éxito");

            cargarDatos();
        } catch (SQLException sqle) {
            AlertUtils.mostrarError("No se ha podido eliminar la flor");
        }
    }

    @FXML
    public void guardarFlor(Event event) {
        try {
            int id = 0;
            String nombre = "";
            Double precio = 0.0;
            String color = "";
            Flor flor;

            switch (accion) {
                case NUEVO:
                    nombre = txtNombre.getText();
                    if (nombre.equals("")) {
                        AlertUtils.mostrarError("El nombre es un campo obligatorio");
                        return;
                    }
                    precio = Double.valueOf(txtPrecio.getText());
                    color = cboxColor.getSelectionModel().getSelectedItem();
                    flor = new Flor(nombre, precio, color);
                    florDAO.guardarFlor(flor);
                    break;
                case MODIFICAR:
                    id = Integer.parseInt(txtId.getText());
                    if (String.valueOf(id).equals("")) {
                        AlertUtils.mostrarError("El id es un campo obligatorio");
                        return;
                    }
                    nombre = txtNombre.getText();
                    precio = Double.valueOf(txtPrecio.getText());
                    color = cboxColor.getSelectionModel().getSelectedItem();
                    flor = new Flor(nombre, precio, color);
                    florDAO.guardarFlor(flor);
                    florDAO.modificarFlor(florSeleccionada, flor);
                    break;
            }
        } catch (SQLException sqle) {
            AlertUtils.mostrarError("Error al guadar la flor");
        }

        lblEstado.setText("Flor guardada con éxito");
        cargarDatos();

        modoEdicion(false);
    }

    @FXML
    public void cancelar() {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Edición");
        confirmacion.setContentText("¿Estás seguro?");
        Optional<ButtonType> respuesta = confirmacion.showAndWait();
        if (respuesta.get().getButtonData() == ButtonBar.ButtonData.CANCEL_CLOSE)
            return;

        modoEdicion(false);
        cargarFlor(florSeleccionada);
    }

    private void cargarFlor(Flor flor) {
        txtNombre.setText(flor.getNombre());
        txtPrecio.setText(String.valueOf(flor.getPrecio()));
        cboxColor.setValue(flor.getColor());
    }

    @FXML
    public void seleccionarFlor(Event event) {
        florSeleccionada = lvFlores.getSelectionModel().getSelectedItem();
        cargarFlor(florSeleccionada);
    }

    private void limpiarCajas() {
        txtNombre.setText("");
        txtPrecio.setText("");
        cboxColor.setValue("<Selecciona tipo>");
        txtNombre.requestFocus();
    }

    private void modoEdicion(boolean activar) {
        btnNuevo.setDisable(activar);
        btnGuardar.setDisable(!activar);
        btnModificar.setDisable(activar);
        btnEliminar.setDisable(activar);

        txtNombre.setEditable(activar);
        txtPrecio.setEditable(activar);
        cboxColor.setDisable(!activar);

        lvFlores.setDisable(activar);
    }

    public void closeWindows() {
        try {
            FXMLLoader loader = new FXMLLoader (getClass().getResource("../resources/org.example.proyecto_2formularios/empleados.fxml"));

            Parent root = loader.load();

            AppController controlador = loader.getController();

            Scene scene = new Scene(root);
            Stage stage = new Stage();

            stage.setScene(scene);
            stage.show();

            Stage myStage = (Stage) this.cboxColor.getScene().getWindow();
            myStage.close();
        } catch (IOException e) {
            Logger.getLogger(AppController.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
