package com.vialectorislibreria.lab5.servlets;

import com.vialectorislibreria.lab5.data.AutorXmlData;
import com.vialectorislibreria.lab5.data.LibroXmlData;
import com.vialectorislibreria.lab5.domain.Autor;
import com.vialectorislibreria.lab5.domain.Libro;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

@WebServlet(name = "BuscarLibrosServlet", urlPatterns = {"/buscar-libros"})
public class BuscarLibrosServlet extends HttpServlet {

    private static final String RUTA_AUTORES = "WEB-INF/autores.xml";
    private static final String RUTA_LIBROS = "WEB-INF/libros.xml";

    private AutorXmlData autorData;
    private LibroXmlData libroData;

    @Override
    public void init() throws ServletException {
        // Obtener las rutas completas dentro del contexto de la aplicación
        String rutaCompletaAutores = getServletContext().getRealPath(RUTA_AUTORES);
        String rutaCompletaLibros = getServletContext().getRealPath(RUTA_LIBROS);

        // Inicializar los objetos de acceso a datos
        autorData = new AutorXmlData(rutaCompletaAutores);
        libroData = new LibroXmlData(rutaCompletaLibros, autorData);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Obtener todos los autores para el combo box
        Set<Autor> autores = autorData.findAll();

        // Guardar la lista de autores en el ámbito de la solicitud
        request.setAttribute("autores", autores);

        // Redirigir al JSP
        request.getRequestDispatcher("buscarLibros.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Recuperar el ID del autor seleccionado
        String idAutorStr = request.getParameter("idAutor");

        if (idAutorStr != null && !idAutorStr.isEmpty()) {
            try {
                int idAutor = Integer.parseInt(idAutorStr);

                // Buscar los libros asociados al autor
                Map<String, Libro> libros = libroData.findLibrosByIdAutor(idAutor);

                // Guardar los resultados en el ámbito de la solicitud
                request.setAttribute("libros", libros.values());
                request.setAttribute("idAutorSeleccionado", idAutor);

            } catch (NumberFormatException e) {
                // Manejar error de formato de número
                request.setAttribute("error", "El ID del autor no es válido");
            }
        }

        // Volver a obtener todos los autores para el combo box
        Set<Autor> autores = autorData.findAll();
        request.setAttribute("autores", autores);

        // Redirigir al JSP
        request.getRequestDispatcher("buscarLibros.jsp").forward(request, response);
    }
}