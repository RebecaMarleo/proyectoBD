package org.example.proyecto_2formularios.DAO;

import org.example.proyecto_2formularios.domain.Empleado;
import org.example.proyecto_2formularios.util.R;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class EmpleadoDAO {

    private Connection conexion;

    public void conectar() throws  ClassNotFoundException, SQLException, IOException {
        Properties configuration = new Properties();
        configuration.load(R.getProperties("database.properties"));
        String host = configuration.getProperty("host");
        String port = configuration.getProperty("port");
        String name = configuration.getProperty("name");
        String username = configuration.getProperty("username");
        String password = configuration.getProperty("password");

        Class.forName("com.mysql.cj.jdbc.Driver");
        conexion = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + name + "?serverTimezone=UTC",
                username, password);
    }

    public void desconectar() throws SQLException {
        conexion.close();
    }

    public void guardarEmpleado(Empleado empleado) throws SQLException {
        String sql = "INSERT INTO empleados (usuario, contrasena) VALUES (?, ?)";

        PreparedStatement sentencia = conexion.prepareStatement(sql);
        sentencia.setString(1, empleado.getUsuario());
        sentencia.setString(2, empleado.getContrasena());
        sentencia.executeUpdate();
    }

    public boolean existeEmpleado(String usuario) throws SQLException {
        String sql = "SELECT * FROM empleados WHERE usuario = ?";
        PreparedStatement sentencia = conexion.prepareStatement(sql);
        ResultSet resultado = sentencia.executeQuery();

        return resultado.next();
    }
}
