package com.vialectorislibreria.lab5.data;

import com.vialectorislibreria.lab5.domain.Autor;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class AutorXmlData {
    private String rutaArchivo;

    /**
     * Constructor que verifica si el archivo existe y lo crea si no
     */
    public AutorXmlData(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
        File archivo = new File(rutaArchivo);

        if (!archivo.exists()) {
            crearArchivoVacio();
        }
    }

    /**
     * Método para crear un archivo XML vacío con la estructura básica
     */
    private void crearArchivoVacio() {
        try {
            // Asegurar que el directorio existe
            File archivo = new File(rutaArchivo);
            File directorio = archivo.getParentFile();
            if (directorio != null && !directorio.exists()) {
                directorio.mkdirs();
            }

            Element raiz = new Element("autores");
            Document documento = new Document(raiz);

            XMLOutputter xmlOutput = new XMLOutputter();
            xmlOutput.setFormat(Format.getPrettyFormat());
            xmlOutput.output(documento, new FileOutputStream(rutaArchivo));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método insertar (adiciona al final del archivo)
     */
    public void insertar(Autor autor) {
        try {
            File archivoXml = new File(rutaArchivo);

            // Si el archivo no existe o está vacío, crear uno nuevo
            if (!archivoXml.exists() || archivoXml.length() == 0) {
                crearArchivoVacio();
            }

            // Cargar el documento existente
            SAXBuilder builder = new SAXBuilder();
            Document documento = builder.build(new File(rutaArchivo));
            Element raiz = documento.getRootElement();

            // Crear elemento para el nuevo autor
            Element elementoAutor = new Element("autor");
            elementoAutor.setAttribute("idAutor", String.valueOf(autor.getIdAutor()));

            elementoAutor.addContent(new Element("nombre").setText(autor.getNombre()));
            elementoAutor.addContent(new Element("apellidos").setText(autor.getApellidos()));
            elementoAutor.addContent(new Element("nacionalidad").setText(autor.getNacionalidad()));

            // Adicionar al final del archivo
            raiz.addContent(elementoAutor);

            // Guardar el documento
            XMLOutputter xmlOutput = new XMLOutputter();
            xmlOutput.setFormat(Format.getPrettyFormat());
            xmlOutput.output(documento, new FileOutputStream(rutaArchivo));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Método findAll (retorna todos los registros de autor presentes en el archivo)
     * El método debe retornar un Set de autores
     */
    public Set<Autor> findAll() {
        Set<Autor> autoresSet = new HashSet<>();

        try {
            File archivo = new File(rutaArchivo);

            // Verificar si el archivo existe y no está vacío
            if (!archivo.exists() || archivo.length() == 0) {
                return autoresSet; // Retornar set vacío
            }

            SAXBuilder builder = new SAXBuilder();
            Document documento = builder.build(archivo);
            Element raiz = documento.getRootElement();

            List<Element> autores = raiz.getChildren("autor");

            for (Element autorElement : autores) {
                int id = Integer.parseInt(autorElement.getAttributeValue("idAutor"));
                Autor autor = new Autor();
                autor.setIdAutor(id);
                autor.setNombre(autorElement.getChildText("nombre"));
                autor.setApellidos(autorElement.getChildText("apellidos"));
                autor.setNacionalidad(autorElement.getChildText("nacionalidad"));

                autoresSet.add(autor);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return autoresSet;
    }

    /**
     * Método adicional para buscar autor por ID (útil para LibroXmlData)
     */
    public Optional<Autor> findAutorById(int idAutor) {
        try {
            File archivo = new File(rutaArchivo);

            // Verificar si el archivo existe y no está vacío
            if (!archivo.exists() || archivo.length() == 0) {
                return Optional.empty(); // Retornar Optional vacío
            }

            SAXBuilder builder = new SAXBuilder();
            Document documento = builder.build(archivo);
            Element raiz = documento.getRootElement();

            List<Element> autores = raiz.getChildren("autor");

            for (Element autorElement : autores) {
                int id = Integer.parseInt(autorElement.getAttributeValue("idAutor"));
                if (id == idAutor) {
                    Autor autor = new Autor();
                    autor.setIdAutor(id);
                    autor.setNombre(autorElement.getChildText("nombre"));
                    autor.setApellidos(autorElement.getChildText("apellidos"));
                    autor.setNacionalidad(autorElement.getChildText("nacionalidad"));

                    return Optional.of(autor);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }
}