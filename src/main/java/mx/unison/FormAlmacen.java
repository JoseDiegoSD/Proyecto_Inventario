package mx.unison;

import javax.swing.*;
import java.awt.*;

public class FormAlmacen extends JDialog {
    private JTextField txtNombre;
    private JTextField txtUbicacion;
    private boolean saved = false;

    public FormAlmacen(Window owner, Almacen a) {
        super(owner, "Formulario de Almacén", ModalityType.APPLICATION_MODAL);
        initComponents(a);
        pack();
        setLocationRelativeTo(owner);
    }

    private void initComponents(Almacen a) {
        setLayout(new BorderLayout());
        JPanel center = new JPanel(new GridLayout(2, 2, 10, 10));
        center.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        center.add(new JLabel("Nombre del Almacén:"));
        txtNombre = new JTextField(a != null ? a.nombre : "");
        center.add(txtNombre);

        center.add(new JLabel("Ubicación física:"));
        txtUbicacion = new JTextField(a != null ? a.ubicacion : "");
        center.add(txtUbicacion);

        add(center, BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        btnGuardar.addActionListener(e -> {
            if (txtNombre.getText().trim().isEmpty() || txtUbicacion.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios", "Advertencia", JOptionPane.WARNING_MESSAGE);
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

    public boolean isSaved() {
        return saved;
    }

    public String getNombre() {
        return txtNombre.getText().trim();
    }

    public String getUbicacion() {
        return txtUbicacion.getText().trim();
    }
}
