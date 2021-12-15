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

    public static boolean registrarExamen(Examen examen){
        boolean validacion = false;
        Connection con = ConnectionDB.createConnection();
        try {
        PreparedStatement pst;
        pst = con.prepareStatement("insert into examen(nombre, calificacion, inicio, fin, usuario) values (?,?,?,?,'Profesor')");
            pst.setString(1, examen.getNombre());
            pst.setInt(2, 0);
            pst.setString(3, examen.getFechaInicio());
            pst.setString(4, examen.getFechaFin());
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
            PreparedStatement query = connection.prepareStatement("select e.folioExamen as folioExamen, e.fin as fin, e.inicio as inicio, e.nombre as nombre, a.calificacion as calificacion, a.realizado as realizado from examen e, examen_alumno a where e.folioExamen = a.folioExamen and matricula = ?");
            query.setString(1, matricula);
            
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

}
