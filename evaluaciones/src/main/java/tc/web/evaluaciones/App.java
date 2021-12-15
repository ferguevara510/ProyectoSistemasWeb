package tc.web.evaluaciones;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.MultipartConfigElement;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.options;
import static spark.Spark.before;
import static spark.Spark.staticFileLocation;
import static spark.Spark.staticFiles;

import spark.ModelAndView;
import spark.Session;
import tc.web.evaluaciones.api.MustacheTemplateEngine;
import tc.web.evaluaciones.model.Alumno;
import tc.web.evaluaciones.model.Examen;
import tc.web.evaluaciones.model.Pregunta;
import tc.web.evaluaciones.model.Profesor;
import tc.web.evaluaciones.model.Respuesta;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.apache.log4j.BasicConfigurator;

public class App 
{
    public static void main( String[] args )
    {
        //Fer Guevara
        //ItzNadia
        BasicConfigurator.configure();
        port(8080);
        staticFileLocation("/public");
        File uploadDir = new File("upload");
        uploadDir.mkdir(); // create the upload directory if it doesn't exist
        staticFiles.externalLocation("upload");

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

        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            String uri = request.uri();
            String method = request.requestMethod();
            Session sesion = request.session();
            if(!uri.equals("/login") && uri.indexOf("css") == -1 && sesion.attribute("usuario") == null){
                if(method.equals("GET") && !uri.equals("/loginAlumno")){
                    if(sesion.attribute("matricula") == null){
                        response.redirect("/login");
                    }
                    
                }
            }
            
        });


        get("/login", (request, response) -> { 
            Map map = new HashMap<String,Object>(); 
            return new ModelAndView(map,"login.mustache"); 
        }, new MustacheTemplateEngine());

        get("/examenesProfesor", (request, response) -> { 
            Map map = new HashMap<String,Object>();
            String usuario = request.session().attribute("usuario");
            List<Examen> examenes = Examen.listaExamenes(usuario);
            map.put("examenes", examenes);
            return new ModelAndView(map,"examenes-profesor.mustache"); 
        }, new MustacheTemplateEngine());

        get("/examenesAlumnos", (request, response) -> { 
            Map map = new HashMap<String,Object>();
            String matricula = request.session().attribute("matricula");
            List<Examen> examenes = Examen.obtenerExamenesAlumnos(matricula);
            map.put("examenes", examenes);
            map.put("matricula", matricula);
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
            if(validacion){
                Session sesion = request.session(true);
                sesion.attribute("usuario",usuario);
                response.redirect("/examenesProfesor");
            }else{
                response.redirect("/login");
            }
            return true;
        });

        post("/loginAlumno", (request, response) -> {
            String usuario = request.queryParams("matricula");
            String contraseña = request.queryParams("password");
            System.out.println(usuario);
            System.out.println(contraseña);
            Map map = new HashMap<String,Object>(); 
            boolean validacion = Alumno.iniciarSesion(usuario, contraseña);
            if(validacion){
                Session sesion = request.session(true);
                sesion.attribute("matricula",usuario);
                response.redirect("/examenesAlumnos");
            }else{
                response.redirect("/loginAlumno");
            }
            return true;
        });

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

            Session sesion = request.session();
            boolean validation = Examen.registrarExamen(examen,sesion.attribute("usuario"));
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
            List<Pregunta> preguntas = Pregunta.buscarPreguntasDeExamen(folioExamen);

            map.put("examen", examen);
            map.put("preguntas", preguntas);
            return new ModelAndView(map,"examen.mustache"); 
        }, new MustacheTemplateEngine());
        
        get("/crearPregunta/:folio", (request, response) -> { 
            Map map = new HashMap<String,Object>();
            String folioExamen = request.params(":folio");
            map.put("folioExamen", folioExamen);
            return new ModelAndView(map,"registrar-pregunta.mustache"); 
        }, new MustacheTemplateEngine());

        post("/registrarAlumno", (request, response) -> {
            String matricula = request.queryParams("matricula");
            String nombre = request.queryParams("nombre");
            String apellidoPaterno = request.queryParams("apellidoPaterno");
            String apellidoMaterno = request.queryParams("apellidoMaterno");
            String contraseña = request.queryParams("contrasena");

            Alumno.agregarAlumno(nombre, apellidoMaterno, apellidoPaterno, matricula, contraseña);
            response.redirect("/listaAlumnos");
            return true;
        });

        post("/registrarPregunta/:folioExamen", (request, response) -> {
            String folioExamen = request.params(":folioExamen");
            String tipo = request.queryParams("tipo");
            String descripcion = request.queryParams("descripcion");

            Pregunta.registrarPregunta(descripcion, tipo, folioExamen);
            response.redirect("/examen/"+folioExamen);
            return true;
        });

        get("/registrarRespuesta/:idPregunta", (request, response) -> {
            Map map = new HashMap<String, Object>();
            Integer idPregunta = Integer.parseInt(request.params(":idPregunta"));
            Pregunta pregunta = Pregunta.buscarPreguntaPorID(idPregunta);
            List<Respuesta> respuestas = Respuesta.buscarRespeustaDePreguntas(idPregunta);

            map.put("pregunta", pregunta);
            map.put("idPregunta", idPregunta);
            map.put("respuestas", respuestas);
            map.put("mostrarRespuesta", pregunta.getTipo() == "multiple");

            return new ModelAndView(map,"registrar-respuesta.mustache"); 
        }, new MustacheTemplateEngine());

        post("/registrarRespuesta/:idPregunta", (request, response) -> {
            String idPregunta = request.params(":idPregunta");
            String descripcion = request.queryParams("descripcion");
            String correcto = request.queryParams("correcto");
            if(correcto == null){
                correcto = "off";
            }

            Respuesta.registrarRespuesta(descripcion, correcto.equals("on"), idPregunta);
            response.redirect("/registrarRespuesta/"+idPregunta);
            return true;
        });

        get("/alumnoExamenes/:matricula", (request, response) -> {
            Map map = new HashMap<String, Object>();
            String matricula = request.params(":matricula");
            List<Examen> examenes = Examen.obtenerExamenesAlumnos(matricula);
            map.put("examenes", examenes);
            map.put("matricula", matricula);
            return new ModelAndView(map,"examenes-alumnos.mustache"); 
        }, new MustacheTemplateEngine());

        get("/presentarExamen/:matricula/:folioExamen/:pagina",(request,response)->{
            Map map = new HashMap<String,Object>();
            String matricula = request.params(":matricula");
            String folioExamen = request.params(":folioExamen");
            Integer pagina = Integer.parseInt(request.params(":pagina"));

            Pregunta pregunta = Pregunta.preguntaActual(folioExamen, pagina);
            String mustache = "resultado-examen.mustache";
            if(pregunta != null){
                map.put("pregunta", pregunta);
                map.put("pagina", pagina);
                map.put("folioExamen", folioExamen);
                map.put("matricula", matricula);
                List<Respuesta> respuestas = Respuesta.buscarRespeustaDePreguntas(pregunta.getId());
                map.put("respuestas", respuestas);
                mustache = "pregunta.mustache";
            }else{
                double calificacion = Examen.generarCalificacion(Integer.parseInt(folioExamen), matricula);
                map.put("calificacion", calificacion);
            }

            return new ModelAndView(map,mustache); 
        }, new MustacheTemplateEngine());

        post("/registrarRespuesta/:matricula/:folioExamen/:idPregunta", (request, response) ->{
            String matricula = request.params(":matricula");
            Integer idPregunta = Integer.parseInt(request.params(":idPregunta"));
            Integer folioExamen = Integer.parseInt(request.params(":folioExamen"));
            Integer idRespuesta = Integer.parseInt(request.queryParams("respuesta"));
            Integer pagina = Integer.parseInt(request.queryParams("pagina"));
            Pregunta.registrarRespuesta(matricula, folioExamen, idPregunta, idRespuesta);
            response.redirect("/presentarExamen/"+matricula+"/"+folioExamen+"/"+(pagina+1));
            return true;
        });

        post("/upload/:matricula/:folioExamen/:idPregunta/:pagina", (request, response)->{
            String matricula = request.params(":matricula");
            Integer idPregunta = Integer.parseInt(request.params(":idPregunta"));
            Integer folioExamen = Integer.parseInt(request.params(":folioExamen"));
            Integer pagina = Integer.parseInt(request.params(":pagina"));
            Path tempFile = Files.createTempFile(uploadDir.toPath(), "", "");
            
            request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
            
            try (InputStream input = request.raw().getPart("videoGrabado").getInputStream()) { // getPart needs to use same "name" as input field in form
                Files.copy(input, tempFile, StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                System.out.println(e);
            }
            // Pregunta.registrarRespuestaMultimedia(matricula, folioExamen, idPregunta, tempFile.getFileName().toString());
            
            return  "/presentarExamen/"+matricula+"/"+folioExamen+"/"+(pagina+1);
        });

        get("/upload/:matricula/:folioExamen/:idPregunta/:pagina",(request,response) ->{
            Map map = new HashMap<String,Object>();
            String matricula = request.params(":matricula");
            Integer idPregunta = Integer.parseInt(request.params(":idPregunta"));
            Integer folioExamen = Integer.parseInt(request.params(":folioExamen"));
            Integer pagina = Integer.parseInt(request.params(":pagina"));
            map.put("matricula", matricula);
            map.put("idPregunta", idPregunta);
            map.put("folioExamen", folioExamen);
            map.put("pagina", pagina);
            return new ModelAndView(map,"multimedia.mustache"); 
        }, new MustacheTemplateEngine());
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