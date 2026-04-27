package mx.unison.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Entidad que representa un Almacen en el sistema.
 */
@DatabaseTable(tableName = "almacenes")
public class Almacen {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = false)
    private String nombre;

    @DatabaseField
    private String ubicacion;

    @DatabaseField(columnName = "fecha_hora_creacion")
    private String fechaHoraCreacion;

    @DatabaseField(columnName = "fecha_hora_ultima_modificacion")
    private String fechaHoraUltimaModificacion;

    @DatabaseField(columnName = "ultimo_usuario_en_modificar")
    private String ultimoUsuarioEnModificar;

    public Almacen() {
        // Constructor vacío requerido por ORMLite
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public String getFechaHoraCreacion() { return fechaHoraCreacion; }
    public void setFechaHoraCreacion(String fechaHoraCreacion) { this.fechaHoraCreacion = fechaHoraCreacion; }

    public String getFechaHoraUltimaModificacion() { return fechaHoraUltimaModificacion; }
    public void setFechaHoraUltimaModificacion(String fechaHoraUltimaModificacion) { this.fechaHoraUltimaModificacion = fechaHoraUltimaModificacion; }

    public String getUltimoUsuarioEnModificar() { return ultimoUsuarioEnModificar; }
    public void setUltimoUsuarioEnModificar(String ultimoUsuarioEnModificar) { this.ultimoUsuarioEnModificar = ultimoUsuarioEnModificar; }

    @Override
    public String toString() {
        return nombre; // Útil para JavaFX ComboBox
    }
}
