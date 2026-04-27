package mx.unison.dao;

import com.j256.ormlite.dao.Dao;
import mx.unison.models.Almacen;
import mx.unison.models.Producto;
import mx.unison.models.Usuario;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseHelperTest {

    @BeforeEach
    void setUp() throws SQLException {
        // Usar base de datos en memoria para pruebas
        DatabaseHelper.init("jdbc:sqlite::memory:");
    }

    @AfterEach
    void tearDown() throws Exception {
        DatabaseHelper.close();
    }

    @Test
    void testUsuariosIniciales() throws SQLException {
        Dao<Usuario, Integer> dao = DatabaseHelper.getUsuarioDao();
        List<Usuario> usuarios = dao.queryForAll();
        
        // Deben existir ADMIN, PRODUCTOS y ALMACENES
        assertTrue(usuarios.size() >= 3);
        assertNotNull(dao.queryBuilder().where().eq("nombre", "ADMIN").queryForFirst());
    }

    @Test
    void testCrearYLeerAlmacen() throws SQLException {
        Dao<Almacen, Integer> dao = DatabaseHelper.getAlmacenDao();
        
        Almacen a = new Almacen();
        a.setNombre("Almacen Central");
        a.setUbicacion("Norte");
        
        dao.create(a);
        assertTrue(a.getId() > 0); // ID generado
        
        Almacen guardado = dao.queryForId(a.getId());
        assertEquals("Almacen Central", guardado.getNombre());
        assertEquals("Norte", guardado.getUbicacion());
    }

    @Test
    void testCrearYLeerProducto() throws SQLException {
        Dao<Almacen, Integer> almacenDao = DatabaseHelper.getAlmacenDao();
        Dao<Producto, Integer> productoDao = DatabaseHelper.getProductoDao();

        Almacen a = new Almacen();
        a.setNombre("Almacen Sur");
        almacenDao.create(a);

        Producto p = new Producto();
        p.setNombre("Laptop");
        p.setCantidad(10);
        p.setPrecio(1500.50);
        p.setAlmacen(a);
        
        productoDao.create(p);
        assertTrue(p.getId() > 0);

        Producto guardado = productoDao.queryForId(p.getId());
        assertEquals("Laptop", guardado.getNombre());
        assertEquals(10, guardado.getCantidad());
        assertNotNull(guardado.getAlmacen());
        assertEquals(a.getId(), guardado.getAlmacen().getId());
    }

    @Test
    void testActualizarEliminarProducto() throws SQLException {
        Dao<Producto, Integer> productoDao = DatabaseHelper.getProductoDao();

        Producto p = new Producto();
        p.setNombre("Mouse");
        p.setCantidad(50);
        productoDao.create(p);

        // Actualizar
        p.setCantidad(45);
        productoDao.update(p);
        Producto actualizado = productoDao.queryForId(p.getId());
        assertEquals(45, actualizado.getCantidad());

        // Eliminar
        productoDao.delete(actualizado);
        assertNull(productoDao.queryForId(p.getId()));
    }
}
