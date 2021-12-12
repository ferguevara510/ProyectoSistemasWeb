package tc.web.evaluaciones;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.options;
import static spark.Spark.before;
import static spark.Spark.staticFileLocation;

import spark.ModelAndView;
import tc.web.evaluaciones.api.MustacheTemplateEngine;
import tc.web.evaluaciones.model.Alumno;
import tc.web.evaluaciones.model.Examen;
import tc.web.evaluaciones.model.Profesor;

import java.io.IOException;
import java.io.StringWriter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.apache.log4j.BasicConfigurator;

public class App 
{
    public static void main( String[] args )
    {
        BasicConfigurator.configure();
        port(8080);
        staticFileLocation("/public");

        options("/*", (request, response) -> {

            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });

        before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));


        get("/login", (request, response) -> { 
            Map map = new HashMap<String,Object>(); 
            return new ModelAndView(map,"login.mustache"); 
        }, new MustacheTemplateEngine());

        get("/examenesProfesor", (request, response) -> { 
            Map map = new HashMap<String,Object>(); 
            return new ModelAndView(map,"examenes-profesor.mustache"); 
        }, new MustacheTemplateEngine());

        get("/examenesAlumnos", (request, response) -> { 
            Map map = new HashMap<String,Object>(); 
            return new ModelAndView(map,"examenes-alumnos.mustache"); 
        }, new MustacheTemplateEngine());

        get("/crearExamen", (request, response) -> { 
            Map map = new HashMap<String,Object>(); 
            return new ModelAndView(map,"registrar-examen.mustache"); 
        }, new MustacheTemplateEngine());

        post("/loginProfesor", (request, response) -> {
            String usuario = request.queryParams("usuario");
            String contraseña = request.queryParams("password");
            System.out.println(usuario);
            System.out.println(contraseña);
            Map map = new HashMap<String,Object>(); 
            boolean validacion = Profesor.iniciarSesion(usuario, contraseña);
            String pantalla = "";
            if(validacion){
                pantalla = "examenes-profesor.mustache";
                response.status(200);
            }else{
                pantalla = "login.mustache";
                response.status(400);
            }
            return new ModelAndView(map,pantalla);
        },new MustacheTemplateEngine());

        post("/loginAlumno", (request, response) -> {
            String usuario = request.queryParams("matricula");
            String contraseña = request.queryParams("password");
            System.out.println(usuario);
            System.out.println(contraseña);
            Map map = new HashMap<String,Object>(); 
            boolean validacion = Alumno.iniciarSesion(usuario, contraseña);
            String pantalla = "";
            if(validacion){
                pantalla = "examenes-alumnos.mustache";
                response.status(200);
            }else{
                pantalla = "login.mustache";
                response.status(400);
            }
            return new ModelAndView(map,pantalla);
        },new MustacheTemplateEngine());

        get("/loginAlumno", (request, response) -> { 
            Map map = new HashMap<String,Object>(); 
            return new ModelAndView(map,"login-alumno.mustache"); 
        }, new MustacheTemplateEngine());

        get("/modificarAlumno/:matricula", (request, response) -> { 
            Map map = new HashMap<String,Alumno>();
            Alumno alumno = Alumno.consultarAlumno(request.params(":matricula"));
            map.put("alumno", alumno);
            return new ModelAndView(map,"modificar-alumno.mustache"); 
        }, new MustacheTemplateEngine());

        post("/modificarAlumno", (request, response) -> {
            Alumno alumno = new Alumno();
            alumno.setNombre(request.queryParams("nombre"));
            alumno.setApellidoMaterno(request.queryParams("apellidoMaterno"));
            alumno.setApellidoPaterno(request.queryParams("apellidoPaterno"));
            String matricula = request.queryParams("matricula");

            Alumno.modificarAlumno(matricula, alumno);

            response.redirect("/modificarAlumno/"+matricula);
            return true;
        });
        
        post("/registrarExamen", (request, response) -> {
            Examen examen = new Examen();
            examen.setNombre(request.queryParams("nombre"));
            String fechaInicio = request.queryParams("fechaInicio");
            String fechaFin = request.queryParams("fechaFin");
            String horaInicio = request.queryParams("horaInicio");
            String horaFin = request.queryParams("horaFin");
            examen.setFechaInicio(fechaInicio+" "+horaInicio);
            examen.setFechaFin(fechaFin+" "+horaFin);


            boolean validation = Examen.registrarExamen(examen);
            if(validation){
                response.redirect("/examenesProfesor");
            }else{
                response.redirect("/crearExamen");
            }
            
            return true;
        });

        get("/registrarAlumno", (request, response) -> { 
            Map map = new HashMap<String,Object>();
            return new ModelAndView(map,"registrar-alumno.mustache"); 
        }, new MustacheTemplateEngine());

        get("/listaAlumnos", (request, response) -> { 
            Map map = new HashMap<String,Object>();
            List<Alumno> alumnos = Alumno.consultarAlumnos();
            map.put("alumnos", alumnos);
            return new ModelAndView(map,"alumnos-profesor.mustache"); 
        }, new MustacheTemplateEngine());

        get("/listaExamenes/:usuario", (request, response) -> { 
            Map map = new HashMap<String,Object>();
            String folioExamen = request.params(":usuario");
            List<Examen> examenes = Examen.listaExamenes(folioExamen);
            map.put("examenes", examenes);
            return new ModelAndView(map,"examenes-profesor.mustache"); 
        }, new MustacheTemplateEngine());

        get("/examen/:folio", (request, response) -> { 
            Map map = new HashMap<String,Object>();
            String folioExamen = request.params(":folio");
            Examen examen = Examen.obtenerExamen(folioExamen);
            map.put("examen", examen);
            return new ModelAndView(map,"examenes-profesor.mustache"); 
        }, new MustacheTemplateEngine());

        post("/registrarAlumno", (request, response) -> {
            String matricula = request.queryParams("matricula");
            String nombre = request.queryParams("nombre");
            String apellidoPaterno = request.queryParams("apellidoPaterno");
            String apellidoMaterno = request.queryParams("apellidoMaterno");
            String contraseña = request.queryParams("contrasena");

            Alumno.agregarAlumno(nombre, apellidoMaterno, apellidoPaterno, matricula, contraseña);
            response.redirect("menu-principal-profesor.mustache");
            return true;
        });
    }

    public static String dataToJson(Object data) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            StringWriter sw = new StringWriter();
            mapper.writeValue(sw, data);
            return sw.toString();
        } catch (IOException e){
            throw new RuntimeException("IOException from a StringWriter?");
        }
    }
}