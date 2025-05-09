package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import vista.ventana;
import modelo.*;

// Definición de la clase logica_ventana que implementa tres interfaces para manejar eventos.
public class logica_ventana implements ActionListener, ListSelectionListener, ItemListener {
    private ventana delegado; // Referencia a la ventana principal que contiene la GUI.
    private String nombres, email, telefono, categoria = ""; // Variables para almacenar datos del contacto.
    private persona persona; // Objeto de tipo persona, que representa un contacto.
    private List<persona> contactos; // Lista de objetos persona que representa todos los contactos.
    private boolean favorito = false; // Booleano que indica si un contacto es favorito.
    private final Object bloqueoModificacion = new Object(); // Objeto para sincronización al modificar

    // Constructor que inicializa la clase y configura los escuchadores de eventos para los componentes de la GUI.
    public logica_ventana(ventana delegado) {
        this.delegado = delegado;
        cargarContactosRegistrados();
        this.delegado.btn_add.addActionListener(this);
        this.delegado.btn_eliminar.addActionListener(this);
        this.delegado.btn_modificar.addActionListener(this);
        this.delegado.tablaContactos.getSelectionModel().addListSelectionListener(this);
        this.delegado.cmb_categoria.addItemListener(this);
        this.delegado.chb_favorito.addItemListener(this);
        configurarBusqueda(); // Añade búsqueda en segundo plano
    }

    private void incializacionCampos() {
        nombres = delegado.txt_nombres.getText();
        email = delegado.txt_email.getText();
        telefono = delegado.txt_telefono.getText();
    }

    private void cargarContactosRegistrados() {
        try {
            contactos = new personaDAO(new persona()).leerArchivo();
            DefaultTableModel modelo = (DefaultTableModel) delegado.tablaContactos.getModel();
            modelo.setRowCount(0);
            delegado.barraProgreso.setValue(0);
            delegado.barraProgreso.setMaximum(contactos.size());
            int progreso = 0;
            for (persona contacto : contactos) {
                Object[] fila = {
                    contacto.getNombre(),
                    contacto.getTelefono(),
                    contacto.getEmail(),
                    contacto.getCategoria(),
                    contacto.isFavorito() ? "Sí" : "No"
                };
                modelo.addRow(fila);
                progreso++;
                delegado.barraProgreso.setValue(progreso);
                try {
                    Thread.sleep(30);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(delegado, "Existen problemas al cargar todos los contactos");
        }
    }

    private void limpiarCampos() {
        delegado.txt_nombres.setText("");
        delegado.txt_telefono.setText("");
        delegado.txt_email.setText("");
        categoria = "";
        favorito = false;
        delegado.chb_favorito.setSelected(favorito);
        delegado.cmb_categoria.setSelectedIndex(0);
        incializacionCampos();
        cargarContactosRegistrados();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        incializacionCampos();
        if (e.getSource() == delegado.btn_add) {
            if ((!nombres.equals("")) && (!telefono.equals("")) && (!email.equals(""))) {
                if ((!categoria.equals("Elija una Categoria")) && (!categoria.equals(""))) {
                    // Validación concurrente de duplicados antes de guardar
                    new Thread(() -> {
                        synchronized (bloqueoModificacion) {
                            boolean duplicado = contactos.stream().anyMatch(p ->
                                p.getNombre().equalsIgnoreCase(nombres) &&
                                p.getTelefono().equalsIgnoreCase(telefono));

                            if (duplicado) {
                                SwingUtilities.invokeLater(() -> {
                                    JOptionPane.showMessageDialog(delegado, "El contacto ya está registrado.");
                                });
                            } else {
                                persona nuevo = new persona(nombres, telefono, email, categoria, favorito);
                                contactos.add(nuevo); // Agrega a la lista actualizada en memoria
                                try {
                                    new personaDAO(null).sobrescribirArchivo(contactos); // Guarda la lista actualizada
                                    SwingUtilities.invokeLater(() -> {
                                        limpiarCampos();
                                        delegado.mostrarNotificacion("Contacto guardado con éxito");
                                    });
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                    SwingUtilities.invokeLater(() -> {
                                        JOptionPane.showMessageDialog(delegado, "Error al guardar el contacto.");
                                    });
                                }
                            }
                        }
                    }).start();
                } else {
                    JOptionPane.showMessageDialog(delegado, "Elija una Categoria!!!");
                }
            } else {
                JOptionPane.showMessageDialog(delegado, "Todos los campos deben ser llenados!!!");
            }
        } else if (e.getSource() == delegado.btn_eliminar) {
            // Eliminar contacto de la tabla, lista y archivo
            int fila = delegado.tablaContactos.getSelectedRow();
            if (fila != -1) {
                contactos.remove(fila); // Elimina de la lista en memoria
                ((DefaultTableModel) delegado.tablaContactos.getModel()).removeRow(fila); // Elimina de la tabla
                try {
                    new personaDAO(new persona()).sobrescribirArchivo(contactos); // Actualiza archivo
                    delegado.mostrarNotificacion("Contacto eliminado con éxito");
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(delegado, "Error al eliminar el contacto.");
                }
            } else {
                JOptionPane.showMessageDialog(delegado, "Seleccione un contacto para eliminar.");
            }
        } else if (e.getSource() == delegado.btn_modificar) {
            modificarContacto(); // Llamada a función protegida por sincronización
        }
    }

    // Método protegido para modificar contactos usando synchronized para evitar conflictos
    private void modificarContacto() {
        new Thread(() -> {
            synchronized (bloqueoModificacion) {
                int fila = delegado.tablaContactos.getSelectedRow();
                if (fila != -1) {
                    String nombreSeleccionado = (String) delegado.modeloTabla.getValueAt(fila, 0);
                    String nuevoTelefono = delegado.txt_telefono.getText();
                    String nuevoEmail = delegado.txt_email.getText();
                    String nuevaCategoria = delegado.cmb_categoria.getSelectedItem().toString();
                    boolean nuevoFavorito = delegado.chb_favorito.isSelected();

                    try {
                        for (persona p : contactos) {
                            if (p.getNombre().equalsIgnoreCase(nombreSeleccionado)) {
                                p.setTelefono(nuevoTelefono);
                                p.setEmail(nuevoEmail);
                                p.setCategoria(nuevaCategoria);
                                p.setFavorito(nuevoFavorito);
                                break;
                            }
                        }
                        new personaDAO(null).sobrescribirArchivo(contactos);

                        SwingUtilities.invokeLater(() -> {
                            limpiarCampos();
                            delegado.mostrarNotificacion("Contacto modificado con éxito");
                        });

                    } catch (IOException ex) {
                        ex.printStackTrace();
                        SwingUtilities.invokeLater(() -> {
                            JOptionPane.showMessageDialog(delegado, "Error al modificar el contacto.");
                        });
                    }
                }
            }
        }).start();
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            int fila = delegado.tablaContactos.getSelectedRow();
            if (fila != -1) {
                cargarContacto(fila);
            }
        }
    }

    private void cargarContacto(int index) {
        delegado.txt_nombres.setText(contactos.get(index).getNombre());
        delegado.txt_telefono.setText(contactos.get(index).getTelefono());
        delegado.txt_email.setText(contactos.get(index).getEmail());
        delegado.chb_favorito.setSelected(contactos.get(index).isFavorito());
        delegado.cmb_categoria.setSelectedItem(contactos.get(index).getCategoria());
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == delegado.cmb_categoria) {
            categoria = delegado.cmb_categoria.getSelectedItem().toString();
        } else if (e.getSource() == delegado.chb_favorito) {
            favorito = delegado.chb_favorito.isSelected();
        }
    }

    // Búsqueda en segundo plano usando SwingWorker
    private void configurarBusqueda() {
        delegado.txt_buscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                String query = delegado.txt_buscar.getText().trim().toLowerCase();
                if (!query.isEmpty()) {
                    new javax.swing.SwingWorker<Void, Void>() {
                        List<persona> resultados;

                        @Override
                        protected Void doInBackground() {
                            resultados = contactos.stream()
                                .filter(p -> p.getNombre().toLowerCase().contains(query))
                                .toList();
                            return null;
                        }

                        @Override
                        protected void done() {
                            DefaultTableModel modelo = (DefaultTableModel) delegado.tablaContactos.getModel();
                            modelo.setRowCount(0);
                            for (persona p : resultados) {
                                Object[] fila = {
                                    p.getNombre(),
                                    p.getTelefono(),
                                    p.getEmail(),
                                    p.getCategoria(),
                                    p.isFavorito() ? "Sí" : "No"
                                };
                                modelo.addRow(fila);
                            }
                        }
                    }.execute();
                } else {
                    cargarContactosRegistrados();
                }
            }
        });
    }
}