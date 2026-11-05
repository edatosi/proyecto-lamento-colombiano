<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
    <%@ page
        import="java.net.URLEncoder, java.util.Locale, co.umariana.lamento.modelo.Lamento, co.umariana.lamento.web.Html"
        %>
        <%! private static final Locale ES=Locale.forLanguageTag("es-CO"); private static String clasificar(int
            indignacion) { if (indignacion>= 8) return "alto";
            if (indignacion >= 4) return "medio";
            return "bajo";
            }

            /** Codifica el nombre del archivo para usarlo en una URL (espacios, tildes, etc.). */
            private static String urlArchivo(String archivo) {
            return URLEncoder.encode(archivo, java.nio.charset.StandardCharsets.UTF_8).replace("+", "%20");
            }
            %>
            <% /* Atributos que pone el servlet Reproductor (el lamento puede ser null) */ Lamento lamento=(Lamento)
                request.getAttribute("lamento"); Object atributoTotal=request.getAttribute("total"); int
                total=(atributoTotal instanceof Number) ? ((Number) atributoTotal).intValue() : 0; String
                textoTotal=total + ((total==1) ? " lamento" : " lamentos" ) + " en la lista" ; String textoDuracion="" ;
                String rutaAudio="" ; if (lamento !=null) { textoDuracion=String.format(ES, "%.2f" ,
                lamento.getDuracionSeg()); rutaAudio="media/" + urlArchivo(lamento.getArchivo()); } %>
                <!DOCTYPE html>
                <html lang="es">

                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1">
                    <title>Reproductor · Lamentómetro</title>
                    <link rel="stylesheet" href="css/estilos.css">
                </head>

                <body class="app">
                    <header class="sitio__cabecera">
                        <div class="contenedor sitio__barra">
                            <a class="marca" href="index.html"><span aria-hidden="true">🎙️</span> Lamentómetro</a>
                            <nav class="menu" aria-label="Principal">
                                <a href="index.html">Inicio</a>
                                <a href="catalogo">Catálogo</a>
                                <a href="listar">Lamentos</a>
                                <a href="registrar">Registrar</a>
                                <a href="reproductor" aria-current="page">Reproductor</a>
                            </nav>
                        </div>
                    </header>

                    <main>
                        <% if (lamento==null) { %>
                            <section class="tarjeta vacio" aria-live="polite">
                                <span class="vacio__icono" aria-hidden="true">🔇</span>
                                <h1>No hay nada que reproducir</h1>
                                <p>Primero agregue lamentos desde el catálogo o registre uno a mano.</p>
                                <div class="acciones acciones--centro">
                                    <a class="boton" href="catalogo">Ir al catálogo</a>
                                    <a class="boton boton--secundario" href="registrar">Registrar uno</a>
                                </div>
                            </section>
                            <% } else { %>
                                <section class="reproductor tarjeta" id="reproductor" aria-labelledby="titulo-lamento">
                                    <div class="reproductor__ondas" aria-hidden="true">
                                        <span></span><span></span><span></span><span></span><span></span><span></span><span></span>
                                    </div>
                                    <h1 id="titulo-lamento">
                                        <%= Html.esc(lamento.getTitulo()) %>
                                    </h1>
                                    <div class="reproductor__datos">
                                        <span class="etiqueta">
                                            <%= Html.esc(lamento.getCategoria()) %>
                                        </span>
                                        <div class="medidor medidor--<%= clasificar(lamento.getIndignacion()) %>"
                                            role="img" aria-label="Indignación <%= lamento.getIndignacion() %> de 10">
                                            <span class="medidor__pista"><span class="medidor__barra"
                                                    style="--nivel:<%= lamento.getIndignacion() * 10 %>%"></span></span>
                                            <span class="medidor__valor">
                                                <%= lamento.getIndignacion() %>/10
                                            </span>
                                        </div>
                                        <span>
                                            <%= textoDuracion %> s
                                        </span>
                                    </div>
                                    <% String descripcion=lamento.getDescripcion(); %>
                                        <% if (descripcion !=null && !descripcion.isBlank()) { %>
                                            <p class="reproductor__descripcion">
                                                <%= Html.esc(descripcion) %>
                                            </p>
                                            <% } %>

                                                <audio id="audio" controls preload="auto" src="<%= rutaAudio %>">
                                                    Su navegador no soporta el elemento de audio.
                                                </audio>

                                                <div class="controles">
                                                    <a class="boton boton--secundario"
                                                        href="reproductor?accion=anterior" rel="prev">← Anterior</a>
                                                    <a class="boton" href="reproductor?accion=siguiente"
                                                        rel="next">Siguiente →</a>
                                                </div>

                                                <div class="reproductor__pie">
                                                    <span>
                                                        <%= Html.esc(lamento.getCodigo()) %> · <%= textoTotal %>
                                                    </span>
                                                    <label><input type="checkbox" id="seguir" checked> Pasar solo al
                                                        siguiente</label>
                                                </div>
                                </section>
                                <% } %>
                    </main>

                    <footer>Proyecto final · Estructuras de Datos I</footer>

                    <% if (lamento !=null) { %>
                        <script>
                            (function () {
                                var TOTAL = parseInt("<%= total %>", 10);
                                var CLAVE = 'lamentometro.seguir';
                                var audio = document.getElementById('audio');
                                var seguir = document.getElementById('seguir');
                                var tarjeta = document.getElementById('reproductor');

                                try { seguir.checked = localStorage.getItem(CLAVE) !== '0'; } catch (e) { /* sin almacenamiento */ }
                                seguir.addEventListener('change', function () {
                                    try { localStorage.setItem(CLAVE, seguir.checked ? '1' : '0'); } catch (e) { /* nada */ }
                                });

                                audio.addEventListener('play', function () { tarjeta.classList.add('reproduciendo'); });
                                audio.addEventListener('pause', function () { tarjeta.classList.remove('reproduciendo'); });
                                audio.addEventListener('ended', function () {
                                    tarjeta.classList.remove('reproduciendo');
                                    if (seguir.checked && TOTAL > 1) {
                                        window.location.href = 'reproductor?accion=siguiente&auto=1';
                                    }
                                });

                                // Si llegamos por "pasar solo", intenta reproducir (el navegador puede bloquearlo)
                                if (new URLSearchParams(window.location.search).get('auto') === '1' && seguir.checked) {
                                    var intento = audio.play();
                                    if (intento && intento.catch) { intento.catch(function () { /* requiere un clic */ }); }
                                }
                            })();
                        </script>
                        <% } %>
                </body>

                </html>