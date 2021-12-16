package tc.web.evaluaciones.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import tc.web.evaluaciones.database.ConnectionDB;

public class Examen {
    private String nombre;
    private double calificacion;
    private int folioExamen;
    private String fechaInicio;
    private String fechaFin;
    private boolean realizado;

    public boolean isRealizado() {
        return this.realizado;
    }

    public void setRealizado(boolean realizado) {
        this.realizado = realizado;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getCalificacion() {
        return this.calificacion;
    }

    public void setCalificacion(double calificacion) {
        this.calificacion = calificacion;
    }

    public int getFolioExamen() {
        return this.folioExamen;
    }

    public void setFolioExamen(int folioExamen) {
        this.folioExamen = folioExamen;
    }

    public String getFechaInicio() {
        return this.fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return this.fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public static boolean registrarExamen(Examen examen, String usuario){
        boolean validacion = false;
        Connection con = ConnectionDB.createConnection();
        try {
        PreparedStatement pst;
        pst = con.prepareStatement("insert into examen(nombre, inicio, fin, usuario) values (?,?,?,?)");
            pst.setString(1, examen.getNombre());
            pst.setString(2, examen.getFechaInicio());
            pst.setString(3, examen.getFechaFin());
            pst.setString(4, usuario);
            pst.execute();
            pst.close();
            validacion = true;
        } catch(SQLException e){
            System.out.print(e);
        } finally{
            try{
                if(!con.isClosed()){
                    con.close();
                }
            } catch(SQLException ex){
                System.out.print(ex);
            }
        }
        return validacion;
    }

    public static List<Examen> listaExamenes(String usuario){
        List<Examen> examenes = new ArrayList();
        Examen examen = null;
        Connection connection = ConnectionDB.createConnection();
        try {
            PreparedStatement query = connection.prepareStatement("select * from examen where usuario = ?");
            query.setString(1, usuario);
            
            ResultSet result = query.executeQuery();
            while(result.next()){
                examen = new Examen();
                examen.setFechaFin(result.getString("fin"));
                examen.setFechaInicio(result.getString("inicio"));
                examen.setFolioExamen(result.getInt("folioExamen"));
                examen.setNombre(result.getString("nombre"));
                examenes.add(examen);
            }
            result.close();
        } catch (SQLException ex) {
            
        }
        return examenes;
    }

    public static Examen obtenerExamen(String folioExamen){
        Examen examen = null;
        Connection connection = ConnectionDB.createConnection();
        try {
            PreparedStatement query = connection.prepareStatement("select * from examen where folioExamen = ?");
            query.setString(1, folioExamen);

            ResultSet result = query.executeQuery();
            if(result.next()){
                examen = new Examen();
                examen.setFechaFin(result.getString("fin"));
                examen.setFechaInicio(result.getString("inicio"));
                examen.setFolioExamen(result.getInt("folioExamen"));
                examen.setNombre(result.getString("nombre"));
            }
            result.close();
        } catch (SQLException ex) {
            
        }
        return examen;
    }

    public static List<Examen> obtenerExamenesAlumnos(String matricula){
        List<Examen> examenes = new ArrayList<>();
        Examen examen = null;
        Connection connection = ConnectionDB.createConnection();
        try {
            PreparedStatement query = connection.prepareStatement("select *, (select calificacion from examen_alumno a where a.matricula = ? and e.folioExamen = a.folioExamen) as calificacion, (select realizado from examen_alumno a where a.matricula = ? and e.folioExamen = a.folioExamen) as realizado from examen e;");
            query.setString(1, matricula);
            query.setString(2, matricula);
            
            ResultSet result = query.executeQuery();
            while(result.next()){
                examen = new Examen();
                examen.setFechaFin(result.getString("fin"));
                examen.setFechaInicio(result.getString("inicio"));
                examen.setFolioExamen(result.getInt("folioExamen"));
                examen.setNombre(result.getString("nombre"));
                examen.setCalificacion(result.getFloat("calificacion"));
                examen.setRealizado(result.getBoolean("realizado"));
                examenes.add(examen);
            }
            result.close();
        } catch (SQLException ex) {
            
        }
        return examenes;
    }

    public static double generarCalificacion(Integer folioExamen, String matricula){
        int total = Examen.obtenerTotalPreguntas(folioExamen);
        int aciertos = Examen.obtenerTotalAciertos(matricula);
        return total==0?0:(aciertos/total)*10;
    }

    public static int obtenerTotalPreguntas(Integer folioExamen){
        int total = 0;
        Connection connection = ConnectionDB.createConnection();
        try {
            PreparedStatement query = connection.prepareStatement("select count(*) as total from pregunta where folioExamen = ?");
            query.setInt(1, folioExamen);

            ResultSet result = query.executeQuery();
            if(result.next()){
                total = result.getInt("total");
            }
            result.close();
        } catch (SQLException ex) {
            
        }
        return total;
    }
    
    public static int obtenerTotalAciertos(String matricula){
        int aciertos = 0;
        Connection connection = ConnectionDB.createConnection();
        try {
            PreparedStatement query = connection.prepareStatement("select count(*) as aciertos from respuesta r, respuesta_pregunta p where r.correcto = true and matricula = ? and r.id = p.idRespuesta");
            query.setString(1, matricula);

            ResultSet result = query.executeQuery();
            if(result.next()){
                aciertos = result.getInt("aciertos");
            }
            result.close();
        } catch (SQLException ex) {
            
        }
        return aciertos;
    }

}
