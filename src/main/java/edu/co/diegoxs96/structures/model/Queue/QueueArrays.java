package edu.co.diegoxs96.structures.model.Queue;

import edu.co.diegoxs96.structures.model.collection.Collection;
import edu.co.diegoxs96.structures.model.iterator.Iterator;

/**
 * Array-based circular queue implementation.
 *
 * @param <E> the type of elements in the queue.
 */
public class QueueArrays<E> extends AbstractQueue<E> {

    private Object[] array;
    private int front;
    private int rear;
    private int size;
    private int capacity;

    private static final int DEFAULT_CAPACITY = 10;

    public QueueArrays() {
        this(DEFAULT_CAPACITY);
    }

    public QueueArrays(int capacity) {
        this.capacity = capacity;
        this.array = new Object[capacity];
        this.front = 0;
        this.rear = -1;
        this.size = 0;
    }

    @Override
    public E insert(E element) {
        if (element == null || size == capacity)
            return null;

        rear = (rear + 1) % capacity;
        array[rear] = element;
        size++;
        return element;
    }

    @Override
    public E extract() {
        if (isEmpty())
            return null;

        @SuppressWarnings("unchecked")
        E element = (E) array[front];

        front = (front + 1) % capacity;
        size--;

        return element;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E peek() {
        if (isEmpty())
            return null;

        return (E) array[front];
    }

    @Override
    public boolean clear() {
        front = 0;
        rear = -1;
        size = 0;
        return true;
    }

    @Override
    public boolean contains(E element) {
        if (element == null)
            return false;

        for (int i = 0; i < size; i++) {
            if (element.equals(array[(front + i) % capacity]))
                return true;
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
        if (size <= 1)
            return true;

        for (int i = 0; i < size / 2; i++) {
            int a = (front + i) % capacity;
            int b = (front + size - 1 - i) % capacity;

            Object temp = array[a];
            array[a] = array[b];
            array[b] = temp;
        }
        return true;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {

            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < size;
            }

            @Override
            @SuppressWarnings("unchecked")
            public E next() {
                E element = (E) array[(front + index) % capacity];
                index++;
                return element;
            }
        };
    }
}