package mx.unison.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import mx.unison.models.Almacen;
import mx.unison.models.Producto;
import mx.unison.models.Usuario;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import mx.unison.util.CryptoUtils;

/**
 * Gestor de la conexión a la base de datos y creación de DAOs con ORMLite.
 */
public class DatabaseHelper {

    private static final String DATABASE_URL = "jdbc:sqlite:InventarioV4.db";
    private static ConnectionSource connectionSource;

    private static Dao<Usuario, Integer> usuarioDao;
    private static Dao<Almacen, Integer> almacenDao;
    private static Dao<Producto, Integer> productoDao;

    // Para testing se puede inyectar una URL diferente
    public static void init(String dbUrl) throws SQLException {
        if (connectionSource != null) {
            try {
                connectionSource.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        connectionSource = new JdbcConnectionSource(dbUrl != null ? dbUrl : DATABASE_URL);

        // Crear tablas si no existen
        TableUtils.createTableIfNotExists(connectionSource, Usuario.class);
        TableUtils.createTableIfNotExists(connectionSource, Almacen.class);
        TableUtils.createTableIfNotExists(connectionSource, Producto.class);

        // Inicializar DAOs
        usuarioDao = DaoManager.createDao(connectionSource, Usuario.class);
        almacenDao = DaoManager.createDao(connectionSource, Almacen.class);
        productoDao = DaoManager.createDao(connectionSource, Producto.class);

        insertDefaultUsers();
    }

    public static void init() throws SQLException {
        init(null);
    }

    private static void insertDefaultUsers() throws SQLException {
        insertUserIfNotExists("ADMIN", "admin23", "ADMIN");
        insertUserIfNotExists("PRODUCTOS", "productos19", "PRODUCTOS");
        insertUserIfNotExists("ALMACENES", "almacenes11", "ALMACENES");
    }

    private static void insertUserIfNotExists(String nombre, String password, String rol) throws SQLException {
        Usuario user = usuarioDao.queryBuilder().where().eq("nombre", nombre).queryForFirst();
        if (user == null) {
            Usuario newUser = new Usuario();
            newUser.setNombre(nombre);
            newUser.setPassword(CryptoUtils.md5(password));
            newUser.setRol(rol);
            usuarioDao.create(newUser);
        }
    }

    public static Dao<Usuario, Integer> getUsuarioDao() throws SQLException {
        if (usuarioDao == null) init();
        return usuarioDao;
    }

    public static Dao<Almacen, Integer> getAlmacenDao() throws SQLException {
        if (almacenDao == null) init();
        return almacenDao;
    }

    public static Dao<Producto, Integer> getProductoDao() throws SQLException {
        if (productoDao == null) init();
        return productoDao;
    }
    
    public static void close() throws Exception {
        if (connectionSource != null) {
            connectionSource.close();
        }
    }
}
