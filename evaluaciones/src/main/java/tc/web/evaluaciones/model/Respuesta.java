package tc.web.evaluaciones.model;

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

}
