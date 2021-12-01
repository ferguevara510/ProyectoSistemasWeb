package tc.web.evaluaciones.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import tc.web.evaluaciones.database.ConnectionDB;

public class Alumno {
    private String nombre;
    private String contrasena;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String matricula;

    public void setNombre(String nombre){
        this.nombre = nombre;
    }

    public String getNombre(){
        return nombre;
    }

    public void setContraseña(String contraseña){
        this.contrasena = contraseña;
    }

    public String getContrasena(){
        return contrasena;
    }

    public void setApellidoMaterno(String apellidoMaterno){
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getApellidoMaterno(){
        return apellidoMaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno){
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoPaterno(){
        return apellidoPaterno;
    }

    public void setMatricula(String matricula){
        this.matricula = matricula;
    }

    public String getMatricula(){
        return matricula;
    }

    public static boolean iniciarSesion(String matricula, String contraseña){
        Connection connection = ConnectionDB.createConnection();
        boolean validation = false;
        try {
            PreparedStatement query = connection.prepareStatement("select * from alumno where matricula = ? and contrasena = ?");
            query.setString(1, matricula);
            query.setString(2, contraseña);
            
            ResultSet result = query.executeQuery();
            if(result.next()){
                validation = true;
            }
            result.close();
        } catch (SQLException ex) {
            
        }

        return validation;
    }

    public static void agregarAlumno(String nombre, String apellidoMaterno, String apellidoPaterno, String matricula, String contrasena){
        Connection con = ConnectionDB.createConnection();
        try {
        PreparedStatement pst;
        pst = con.prepareStatement("insert into alumno(nombre, apellidoMaterno, apellidoPaterno, matricula, contrasena) values (?,?,?,?,?)");
            pst.setString(1, nombre);
            pst.setString(2, apellidoMaterno);
            pst.setString(3, apellidoPaterno);
            pst.setString(4, matricula);
            pst.setString(5, contrasena);
            pst.execute();
            //Se cierra pst
            pst.close();
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
    }
    
    public static Alumno consultarAlumno(String matricula){
        Connection connection = ConnectionDB.createConnection();
        Alumno alumno = null;
        try {
            PreparedStatement query = connection.prepareStatement("select * from alumno where matricula = ?");
            query.setString(1, matricula);
            
            ResultSet result = query.executeQuery();
            if(result.next()){
                alumno = new Alumno();
                alumno.setNombre(result.getString("nombre"));
                alumno.setApellidoMaterno(result.getString("apellidoMaterno"));
                alumno.setApellidoPaterno(result.getString("apellidoPaterno"));
                alumno.setContraseña(result.getString("contrasena"));
                alumno.setMatricula(result.getString("matricula"));
            }
            result.close();
        } catch (SQLException ex) {
            
        }

        return alumno;
    }

    public static List<Alumno> consultarAlumnos(){
        Connection connection = ConnectionDB.createConnection();
        List<Alumno> alumnos = new ArrayList<>();
        Alumno alumno = null;
        try {
            PreparedStatement query = connection.prepareStatement("select * from alumno");
            
            ResultSet result = query.executeQuery();
            while(result.next()){
                alumno = new Alumno();
                alumno.setNombre(result.getString("nombre"));
                alumno.setApellidoMaterno(result.getString("apellidoMaterno"));
                alumno.setApellidoPaterno(result.getString("apellidoPaterno"));
                alumno.setContraseña(result.getString("contrasena"));
                alumno.setMatricula(result.getString("matricula"));
                alumnos.add(alumno);
            }
            result.close();
        } catch (SQLException ex) {
            
        }

        return alumnos;
    }
    
    public static boolean modificarAlumno(String matricula, Alumno al){
        boolean modificacion = false;
        Connection con = ConnectionDB.createConnection();
        try{
            PreparedStatement pst;
            pst = con.prepareStatement("update alumno SET nombre='"+al.getNombre()+"', apellidoMaterno='"+al.getApellidoMaterno()+"',apellidoMaterno='"+al.getApellidoMaterno()+"' WHERE matricula='"+matricula+"'");
                pst.execute();
                pst.close();
                modificacion = true;
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
        return modificacion; 
    }
}
