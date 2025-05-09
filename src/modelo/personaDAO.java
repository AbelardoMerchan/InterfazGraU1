package modelo;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//Definición de la clase pública "personaDAO"
public class personaDAO {

	// Declaración de atributos privados de la clase "personaDAO"
	private File archivo; // Archivo donde se almacenarán los datos de los contactos
	private persona persona; // Objeto "persona" que se gestionará

	// Constructor público de la clase "personaDAO" que recibe un objeto "persona" como parámetro
	public personaDAO(persona persona) {
		this.persona = persona; // Asigna el objeto "persona" recibido al atributo de la clase
		archivo = new File("c:/gestionContactos"); // Establece la ruta donde se alojará el archivo
		// Llama al método para preparar el archivo
		prepararArchivo();
	}

	// Método privado para gestionar el archivo utilizando la clase File
	private void prepararArchivo() {
		// Verifica si el directorio existe
		if (!archivo.exists()) { // Si el directorio no existe, se crea
			archivo.mkdir();
		}

		// Accede al archivo "datosContactos.csv" dentro del directorio especificado
		archivo = new File(archivo.getAbsolutePath(), "datosContactos.csv");
		// Verifica si el archivo existe
		if (!archivo.exists()) { // Si el archivo no existe, se crea
			try {
				archivo.createNewFile();
				// Prepara el encabezado para el archivo de csv
				String encabezado = String.format("%s;%s;%s;%s;%s", "NOMBRE", "TELEFONO", "EMAIL", "CATEGORIA", "FAVORITO");
				escribir(encabezado);
			} catch (IOException e) {
				// Maneja la excepción de entrada/salida
				e.printStackTrace();
			}
		}
	}

	// Método privado para escribir texto en el archivo
	private void escribir(String texto) {
		// Prepara el archivo para escribir en la última línea
		FileWriter escribir;
		try {
			escribir = new FileWriter(archivo.getAbsolutePath(), true);
			escribir.write(texto + "\n"); // Escribe los datos del contacto en el archivo
			escribir.close(); // Cierra el archivo
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Método público para escribir un contacto en el archivo
	public boolean escribirArchivo() {
		escribir(persona.datosContacto());
		return true; // Retorna true si la escritura fue exitosa
	}

	// Método público para leer los datos del archivo
	public List<persona> leerArchivo() throws IOException {
		String contactos = ""; // Cadena que contendrá toda la data del archivo
		FileReader leer = new FileReader(archivo.getAbsolutePath());
		int c;
		while ((c = leer.read()) != -1) { // Lee hasta la última línea del archivo
			contactos += String.valueOf((char) c);
		}
		String[] datos = contactos.split("\n"); // Separa cada contacto por salto de línea
		List<persona> personas = new ArrayList<>(); // Lista para almacenar cada persona
		for (String contacto : datos) {
			if (contacto.trim().isEmpty() || contacto.startsWith("NOMBRE")) continue; // Ignora línea vacía o encabezado
			persona p = new persona();
			p.setNombre(contacto.split(";")[0]);
			p.setTelefono(contacto.split(";")[1]);
			p.setEmail(contacto.split(";")[2]);
			p.setCategoria(contacto.split(";")[3]);
			p.setFavorito(Boolean.parseBoolean(contacto.split(";")[4]));
			personas.add(p); // Añade a la lista
		}
		leer.close(); // Cierra el archivo
		return personas; // Retorna la lista
	}

	// Método público para sobrescribir completamente el archivo con una nueva lista de contactos
	public synchronized void sobrescribirArchivo(List<persona> personas) throws IOException {
		// Abre el archivo en modo sobrescritura (false)
		FileWriter escribir = new FileWriter(archivo.getAbsolutePath(), false);
		// Escribe el encabezado
		escribir.write("NOMBRE;TELEFONO;EMAIL;CATEGORIA;FAVORITO\n");

		// Escribe cada persona en el archivo
		for (persona p : personas) {
			escribir.write(p.datosContacto() + "\n");
		}

		// Cierra el archivo
		escribir.close();
	}
}
