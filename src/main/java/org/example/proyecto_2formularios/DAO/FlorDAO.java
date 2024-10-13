package org.example.proyecto_2formularios.DAO;

import org.example.proyecto_2formularios.domain.Flor;
import org.example.proyecto_2formularios.util.R;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class FlorDAO {

    private Connection conexion;

    public void conectar() throws ClassNotFoundException, SQLException, IOException {
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

    public void guardarFlor(Flor flor) throws SQLException {
        String sql = "INSERT INTO flores (nombre, precio, color) VALUES (?, ?, ?, ?)";

        PreparedStatement sentencia = conexion.prepareStatement(sql);
        sentencia.setString(1, flor.getNombre());
        sentencia.setDouble(2, flor.getPrecio());
        sentencia.setString(3, flor.getColor());
        sentencia.executeUpdate();
    }

    public void eliminarFlor(Flor flor) throws SQLException {
        String sql = "DELETE FROM flores WHERE id = ?";

        PreparedStatement sentencia = conexion.prepareStatement(sql);
        sentencia.setInt(1, flor.getId());
        sentencia.executeUpdate();
    }

    public void modificarFlor(Flor florAntigua, Flor florNueva) throws SQLException {
        String sql = "UPDATE flores SET nombre = ?, precio = ?, color = ? WHERE id = ?";

        PreparedStatement sentencia = conexion.prepareStatement(sql);
        sentencia.setString(1, florNueva.getNombre());
        sentencia.setDouble(2, florNueva.getPrecio());
        sentencia.setString(3, florNueva.getColor());
        sentencia.setInt(5, florAntigua.getId());
        sentencia.executeUpdate();
    }

    public List<Flor> obtenerFlores() throws SQLException {
        List<Flor> flores = new ArrayList<>();
        String sql = "SELECT * FROM flores";

        PreparedStatement sentencia = conexion.prepareStatement(sql);
        ResultSet resultado = sentencia.executeQuery();
        while (resultado.next()) {
            Flor flor = new Flor();
            flor.setId(resultado.getInt(1));
            flor.setNombre(resultado.getString(2));
            flor.setPrecio(resultado.getDouble(3));
            flor.setColor(resultado.getString(4));

            flores.add(flor);
        }

        return flores;
    }

    public boolean existeFlor(int id) throws SQLException {
        String sql = "SELECT * FROM flores WHERE id = ?";
        PreparedStatement sentencia = conexion.prepareStatement(sql);
        ResultSet resultado = sentencia.executeQuery();

        return resultado.next();
    }
}
