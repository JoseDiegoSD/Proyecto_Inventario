package mx.unison.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Entidad que representa un Usuario en el sistema.
 */
@DatabaseTable(tableName = "usuarios")
public class Usuario {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = false, unique = true)
    private String nombre;

    @DatabaseField(canBeNull = false)
    private String password;

    @DatabaseField(columnName = "fecha_hora_ultimo_inicio")
    private String fechaHoraUltimoInicio;

    @DatabaseField(canBeNull = false)
    private String rol;

    public Usuario() {
        // ORMLite necesita un constructor sin argumentos
    }

    public Usuario(String nombre, String password, String rol) {
        this.nombre = nombre;
        this.password = password;
        this.rol = rol;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFechaHoraUltimoInicio() { return fechaHoraUltimoInicio; }
    public void setFechaHoraUltimoInicio(String fechaHoraUltimoInicio) { this.fechaHoraUltimoInicio = fechaHoraUltimoInicio; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
}
