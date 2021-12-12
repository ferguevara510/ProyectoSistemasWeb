package tc.web.evaluaciones.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import tc.web.evaluaciones.database.ConnectionDB;

public class Examen {
    private String nombre;
    private float calificacion;
    private int folioExamen;
    private String fechaInicio;
    private String fechaFin;

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public float getCalificacion() {
        return this.calificacion;
    }

    public void setCalificacion(float calificacion) {
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

}
