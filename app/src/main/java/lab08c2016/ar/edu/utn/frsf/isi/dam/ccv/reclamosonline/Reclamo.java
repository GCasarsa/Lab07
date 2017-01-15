package lab08c2016.ar.edu.utn.frsf.isi.dam.ccv.reclamosonline;

import java.io.Serializable;

/**
 * Created by Gabriel on 11/01/2017.

 */
public class Reclamo implements Serializable{
    private Double latitud;
    private Double longitud;
    private String titulo;
    private String telefono;
    private String email;
    private String imagenPath;

    Reclamo(String titulo, String telefono, String email, Double latitud, Double longitud, String imagenPath){
        this.titulo = titulo;
        this.telefono = telefono;
        this.email = email;
        this.latitud = latitud;
        this.longitud = longitud;
        this.imagenPath = imagenPath;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImagenPath() {
        return imagenPath;
    }

    public void setImagenPath(String imagenPath) {
        this.imagenPath = imagenPath;
    }
}
