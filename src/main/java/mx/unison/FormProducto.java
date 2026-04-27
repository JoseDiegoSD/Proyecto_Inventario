package mx.unison;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FormProducto extends JDialog {
    private JTextField txtNombre, txtDescripcion, txtCantidad, txtPrecio;
    private JComboBox<ComboItem> cbAlmacen;
    private boolean saved = false;
    private Database db;

    public FormProducto(Window owner, Producto p, Database db) {
        super(owner, "Formulario de Producto", ModalityType.APPLICATION_MODAL);
        this.db = db;
        initComponents(p);
        pack();
        setLocationRelativeTo(owner);
    }

    private void initComponents(Producto p) {
        setLayout(new BorderLayout());
        JPanel center = new JPanel(new GridLayout(5, 2, 10, 10));
        center.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        center.add(new JLabel("Nombre del Producto:"));
        txtNombre = new JTextField(p != null && p.nombre != null ? p.nombre : "");
        center.add(txtNombre);

        center.add(new JLabel("Descripción breve:"));
        txtDescripcion = new JTextField(p != null && p.descripcion != null ? p.descripcion : "");
        center.add(txtDescripcion);

        center.add(new JLabel("Cantidad en Inventario:"));
        txtCantidad = new JTextField(p != null ? String.valueOf(p.cantidad) : "0");
        center.add(txtCantidad);

        center.add(new JLabel("Precio Unitario ($):"));
        txtPrecio = new JTextField(p != null ? String.valueOf(p.precio) : "0.0");
        center.add(txtPrecio);

        center.add(new JLabel("Asignar a Almacén:"));
        cbAlmacen = new JComboBox<>();
        cbAlmacen.addItem(new ComboItem(0, "Sin Almacén (Nulo)"));
        
        List<Almacen> almacenes = db.listAlmacenes();
        for (Almacen a : almacenes) {
            ComboItem item = new ComboItem(a.id, a.nombre);
            cbAlmacen.addItem(item);
            if (p != null && p.almacenId == a.id) {
                cbAlmacen.setSelectedItem(item);
            }
        }
        center.add(cbAlmacen);

        add(center, BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        JButton btnGuardar = new JButton("Guardar Cambios");
        JButton btnCancelar = new JButton("Cancelar");

        btnGuardar.addActionListener(e -> {
            try {
                int cant = Integer.parseInt(txtCantidad.getText().trim());
                double costo = Double.parseDouble(txtPrecio.getText().trim());
                if(cant < 0 || costo < 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "La cantidad y el precio deben ser valores numéricos positivos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (txtNombre.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre del producto no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            saved = true;
            dispose();
        });
        
        btnCancelar.addActionListener(e -> dispose());
        
        bottom.add(btnGuardar);
        bottom.add(btnCancelar);
        add(bottom, BorderLayout.SOUTH);
    }

    public boolean isSaved() { return saved; }

    public void fillModel(Producto p) {
        p.nombre = txtNombre.getText().trim();
        p.descripcion = txtDescripcion.getText().trim();
        p.cantidad = Integer.parseInt(txtCantidad.getText().trim());
        p.precio = Double.parseDouble(txtPrecio.getText().trim());
        p.almacenId = ((ComboItem) cbAlmacen.getSelectedItem()).id;
    }

    private class ComboItem {
        int id; 
        String label;
        ComboItem(int id, String label) { this.id = id; this.label = label; }
        @Override public String toString() { return label; }
    }
}
