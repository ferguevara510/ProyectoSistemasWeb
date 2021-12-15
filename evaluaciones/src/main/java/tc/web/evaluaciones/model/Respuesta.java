package tc.web.evaluaciones.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import tc.web.evaluaciones.database.ConnectionDB;

public class Respuesta {
    private int id;
    private String descripcion;
    private String tipo;
    private boolean correcto;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipo() {
        return this.tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public boolean isCorrecto() {
        return this.correcto;
    }

    public void setCorrecto(boolean correcto) {
        this.correcto = correcto;
    }

    public static boolean registrarRespuesta(String descripcion, boolean correcto, String idPregunta){
        boolean validacion = false;
        Connection conexion = ConnectionDB.createConnection();
        try {
        PreparedStatement pst;
        pst = conexion.prepareStatement("insert into respuesta(descripcion, correcto, idPregunta) values (?,?,?)");
            pst.setString(1, descripcion);
            pst.setBoolean(2, correcto);
            pst.setString(3, idPregunta);
            pst.execute();
            pst.close();
            validacion = true;
        } catch(SQLException e){
            System.out.print(e);
        } finally{
            try{
                if(!conexion.isClosed()){
                    conexion.close();
                }
            } catch(SQLException ex){
                System.out.print(ex);
            }
        }
        return validacion;
    }

    public static List<Respuesta> buscarRespeustaDePreguntas(Integer idPregunta){
        List<Respuesta> respuestas = new ArrayList<>();
        Respuesta respuesta = null;
        Connection connection = ConnectionDB.createConnection();
        try {
            PreparedStatement query = connection.prepareStatement("select * from respuesta where idPregunta = ?");
            query.setInt(1, idPregunta);
            
            ResultSet result = query.executeQuery();
            while(result.next()){
                respuesta = new Respuesta();
                respuesta.setId(result.getInt("id"));
                respuesta.setDescripcion(result.getString("descripcion"));
                respuesta.setCorrecto(result.getBoolean("correcto"));
                respuestas.add(respuesta);
            }
            result.close();
        } catch (SQLException ex) {
            
        }
        return respuestas;
    }

}
