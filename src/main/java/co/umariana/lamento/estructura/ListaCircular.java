package co.umariana.lamento.estructura;

import java.util.Iterator;

/**
 * Lista enlazada CIRCULAR del equipo (tarea de ESTRUCTURA).
 *
 * <p>El último nodo apunta de nuevo al primero. Implemente la lista con sus propios nodos: no se
 * permite usar {@code ArrayList}, {@code LinkedList} ni ninguna otra colección de {@code java.util}
 * para guardar los elementos (las pruebas lo revisan).
 *
 * <p>Comportamiento especial por ser circular: el cursor NUNCA se detiene. {@link #avanzar()} desde
 * el último elemento vuelve al primero, y {@link #retroceder()} desde el primero va al último.
 * Ambos devuelven {@code true} mientras la lista no esté vacía.
 *
 * <p>El iterador recorre los elementos UNA sola vez, desde el primero hasta el último (¡sin quedar
 * dando vueltas para siempre!).
 */
public class ListaCircular<T> implements Lista<T> {

    // TODO (ESTRUCTURA): declarar la clase Nodo y los atributos necesarios (cabeza, tamaño, cursor...).

    @Override
    public void agregar(T elemento) {
        throw new UnsupportedOperationException("TODO: agregar()");
    }

    @Override
    public void insertar(int indice, T elemento) {
        throw new UnsupportedOperationException("TODO: insertar()");
    }

    @Override
    public T obtener(int indice) {
        throw new UnsupportedOperationException("TODO: obtener()");
    }

    @Override
    public T eliminar(int indice) {
        throw new UnsupportedOperationException("TODO: eliminar()");
    }

    @Override
    public int tamano() {
        throw new UnsupportedOperationException("TODO: tamano()");
    }

    @Override
    public boolean estaVacia() {
        throw new UnsupportedOperationException("TODO: estaVacia()");
    }

    @Override
    public void vaciar() {
        throw new UnsupportedOperationException("TODO: vaciar()");
    }

    @Override
    public T actual() {
        throw new UnsupportedOperationException("TODO: actual()");
    }

    @Override
    public boolean avanzar() {
        throw new UnsupportedOperationException("TODO: avanzar()");
    }

    @Override
    public boolean retroceder() {
        throw new UnsupportedOperationException("TODO: retroceder()");
    }

    @Override
    public Iterator<T> iterator() {
        throw new UnsupportedOperationException("TODO: iterator()");
    }
}
