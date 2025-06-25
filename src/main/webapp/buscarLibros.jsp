<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.vialectorislibreria.lab5.domain.Autor" %>
<%@ page import="com.vialectorislibreria.lab5.domain.Libro" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.Collection" %>

<!DOCTYPE html>
<html>
<head>
    <title>Buscar Libros por Autor</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
        }
        h1 {
            color: #333;
        }
        form {
            margin-bottom: 20px;
        }
        select, button {
            padding: 8px;
            margin: 10px 0;
        }
        table {
            border-collapse: collapse;
            width: 100%;
        }
        th, td {
            border: 1px solid #ddd;
            padding: 8px;
            text-align: left;
        }
        th {
            background-color: #f2f2f2;
        }
        tr:nth-child(even) {
            background-color: #f9f9f9;
        }
        .message {
            color: #666;
            font-style: italic;
        }
    </style>
</head>
<body>
<h1>Buscar Libros por Autor</h1>

<form action="buscar-libros" method="POST">
    <label for="idAutor">Seleccione un autor:</label>
    <select id="idAutor" name="idAutor" required>
        <option value="">-- Seleccione un autor --</option>
        <%
            Set<Autor> autores = (Set<Autor>)request.getAttribute("autores");
            if (autores != null) {
                for (Autor autor : autores) {
                    String selected = "";
                    Object idAutorSeleccionado = request.getAttribute("idAutorSeleccionado");
                    if (idAutorSeleccionado != null && autor.getIdAutor() == (int)idAutorSeleccionado) {
                        selected = "selected";
                    }
        %>
        <option value="<%= autor.getIdAutor() %>" <%= selected %>><%= autor.getNombre() %> <%= autor.getApellidos() %></option>
        <%
                }
            }
        %>
    </select>
    <button type="submit">Buscar</button>
</form>

<%
    Collection<Libro> libros = (Collection<Libro>)request.getAttribute("libros");
    if (libros != null) {
        if (libros.isEmpty()) {
%>
<p class="message">No se encontraron libros para el autor seleccionado.</p>
<%
} else {
%>
<h2>Resultados de la búsqueda</h2>
<table>
    <thead>
    <tr>
        <th>Título</th>
        <th>Año de Publicación</th>
        <th>Autores</th>
    </tr>
    </thead>
    <tbody>
    <%
        for (Libro libro : libros) {
    %>
    <tr>
        <td><%= libro.getTitulo() %></td>
        <td><%= libro.getAnnoPublicacion() %></td>
        <td>
            <%
                for (Autor autor : libro.getAutores()) {
                    out.println(autor.getNombre() + " " + autor.getApellidos() + "<br>");
                }
            %>
        </td>
    </tr>
    <%
        }
    %>
    </tbody>
</table>
<%
        }
    }
%>
</body>
</html>