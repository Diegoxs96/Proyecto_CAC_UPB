package edu.co.diegoxs96.structures.queue;

import edu.co.diegoxs96.structures.model.Queue.AbstractQueue;
import edu.co.diegoxs96.structures.model.collection.Collection;
import edu.co.diegoxs96.structures.model.iterator.Iterator;

/**
 * Linked-list based Queue implementation.
 *
 * @param <E> the type of elements in the queue.
 */
public class QueueList<E> extends AbstractQueue<E> {

    private static class Node<E> {
        E data;
        Node<E> next;

        Node(E data) {
            this.data = data;
        }
    }

    private Node<E> front;
    private Node<E> rear;
    private int size;

    @Override
    public E insert(E element) {
        if (element == null)
            return null;

        Node<E> newNode = new Node<>(element);

        if (isEmpty()) {
            front = rear = newNode;
        } else {
            rear.next = newNode;
            rear = newNode;
        }

        size++;
        return element;
    }

    @Override
    public E extract() {
        if (isEmpty())
            return null;

        E element = front.data;
        front = front.next;
        size--;

        if (front == null)
            rear = null;

        return element;
    }

    @Override
    public E peek() {
        return isEmpty() ? null : front.data;
    }

    @Override
    public boolean clear() {
        front = rear = null;
        size = 0;
        return true;
    }

    @Override
    public boolean contains(E element) {
        if (element == null)
            return false;

        Node<E> current = front;
        while (current != null) {
            if (element.equals(current.data))
                return true;
            current = current.next;
        }
        return false;
    }

    @Override
    public boolean contains(E[] arr) {
        if (arr == null)
            return false;

        for (E e : arr) {
            if (!contains(e))
                return false;
        }
        return true;
    }

    @Override
    public boolean contains(Collection<E> collection) {
        if (collection == null)
            return false;

        Iterator<E> it = collection.iterator();
        while (it.hasNext()) {
            if (!contains(it.next()))
                return false;
        }
        return true;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean reverse() {
        Node<E> prev = null;
        Node<E> current = front;
        rear = front;

        while (current != null) {
            Node<E> next = current.next;
            current.next = prev;
            prev = current;
            current = next;
        }

        front = prev;
        return true;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {

            private Node<E> current = front;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                E element = current.data;
                current = current.next;
                return element;
            }
        };
    }
}