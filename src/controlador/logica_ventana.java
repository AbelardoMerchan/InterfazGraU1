package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import vista.ventana;
import modelo.*;

// Definición de la clase logica_ventana que implementa tres interfaces para manejar eventos.
public class logica_ventana implements ActionListener, ListSelectionListener, ItemListener {
	private ventana delegado; // Referencia a la ventana principal que contiene la GUI.
	private String nombres, email, telefono, categoria = ""; // Variables para almacenar datos del contacto.
	private persona persona; // Objeto de tipo persona, que representa un contacto.
	private List<persona> contactos; // Lista de objetos persona que representa todos los contactos.
	private boolean favorito = false; // Booleano que indica si un contacto es favorito.

	// Constructor que inicializa la clase y configura los escuchadores de eventos para los componentes de la GUI.
	public logica_ventana(ventana delegado) {
		// Asigna la ventana recibida como parámetro a la variable de instancia delegado.
		this.delegado = delegado;
		// Carga los contactos almacenados al inicializar.
		cargarContactosRegistrados();
		// Registra los ActionListener para los botones de la GUI.
		this.delegado.btn_add.addActionListener(this);
		this.delegado.btn_eliminar.addActionListener(this);
		this.delegado.btn_modificar.addActionListener(this);
		// Registra los ListSelectionListener para la tabla de contactos.
		this.delegado.tablaContactos.getSelectionModel().addListSelectionListener(this);
		// Registra los ItemListener para el JComboBox de categoría y el JCheckBox de favoritos.
		this.delegado.cmb_categoria.addItemListener(this);
		this.delegado.chb_favorito.addItemListener(this);
	}

	// Método privado para inicializar las variables con los valores ingresados en la GUI.
	private void incializacionCampos() {
		// Obtiene el texto ingresado en los campos de nombres, email y teléfono de la GUI.
		nombres = delegado.txt_nombres.getText();
		email = delegado.txt_email.getText();
		telefono = delegado.txt_telefono.getText();
	}

	// Método privado para cargar los contactos almacenados desde un archivo.
	private void cargarContactosRegistrados() {
		try {
			// Lee los contactos almacenados utilizando una instancia de personaDAO.
			contactos = new personaDAO(new persona()).leerArchivo();

			// Establece los datos en la tabla de contactos
			DefaultTableModel modelo = (DefaultTableModel) delegado.tablaContactos.getModel();
			modelo.setRowCount(0); // Limpia la tabla antes de agregar nuevos datos

			// Configurar la barra de progreso
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
			    delegado.barraProgreso.setValue(progreso); // Avanza la barra

			    // (Opcional) Retardo visual para ver el efecto de carga
			    try {
			        Thread.sleep(30);
			    } catch (InterruptedException ex) {
			        ex.printStackTrace();
			    }
			}


		} catch (IOException e) {
			// Muestra un mensaje de error si ocurre una excepción al cargar los contactos.
			JOptionPane.showMessageDialog(delegado, "Existen problemas al cargar todos los contactos");
		}
	}

	// Método privado para limpiar los campos de entrada en la GUI y reiniciar variables.
	private void limpiarCampos() {
		// Limpia los campos de nombres, email y teléfono en la GUI.
		delegado.txt_nombres.setText("");
		delegado.txt_telefono.setText("");
		delegado.txt_email.setText("");
		// Reinicia las variables de categoría y favorito.
		categoria = "";
		favorito = false;
		// Desmarca la casilla de favorito y establece la categoría por defecto.
		delegado.chb_favorito.setSelected(favorito);
		delegado.cmb_categoria.setSelectedIndex(0);
		// Reinicia las variables con los valores actuales de la GUI.
		incializacionCampos();
		// Recarga los contactos en la tabla de contactos de la GUI.
		cargarContactosRegistrados();
	}

	// Método que maneja los eventos de acción (clic) en los botones.
	@Override
	public void actionPerformed(ActionEvent e) {
		incializacionCampos(); // Inicializa las variables con los valores actuales de la GUI.

		// Verifica si el evento proviene del botón "Agregar".
		if (e.getSource() == delegado.btn_add) {
			// Verifica si los campos de nombres, teléfono y email no están vacíos.
			if ((!nombres.equals("")) && (!telefono.equals("")) && (!email.equals(""))) {
				// Verifica si se ha seleccionado una categoría válida.
				if ((!categoria.equals("Elija una Categoria")) && (!categoria.equals(""))) {
					// Crea un nuevo objeto persona con los datos ingresados y lo guarda.
					persona = new persona(nombres, telefono, email, categoria, favorito);
					new personaDAO(persona).escribirArchivo();
					// Limpia los campos después de agregar el contacto.
					limpiarCampos();
					// Muestra un mensaje de éxito.
					JOptionPane.showMessageDialog(delegado, "Contacto Registrado!!!");
				} else {
					// Muestra un mensaje de advertencia si no se ha seleccionado una categoría válida.
					JOptionPane.showMessageDialog(delegado, "Elija una Categoria!!!");
				}
			} else {
				// Muestra un mensaje de advertencia si algún campo está vacío.
				JOptionPane.showMessageDialog(delegado, "Todos los campos deben ser llenados!!!");
			}

		} else if (e.getSource() == delegado.btn_eliminar) {
			// Lugar para implementar la funcionalidad de eliminar un contacto.

		} else if (e.getSource() == delegado.btn_modificar) {
			// Lugar para implementar la funcionalidad de modificar un contacto.
		}
	}

	// Método que maneja los eventos de selección en la tabla de contactos.
	@Override
	public void valueChanged(ListSelectionEvent e) {
		// Verifica si la selección está ajustándose (evita ejecuciones múltiples)
		if (!e.getValueIsAdjusting()) {
			int fila = delegado.tablaContactos.getSelectedRow(); // Obtiene la fila seleccionada
			// Verifica si se ha seleccionado una fila válida.
			if (fila != -1) {
				cargarContacto(fila); // Carga los datos del contacto en los campos
			}
		}
	}

	// Método privado para cargar los datos del contacto seleccionado en los campos de la GUI.
	private void cargarContacto(int index) {
		// Establece el nombre del contacto en el campo de texto de nombres.
		delegado.txt_nombres.setText(contactos.get(index).getNombre());
		// Establece el teléfono del contacto en el campo de texto de teléfono.
		delegado.txt_telefono.setText(contactos.get(index).getTelefono());
		// Establece el correo electrónico del contacto en el campo de texto de correo electrónico.
		delegado.txt_email.setText(contactos.get(index).getEmail());
		// Establece el estado de favorito del contacto en el JCheckBox de favorito.
		delegado.chb_favorito.setSelected(contactos.get(index).isFavorito());
		// Establece la categoría del contacto en el JComboBox de categoría.
		delegado.cmb_categoria.setSelectedItem(contactos.get(index).getCategoria());
	}

	// Método que maneja los eventos de cambio de estado en los componentes cmb_categoria y chb_favorito.
	@Override
	public void itemStateChanged(ItemEvent e) {
		// Verifica si el evento proviene del JComboBox de categoría.
		if (e.getSource() == delegado.cmb_categoria) {
			// Obtiene el elemento seleccionado en el JComboBox y lo convierte en una cadena.
			categoria = delegado.cmb_categoria.getSelectedItem().toString();
			// Actualiza la categoría seleccionada en la variable "categoria".
		} else if (e.getSource() == delegado.chb_favorito) {
			// Verifica si el evento proviene del JCheckBox de favorito.
			favorito = delegado.chb_favorito.isSelected();
			// Obtiene el estado seleccionado del JCheckBox y actualiza el estado de favorito en la variable "favorito".
		}
	}
}
