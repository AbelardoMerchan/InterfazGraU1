package vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import controlador.logica_ventana;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTabbedPane;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;



public class ventana extends JFrame {

	public JTabbedPane tabbedPane; // Panel principal que contendrá las pestañas
	public JTable tablaContactos; // Tabla para mostrar contactos
	public DefaultTableModel modeloTabla; // Modelo para la tabla
	public JPanel panelContactos; // Panel que contiene los elementos de contacto
	public JPanel panelEstadisticas; // Panel vacío para estadísticas
	public JProgressBar barraProgreso; // Barra de progreso para mostrar la carga de contactos
    public JTextField txt_nombres; // Campo de texto para ingresar nombres.
	public JTextField txt_telefono; // Campo de texto para ingresar números de teléfono.
	public JTextField txt_email; // Campo de texto para ingresar direcciones de correo electrónico.
	public JTextField txt_buscar; // Campo de texto para buscar por nombre.
	public JCheckBox chb_favorito; // Casilla de verificación para marcar un contacto como favorito.
	public JComboBox cmb_categoria; // Menú desplegable para seleccionar la categoría de contacto.
	public JButton btn_add; // Botón para agregar un nuevo contacto.
	public JButton btn_modificar; // Botón para modificar un contacto existente.
	public JButton btn_eliminar; // Botón para eliminar un contacto.
	public JScrollPane scrLista; // Panel de desplazamiento para la tabla de contactos.

	public void exportarCSV() {
	    try {
	        FileWriter writer = new FileWriter("contactos.csv");

	        // Escribir cabeceras
	        for (int i = 0; i < modeloTabla.getColumnCount(); i++) {
	            writer.write(modeloTabla.getColumnName(i) + ",");
	        }
	        writer.write("\n");

	        // Escribir filas
	        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
	            for (int j = 0; j < modeloTabla.getColumnCount(); j++) {
	                writer.write(modeloTabla.getValueAt(i, j).toString() + ",");
	            }
	            writer.write("\n");
	        }

	        writer.close();
	        JOptionPane.showMessageDialog(this, "Contactos exportados exitosamente.");
	    } catch (IOException e) {
	        JOptionPane.showMessageDialog(this, "Error al exportar el archivo.");
	        e.printStackTrace();
	    }
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
	    // Invoca el método invokeLater de la clase EventQueue para ejecutar la creación de la interfaz de usuario en un hilo de despacho de eventos (Event Dispatch Thread).
	    EventQueue.invokeLater(new Runnable() {
	        public void run() {
	            try {
	                // Dentro de este método, se crea una instancia de la clase ventana, que es la ventana principal de la aplicación.
	                ventana frame = new ventana();
	                // Establece la visibilidad de la ventana como verdadera, lo que hace que la ventana sea visible para el usuario.
	                frame.setVisible(true);
	            } catch (Exception e) {
	                // En caso de que ocurra una excepción durante la creación o visualización de la ventana, se imprime la traza de la pila de la excepción.
	                e.printStackTrace();
	            }
	        }
	    });
	}

	/**
	 * Create the frame.
	 */
	public ventana() {
		setTitle("GESTION DE CONTACTOS"); // Establece el título de la ventana.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Define el comportamiento al cerrar la ventana.
		setResizable(false); // Evita que la ventana sea redimensionable.
		setBounds(100, 100, 1026, 748); // Establece el tamaño y la posición inicial de la ventana.

		tabbedPane = new JTabbedPane(); // Crea el contenedor de pestañas.
		setContentPane(tabbedPane); // Establece el tabbedPane como el contenedor principal.

		// PANEL DE CONTACTOS
		panelContactos = new JPanel(); // Crea el panel de contactos.
		panelContactos.setBorder(new EmptyBorder(5, 5, 5, 5));
		panelContactos.setLayout(null); // Layout manual
		tabbedPane.addTab("Contactos", panelContactos); // Agrega la pestaña "Contactos"

		// PANEL DE ESTADÍSTICAS (vacío)
		panelEstadisticas = new JPanel(); // Crea el panel para futuras estadísticas
		panelEstadisticas.setLayout(null);
		tabbedPane.addTab("Estadísticas", panelEstadisticas); // Agrega la pestaña "Estadísticas"

		// Etiquetas
		JLabel lbl_etiqueta1 = new JLabel("NOMBRES:");
		lbl_etiqueta1.setBounds(25, 41, 89, 13);
		lbl_etiqueta1.setFont(new Font("Tahoma", Font.BOLD, 15));
		panelContactos.add(lbl_etiqueta1);

		JLabel lbl_etiqueta2 = new JLabel("TELEFONO:");
		lbl_etiqueta2.setBounds(25, 80, 89, 13);
		lbl_etiqueta2.setFont(new Font("Tahoma", Font.BOLD, 15));
		panelContactos.add(lbl_etiqueta2);

		JLabel lbl_etiqueta3 = new JLabel("EMAIL:");
		lbl_etiqueta3.setBounds(25, 122, 89, 13);
		lbl_etiqueta3.setFont(new Font("Tahoma", Font.BOLD, 15));
		panelContactos.add(lbl_etiqueta3);

		JLabel lbl_etiqueta4 = new JLabel("BUSCAR POR NOMBRE:");
		lbl_etiqueta4.setFont(new Font("Tahoma", Font.BOLD, 15));
		lbl_etiqueta4.setBounds(25, 661, 192, 13);
		panelContactos.add(lbl_etiqueta4);

		// Campos de texto
		txt_nombres = new JTextField();
		txt_nombres.setBounds(124, 28, 427, 31);
		txt_nombres.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txt_nombres.setColumns(10);
		panelContactos.add(txt_nombres);

		txt_telefono = new JTextField();
		txt_telefono.setBounds(124, 69, 427, 31);
		txt_telefono.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txt_telefono.setColumns(10);
		panelContactos.add(txt_telefono);

		txt_email = new JTextField();
		txt_email.setBounds(124, 110, 427, 31);
		txt_email.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txt_email.setColumns(10);
		panelContactos.add(txt_email);

		txt_buscar = new JTextField();
		txt_buscar.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txt_buscar.setColumns(10);
		txt_buscar.setBounds(212, 650, 784, 31);
		panelContactos.add(txt_buscar);

		// Evento: ENTER en el campo de búsqueda
		txt_buscar.addKeyListener(new java.awt.event.KeyAdapter() {
		    public void keyPressed(java.awt.event.KeyEvent evt) {
		        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
		            System.out.println("Buscar: " + txt_buscar.getText());
		        }
		    }
		});

		// Casilla de verificación
		chb_favorito = new JCheckBox("CONTACTO FAVORITO");
		chb_favorito.setBounds(24, 170, 193, 21);
		chb_favorito.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panelContactos.add(chb_favorito);

		// ComboBox para categorías
		cmb_categoria = new JComboBox();
		cmb_categoria.setBounds(300, 167, 251, 31);
		panelContactos.add(cmb_categoria);

		String[] categorias = {"Elija una Categoria", "Familia", "Amigos", "Trabajo"};
		for (String categoria : categorias) {
		    cmb_categoria.addItem(categoria);
		}

		// Botones principales
		btn_add = new JButton("AGREGAR");
		btn_add.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btn_add.setBounds(601, 70, 125, 65);
		panelContactos.add(btn_add);

		btn_modificar = new JButton("MODIFICAR");
		btn_modificar.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btn_modificar.setBounds(736, 70, 125, 65);
		panelContactos.add(btn_modificar);

		btn_eliminar = new JButton("ELIMINAR");
		btn_eliminar.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btn_eliminar.setBounds(871, 69, 125, 65);
		panelContactos.add(btn_eliminar);
		
		JButton btn_exportar = new JButton("EXPORTAR CSV");
		btn_exportar.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btn_exportar.setBounds(850, 10, 150, 30); 
		panelContactos.add(btn_exportar);
		btn_exportar.addActionListener(e -> exportarCSV());
		
		barraProgreso = new JProgressBar();
		barraProgreso.setBounds(25, 610, 971, 20); // Ubicación y tamaño
		barraProgreso.setStringPainted(true); // Muestra el porcentaje
		panelContactos.add(barraProgreso);



		// Tabla de contactos con encabezados
		String[] columnas = {"Nombre", "Teléfono", "Email", "Categoría", "Favorito"};
		modeloTabla = new DefaultTableModel(columnas, 0); // Modelo vacío
		tablaContactos = new JTable(modeloTabla);
		tablaContactos.setFont(new Font("Tahoma", Font.PLAIN, 15));
		tablaContactos.setRowHeight(25);
		tablaContactos.setAutoCreateRowSorter(true); // Permite ordenar columnas

		// Scroll para la tabla
		scrLista = new JScrollPane(tablaContactos);
		scrLista.setBounds(25, 242, 971, 398);
		panelContactos.add(scrLista);

		// Menú emergente al hacer clic derecho sobre la tabla
		JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem editar = new JMenuItem("Editar");
		JMenuItem eliminar = new JMenuItem("Eliminar");

		popupMenu.add(editar);
		popupMenu.add(eliminar);
		tablaContactos.setComponentPopupMenu(popupMenu);

		// Evento del menú emergente: Editar
		editar.addActionListener(e -> {
		    int fila = tablaContactos.getSelectedRow();
		    if (fila != -1) {
		        System.out.println("Editar: " + modeloTabla.getValueAt(fila, 0));
		    }
		});

		// Evento del menú emergente: Eliminar
		eliminar.addActionListener(e -> {
		    int fila = tablaContactos.getSelectedRow();
		    if (fila != -1) {
		        modeloTabla.removeRow(fila);
		        System.out.println("Contacto eliminado");
		    }
		});

		// Instanciar el controlador para usar el delegado
		logica_ventana lv = new logica_ventana(this);
	}
}
