package vista;

import java.awt.EventQueue;
import java.awt.Color;
import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import controlador.logica_ventana;
import java.io.FileWriter;
import java.io.IOException;

public class ventana extends JFrame {

    public JTabbedPane tabbedPane;               // Panel principal que contendrá las pestañas
    public JTable tablaContactos;                // Tabla para mostrar contactos
    public DefaultTableModel modeloTabla;        // Modelo para la tabla
    public JPanel panelContactos;                // Panel que contiene los elementos de contacto
    public JPanel panelEstadisticas;             // Panel vacío para estadísticas
    public JProgressBar barraProgreso;           // Barra de progreso para mostrar la carga de contactos
    public JTextField txt_nombres;               // Campo de texto para ingresar nombres.
    public JTextField txt_telefono;              // Campo de texto para ingresar números de teléfono.
    public JTextField txt_email;                 // Campo de texto para ingresar direcciones de correo electrónico.
    public JTextField txt_buscar;                // Campo de texto para buscar por nombre.
    public JCheckBox chb_favorito;               // Casilla de verificación para marcar un contacto como favorito.
    public JComboBox<String> cmb_categoria;      // Menú desplegable para seleccionar la categoría de contacto.
    public JButton btn_add;                      // Botón para agregar un nuevo contacto.
    public JButton btn_modificar;                // Botón para modificar un contacto existente.
    public JButton btn_eliminar;                 // Botón para eliminar un contacto.
    public JButton btn_exportar;                 // Botón para exportar contactos a CSV
    public JComboBox<String> cmb_idioma;         // Combo para selección de idioma
    public JScrollPane scrLista;                 // Panel de desplazamiento para la tabla de contactos

    private ResourceBundle mensajes;

    /**
     * Exporta la tabla de contactos a un archivo CSV.
     */
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
        EventQueue.invokeLater(() -> {
            try {
                ventana frame = new ventana();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Constructor por defecto, establece español.
     */
    public ventana() {
        this(new Locale("es"));
    }

    /**
     * Constructor con selección de idioma.
     */
    public ventana(Locale idiomaSeleccionado) {
        inicializarComponentes(idiomaSeleccionado);
    }

    /**
     * Inicializa todos los componentes de la interfaz según el idioma.
     */
    private void inicializarComponentes(Locale idioma) {
        // Colores y fuentes
        Color fondoPrincipal = new Color(240, 242, 245);   // #F0F2F5
        Color textoPrincipal = new Color(51, 51, 51);      // #333333
        Color azulPrimario = new Color(25, 118, 210);      // #1976D2
        Font fuenteGeneral = new Font("Segoe UI", Font.PLAIN, 14);
        Font fuenteEtiquetas = new Font("Segoe UI", Font.BOLD, 15);

        // Cargar resource bundle
        mensajes = ResourceBundle.getBundle("i18n.messages", idioma);

        // Configuración de la ventana
        setTitle(mensajes.getString("titulo"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setBounds(100, 100, 1026, 748);

        // Panel de pestañas
        tabbedPane = new JTabbedPane();
        setContentPane(tabbedPane);

        // PANEL DE CONTACTOS
        panelContactos = new JPanel();
        panelContactos.setBorder(new EmptyBorder(5, 5, 5, 5));
        panelContactos.setLayout(null);
        tabbedPane.addTab(mensajes.getString("menu_contactos"), panelContactos);

        // PANEL DE ESTADÍSTICAS (vacío)
        panelEstadisticas = new JPanel();
        panelEstadisticas.setLayout(null);
        tabbedPane.addTab(mensajes.getString("menu_estadisticas"), panelEstadisticas);

        // Carga de iconos
        ImageIcon iconAdd    = new ImageIcon("img/add.png");
        ImageIcon iconEdit   = new ImageIcon("img/edit.png");
        ImageIcon iconDelete = new ImageIcon("img/delete.png");
        ImageIcon iconExport = new ImageIcon("img/export.png");

        // Etiquetas
        JLabel lbl_etiqueta1 = new JLabel(mensajes.getString("nombres"));
        lbl_etiqueta1.setBounds(25, 41, 89, 13);
        lbl_etiqueta1.setFont(fuenteEtiquetas);
        lbl_etiqueta1.setForeground(textoPrincipal);
        panelContactos.add(lbl_etiqueta1);

        JLabel lbl_etiqueta2 = new JLabel(mensajes.getString("telefono"));
        lbl_etiqueta2.setBounds(25, 80, 89, 13);
        lbl_etiqueta2.setFont(fuenteEtiquetas);
        lbl_etiqueta2.setForeground(textoPrincipal);
        panelContactos.add(lbl_etiqueta2);

        JLabel lbl_etiqueta3 = new JLabel(mensajes.getString("email"));
        lbl_etiqueta3.setBounds(25, 122, 89, 13);
        lbl_etiqueta3.setFont(fuenteEtiquetas);
        lbl_etiqueta3.setForeground(textoPrincipal);
        panelContactos.add(lbl_etiqueta3);

        JLabel lbl_etiqueta4 = new JLabel(mensajes.getString("buscar"));
        lbl_etiqueta4.setBounds(25, 661, 192, 13);
        lbl_etiqueta4.setFont(fuenteEtiquetas);
        lbl_etiqueta4.setForeground(textoPrincipal);
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
        txt_buscar.setBounds(212, 650, 784, 31);
        txt_buscar.setFont(new Font("Tahoma", Font.PLAIN, 15));
        txt_buscar.setColumns(10);
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
        chb_favorito = new JCheckBox(mensajes.getString("favorito"));
        chb_favorito.setBounds(24, 170, 193, 21);
        chb_favorito.setFont(fuenteGeneral);
        chb_favorito.setBackground(fondoPrincipal);
        chb_favorito.setForeground(textoPrincipal);
        panelContactos.add(chb_favorito);

        // ComboBox para categorías
        cmb_categoria = new JComboBox<>();
        cmb_categoria.setBounds(300, 167, 251, 31);
        cmb_categoria.setFont(fuenteGeneral);
        cmb_categoria.setBackground(Color.WHITE);
        cmb_categoria.setForeground(textoPrincipal);
        cmb_categoria.addItem(mensajes.getString("categoria"));
        cmb_categoria.addItem(mensajes.getString("familia"));
        cmb_categoria.addItem(mensajes.getString("amigos"));
        cmb_categoria.addItem(mensajes.getString("trabajo"));
        panelContactos.add(cmb_categoria);

        // Botones principales
        btn_add       = new JButton(mensajes.getString("agregar"), iconAdd);
        btn_modificar = new JButton(mensajes.getString("editar"),  iconEdit);
        btn_eliminar  = new JButton(mensajes.getString("eliminar"), iconDelete);
        for (JButton btn : new JButton[]{btn_add, btn_modificar, btn_eliminar}) {
            btn.setFont(fuenteGeneral);
            btn.setBackground(azulPrimario);
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setHorizontalTextPosition(SwingConstants.RIGHT);
            btn.setIconTextGap(10);
            btn.setMargin(new Insets(0, 10, 0, 0));
        }
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelBotones.setBounds(580, 70, 460, 65);
        panelBotones.setBackground(fondoPrincipal);
        panelBotones.add(btn_add);
        panelBotones.add(btn_modificar);
        panelBotones.add(btn_eliminar);
        panelContactos.add(panelBotones);

        // Botón Exportar CSV
        btn_exportar = new JButton(mensajes.getString("exportar"), iconExport);
        btn_exportar.setFont(fuenteGeneral);
        btn_exportar.setBounds(800, 10, 180, 30);
        btn_exportar.setBackground(azulPrimario);
        btn_exportar.setForeground(Color.WHITE);
        btn_exportar.setFocusPainted(false);
        btn_exportar.addActionListener(e -> exportarCSV());
        panelContactos.add(btn_exportar);

        // ComboBox para cambiar el idioma
        String[] listaIdiomas = {"Español", "English", "Português"};
        cmb_idioma = new JComboBox<>(listaIdiomas);
        cmb_idioma.setBounds(650, 10, 130, 30);
        cmb_idioma.setFont(fuenteGeneral);
        cmb_idioma.setBackground(Color.WHITE);
        cmb_idioma.setForeground(textoPrincipal);
        panelContactos.add(cmb_idioma);
        cmb_idioma.addActionListener(e -> {
            Locale nuevo = switch ((String)cmb_idioma.getSelectedItem()) {
                case "English"   -> new Locale("en");
                case "Português" -> new Locale("pt");
                default           -> new Locale("es");
            };
            dispose();
            new ventana(nuevo).setVisible(true);
        });

        // Barra de progreso
        barraProgreso = new JProgressBar();
        barraProgreso.setBounds(25, 610, 971, 20);
        barraProgreso.setStringPainted(true);
        panelContactos.add(barraProgreso);

        // Tabla de contactos
     // Cabeceras localizadas
        String[] columnas = {
            mensajes.getString("col_nombre"),
            mensajes.getString("col_telefono"),
            mensajes.getString("col_email"),
            mensajes.getString("col_categoria"),
            mensajes.getString("col_favorito")
        };
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaContactos = new JTable(modeloTabla);

        tablaContactos.setFont(fuenteGeneral);
        tablaContactos.setRowHeight(25);
        tablaContactos.setGridColor(new Color(230,230,230));
        tablaContactos.setForeground(new Color(33,33,33));
        tablaContactos.setBackground(Color.WHITE);
        tablaContactos.setAutoCreateRowSorter(true);
        JTableHeader cabecera = tablaContactos.getTableHeader();
        cabecera.setFont(new Font("Segoe UI",Font.BOLD,14));
        cabecera.setBackground(azulPrimario);
        cabecera.setForeground(Color.WHITE);
        cabecera.setReorderingAllowed(false);
        scrLista = new JScrollPane(tablaContactos);
        scrLista.setBounds(25,242,971,398);
        panelContactos.add(scrLista);

        // Menú emergente al hacer clic derecho sobre la tabla
        JPopupMenu popup = new JPopupMenu();
        JMenuItem itemEditar   = new JMenuItem(mensajes.getString("editar"));
        JMenuItem itemEliminar = new JMenuItem(mensajes.getString("eliminar"));
        popup.add(itemEditar);
        popup.add(itemEliminar);
        tablaContactos.setComponentPopupMenu(popup);
        itemEditar.addActionListener(e -> {
            int fila = tablaContactos.getSelectedRow();
            if (fila != -1) {
                System.out.println("Editar: " + modeloTabla.getValueAt(fila,0));
            }
        });
        itemEliminar.addActionListener(e -> {
            int fila = tablaContactos.getSelectedRow();
            if (fila != -1) {
                modeloTabla.removeRow(fila);
                System.out.println("Contacto eliminado");
            }
        });

        // Instanciar el controlador para usar el delegado
        new logica_ventana(this);
    }
}
