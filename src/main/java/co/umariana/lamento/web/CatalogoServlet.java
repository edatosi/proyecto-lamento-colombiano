package co.umariana.lamento.web;

import co.umariana.lamento.modelo.Lamento;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

/**
 * Página del catálogo: muestra los audios que vienen en el proyecto y permite agregarlos al
 * Lamentómetro con un botón. Es la forma de tener datos para probar la aplicación.
 * La presentación usa las mismas clases CSS que las vistas JSP (tarjeta, medidor, alerta...).
 */
@WebServlet("/catalogo")
public class CatalogoServlet extends HttpServlet {

    private static final Locale ES = Locale.forLanguageTag("es-CO");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html;charset=UTF-8");
        String ok = req.getParameter("ok");
        String error = req.getParameter("error");
        List<Catalogo.Entrada> entradas = Catalogo.entradas();
        PrintWriter out = resp.getWriter();

        out.println("<!DOCTYPE html><html lang=\"es\"><head><meta charset=\"UTF-8\">");
        out.println("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">");
        out.println("<title>Catálogo · Lamentómetro</title><link rel=\"stylesheet\" href=\"css/estilos.css\"></head>");
        out.println("<body class=\"app\">");

        // Cabecera y menú (igual que en las demás páginas)
        out.println("<header class=\"sitio__cabecera\"><div class=\"contenedor sitio__barra\">");
        out.println("<a class=\"marca\" href=\"index.html\"><span aria-hidden=\"true\">🎙️</span> Lamentómetro</a>");
        out.println("<nav class=\"menu\" aria-label=\"Principal\">");
        out.println("<a href=\"index.html\">Inicio</a>");
        out.println("<a href=\"catalogo\" aria-current=\"page\">Catálogo</a>");
        out.println("<a href=\"listar\">Lamentos</a>");
        out.println("<a href=\"registrar\">Registrar</a>");
        out.println("<a href=\"reproductor\">Reproductor</a>");
        out.println("</nav></div></header>");

        out.println("<main>");

        // Título de la página + acciones
        out.println("<div class=\"encabezado\"><div>");
        out.println("<h1>Catálogo de audios</h1>");
        out.println("<p>" + entradas.size() + (entradas.size() == 1 ? " audio disponible" : " audios disponibles")
                + ". Pulse <strong>Agregar</strong> para guardarlo en el Lamentómetro.</p>");
        out.println("</div><div class=\"acciones\">");
        out.println("<a class=\"boton boton--secundario\" href=\"listar\">📋 Ver lamentos</a>");
        out.println("</div></div>");

        // Mensajes de resultado
        if (ok != null) {
            out.println("<div class=\"alerta alerta--ok\" role=\"status\">✔ Se agregó «" + Html.esc(ok) + "».</div>");
        }
        if (error != null) {
            out.println("<div class=\"alerta alerta--error\" role=\"alert\">⚠ " + Html.esc(error) + "</div>");
        }

        if (entradas.isEmpty()) {
            out.println("<section class=\"tarjeta vacio\" aria-live=\"polite\">");
            out.println("<span class=\"vacio__icono\" aria-hidden=\"true\">📭</span>");
            out.println("<h2>El catálogo está vacío</h2>");
            out.println("<p>No se encontraron audios en <code>catalogo.txt</code>. También puede registrar uno a mano.</p>");
            out.println("<div class=\"acciones acciones--centro\"><a class=\"boton\" href=\"registrar\">Registrar un lamento</a></div>");
            out.println("</section>");
        } else {
            // Cuadrícula de tarjetas
            out.println("<section class=\"catalogo\" aria-label=\"Audios del catálogo\">");
            for (Catalogo.Entrada e : entradas) {
                imprimirTarjeta(out, e);
            }
            out.println("</section>");
        }

        out.println("</main>");
        out.println("<footer>Proyecto final · Estructuras de Datos I</footer>");
        out.println("</body></html>");
    }

    /** Pinta un audio del catálogo como una tarjeta: datos arriba, reproductor en medio y botón abajo. */
    private static void imprimirTarjeta(PrintWriter out, Catalogo.Entrada e) {
        Lamento l = e.lamento();
        boolean agregado = yaAgregado(l.getCodigo());
        int nivel = l.getIndignacion();
        String duracion = String.format(ES, "%.2f", l.getDuracionSeg());
        String titulo = Html.esc(l.getTitulo());

        out.println("<article class=\"tarjeta catalogo__tarjeta\">");

        // Código y categoría
        out.println("<div class=\"catalogo__cabecera\">");
        out.println("<code>" + Html.esc(l.getCodigo()) + "</code>");
        out.println("<span class=\"etiqueta\">" + Html.esc(l.getCategoria()) + "</span>");
        out.println("</div>");

        out.println("<h2 class=\"catalogo__titulo\">" + titulo + "</h2>");
        out.println("<p class=\"catalogo__descripcion\">" + Html.esc(l.getDescripcion()) + "</p>");

        // Medidor de indignación (mismo componente que la lista)
        out.println("<div class=\"medidor medidor--" + clasificar(nivel) + "\" role=\"img\" aria-label=\"Indignación "
                + nivel + " de 10\">");
        out.println("<span class=\"medidor__pista\"><span class=\"medidor__barra\" style=\"--nivel:" + (nivel * 10)
                + "%\"></span></span>");
        out.println("<span class=\"medidor__valor\">" + nivel + "/10</span>");
        out.println("</div>");

        // Duración y autor
        out.println("<p class=\"catalogo__meta\"><span>⏱ " + duracion + " s</span><span>🎤 " + Html.esc(e.autor())
                + "</span></p>");

        out.println("<audio controls preload=\"none\" src=\"media/" + Html.esc(l.getArchivo())
                + "\" aria-label=\"Escuchar " + titulo + "\"></audio>");

        // Botón de agregar (abajo, a todo el ancho)
        out.println("<form class=\"catalogo__pie\" method=\"post\" action=\"catalogo\">");
        out.println("<input type=\"hidden\" name=\"codigo\" value=\"" + Html.esc(l.getCodigo()) + "\">");
        if (agregado) {
            out.println("<button type=\"button\" class=\"boton boton--secundario\" disabled>✔ Ya agregado</button>");
        } else {
            out.println("<button type=\"submit\" class=\"boton\" aria-label=\"Agregar " + titulo
                    + "\">＋ Agregar</button>");
        }
        out.println("</form>");
        out.println("</article>");
    }

    /** Clasifica la indignación para pintar el medidor: bajo, medio o alto (igual que en las vistas JSP). */
    private static String clasificar(int indignacion) {
        if (indignacion >= 8) {
            return "alto";
        }
        if (indignacion >= 4) {
            return "medio";
        }
        return "bajo";
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Catalogo.Entrada entrada = Catalogo.buscar(req.getParameter("codigo"));
        if (entrada == null) {
            resp.sendRedirect("catalogo?error=" + codificar("Ese audio no está en el catálogo."));
            return;
        }
        try {
            Contexto.servicio().agregar(entrada.lamento());
            resp.sendRedirect("catalogo?ok=" + codificar(entrada.lamento().getTitulo()));
        } catch (UnsupportedOperationException | IllegalStateException e) {
            resp.sendRedirect("catalogo?error=" + codificar("Todavía falta implementar una parte: " + e.getMessage()));
        } catch (IllegalArgumentException e) {
            resp.sendRedirect("catalogo?error=" + codificar(e.getMessage()));
        }
    }

    private static boolean yaAgregado(String codigo) {
        try {
            return Contexto.servicio().buscar(codigo) != null;
        } catch (RuntimeException e) {
            return false; // el modelo aún no está listo: se deja el botón activo
        }
    }

    private static String codificar(String texto) {
        return URLEncoder.encode(String.valueOf(texto), StandardCharsets.UTF_8);
    }
}
