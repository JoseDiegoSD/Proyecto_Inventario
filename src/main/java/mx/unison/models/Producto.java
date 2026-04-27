package mx.unison.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Entidad que representa un Producto en el sistema.
 */
@DatabaseTable(tableName = "productos")
public class Producto {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = false)
    private String nombre;

    @DatabaseField(columnDefinition = "REAL DEFAULT 0.0")
    private double precio;

    @DatabaseField(canBeNull = false)
    private int cantidad;

    // Relación de clave foránea con Almacen
    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "almacen_id")
    private Almacen almacen;

    @DatabaseField
    private String descripcion;

    @DatabaseField(columnName = "fecha_hora_creacion")
    private String fechaHoraCreacion;

    @DatabaseField(columnName = "fecha_hora_ultima_modificacion")
    private String fechaHoraUltimaModificacion;

    @DatabaseField(columnName = "ultimo_usuario_en_modificar")
    private String ultimoUsuarioEnModificar;

    public Producto() {
        // Constructor vacío requerido por ORMLite
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public Almacen getAlmacen() { return almacen; }
    public void setAlmacen(Almacen almacen) { this.almacen = almacen; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getFechaHoraCreacion() { return fechaHoraCreacion; }
    public void setFechaHoraCreacion(String fechaHoraCreacion) { this.fechaHoraCreacion = fechaHoraCreacion; }

    public String getFechaHoraUltimaModificacion() { return fechaHoraUltimaModificacion; }
    public void setFechaHoraUltimaModificacion(String fechaHoraUltimaModificacion) { this.fechaHoraUltimaModificacion = fechaHoraUltimaModificacion; }

    public String getUltimoUsuarioEnModificar() { return ultimoUsuarioEnModificar; }
    public void setUltimoUsuarioEnModificar(String ultimoUsuarioEnModificar) { this.ultimoUsuarioEnModificar = ultimoUsuarioEnModificar; }
}
