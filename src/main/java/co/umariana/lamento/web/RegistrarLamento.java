package co.umariana.lamento.web;

import co.umariana.lamento.modelo.Lamento;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * /registrar (tarea de SERVLETS)
 *
 * <p>
 * Contrato (las pruebas lo revisan):
 * <ul>
 * <li><b>GET:</b> reenvía a {@code /WEB-INF/vistas/formulario.jsp}.</li>
 * <li><b>POST:</b> lee los parámetros
 * {@code codigo, titulo, categoria, indignacion, duracion,
 *       descripcion, archivo}; crea un {@code Lamento} (la indignación es un
 * entero y la duración un
 * decimal) y llama a {@code Contexto.servicio().agregar(...)}. Si todo sale
 * bien, redirige a
 * {@code "listar"} ({@code resp.sendRedirect("listar")}).</li>
 * <li><b>Si algo falla</b> ({@link NumberFormatException} o
 * {@link IllegalArgumentException}), NO redirige:
 * pone el mensaje de la excepción en el atributo {@code "error"} y reenvía de
 * nuevo a
 * {@code formulario.jsp}, que debe mostrar ese error.</li>
 * </ul>
 */
@WebServlet("/registrar")
public class RegistrarLamento extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.getRequestDispatcher("/WEB-INF/vistas/formulario.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        try {

            String codigo = req.getParameter("codigo");
            String titulo = req.getParameter("titulo");
            String categoria = req.getParameter("categoria");
            String descripcion = req.getParameter("descripcion");
            String archivo = req.getParameter("archivo");

            int indignacion = Integer.parseInt(req.getParameter("indignacion"));
            double duracion = Double.parseDouble(req.getParameter("duracion"));

            Lamento lamento = new Lamento(codigo, titulo, categoria, indignacion, duracion, descripcion, archivo);
            Contexto.servicio().agregar(lamento);

            resp.sendRedirect("listar");

        } catch (IllegalArgumentException e) {

            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/WEB-INF/vistas/formulario.jsp").forward(req, resp);
        }
    }
}