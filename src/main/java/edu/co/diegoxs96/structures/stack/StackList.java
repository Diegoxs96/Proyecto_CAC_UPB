package edu.co.diegoxs96.structures.stack;

import java.util.function.Function;

import edu.co.diegoxs96.structures.linkedlist.singly.LinkedList;
import edu.co.diegoxs96.structures.model.Stack.AbstractStack;
import edu.co.diegoxs96.structures.model.collection.Collection;
import edu.co.diegoxs96.structures.model.iterator.Iterator;

/**
 * Stack implementation backed by a singly linked list (LIFO).
 * Elements are pushed and popped from the head of the list.
 *
 * @param <E> the type of elements in the stack.
 *
 * @author julian benitorevollo bernal
 * @version 1.0.20240219
 */
public class StackList<E> extends AbstractStack<E> {

    private LinkedList<E> list;

    public StackList() {
        this.list = new LinkedList<>();
    }

    @Override
    public E peek() {
        return list.peek();
    }

    @Override
    public E pop() {
        return list.poll();
    }

    @Override
    public boolean push(E element) {
        return list.addFirst(element);
    }

    @Override
    public boolean clear() {
        return list.clear();
    }

    @Override
    public boolean contains(E element) {
        return list.contains(element);
    }

    @Override
    public boolean contains(E[] array) {
        return list.contains(array);
    }

    @Override
    public boolean contains(Collection<E> collection) {
        return list.contains(collection);
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public boolean reverse() {
        return list.reverse();
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public Iterator<E> iterator() {
        return list.iterator();
    }

    @Override
    public void forEach(Function<E, Void> action) {
        list.forEach(action);
    }
}
