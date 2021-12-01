package tc.web.evaluaciones.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import tc.web.evaluaciones.database.ConnectionDB;

public class Profesor {
    private String usuario;
    private String contraseña;

    public void setUsuario(String usuario){
        this.usuario = usuario;
    }

    public String getUsuario(){
        return usuario;
    }

    public void setContraseña(String contraseña){
        this.contraseña = contraseña;
    }

    public String getContraseña(){
        return contraseña;
    }

    public static boolean iniciarSesion(String usuario, String contraseña){
        Connection connection = ConnectionDB.createConnection();
        boolean validation = false;
        try {
            PreparedStatement query = connection.prepareStatement("select * from profesor where usuario = ? and contrasena = ?");
            query.setString(1, usuario);
            query.setString(2, contraseña);
            
            ResultSet result = query.executeQuery();
            if(result.next()){
                validation = true;
            }
            result.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return validation;
    }
}
