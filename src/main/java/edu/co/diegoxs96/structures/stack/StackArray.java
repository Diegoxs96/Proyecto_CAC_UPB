package edu.co.diegoxs96.structures.stack;

import java.util.function.Function;

import edu.co.diegoxs96.structures.model.Stack.AbstractStack;
import edu.co.diegoxs96.structures.model.collection.Collection;
import edu.co.diegoxs96.structures.model.iterator.Iterator;

/**
 * Stack implementation backed by an array (LIFO).
 * Elements are pushed and popped from the top of the array.
 *
 * @param <E> the type of elements in the stack.
 *
 * @author julian benitorevollo bernal
 * @version 1.0.20240219
 */
public class StackArray<E> extends AbstractStack<E> {

    private Object[] array;
    private int top;
    private int capacity;

    private static final int DEFAULT_CAPACITY = 10;

    public StackArray() {
        this(DEFAULT_CAPACITY);
    }

    public StackArray(int capacity) {
        this.capacity = capacity;
        this.array = new Object[capacity];
        this.top = -1;
    }

    @Override
    public E peek() {
        if (isEmpty())
            return null;
        @SuppressWarnings("unchecked")
        E element = (E) array[top];
        return element;
    }

    @Override
    public E pop() {
        if (isEmpty())
            return null;
        @SuppressWarnings("unchecked")
        E element = (E) array[top];
        array[top] = null;
        top--;
        return element;
    }

    @Override
    public boolean push(E element) {
        if (element == null || top == capacity - 1)
            return false;
        array[++top] = element;
        return true;
    }

    @Override
    public boolean clear() {
        for (int i = 0; i <= top; i++) {
            array[i] = null;
        }
        top = -1;
        return true;
    }

    @Override
    public boolean contains(E element) {
        if (element == null)
            return false;
        for (int i = 0; i <= top; i++) {
            if (element.equals(array[i]))
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
        return top == -1;
    }

    @Override
    public boolean reverse() {
        if (size() <= 1)
            return true;
        int left = 0;
        int right = top;
        while (left < right) {
            Object temp = array[left];
            array[left] = array[right];
            array[right] = temp;
            left++;
            right--;
        }
        return true;
    }

    @Override
    public int size() {
        return top + 1;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int index = top;

            @Override
            public boolean hasNext() {
                return index >= 0;
            }

            @Override
            @SuppressWarnings("unchecked")
            public E next() {
                return (E) array[index--];
            }
        };
    }

    @Override
    public void forEach(Function<E, Void> action) {
        if (action == null)
            return;
        for (int i = top; i >= 0; i--) {
            @SuppressWarnings("unchecked")
            E element = (E) array[i];
            action.apply(element);
        }
    }
}