package co.umariana.lamento.web;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * GET /listar (tarea de SERVLETS)
 *
 * <p>
 * Contrato (las pruebas lo revisan):
 * <ul>
 * <li>Si el parámetro {@code orden} vale {@code "ranking"}, el atributo
 * {@code "lamentos"} de la petición
 * es {@code Contexto.servicio().ranking()}; en cualquier otro caso es
 * {@code listar()}.</li>
 * <li>Siempre pone el atributo {@code "promedios"} =
 * {@code promedioPorCategoria()}.</li>
 * <li>Reenvía (forward) a {@code /WEB-INF/vistas/listar.jsp}, que debe mostrar
 * la tabla de lamentos
 * (con un botón para eliminar cada uno) y los promedios.</li>
 * </ul>
 */
@WebServlet("/listar")
public class ListarLamentos extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String orden = req.getParameter("orden");

        if ("ranking".equals(orden)) {
            req.setAttribute("lamentos", Contexto.servicio().ranking());

        } else {
            req.setAttribute("lamentos", Contexto.servicio().listar());
        }

        req.setAttribute("promedios", Contexto.servicio().promedioPorCategoria());

        req.getRequestDispatcher("/WEB-INF/vistas/listar.jsp").forward(req, resp);
    }
}