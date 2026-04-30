package edu.co.diegoxs96.structures.model.PriorityQueue;

import edu.co.diegoxs96.structures.model.collection.Collection;
import edu.co.diegoxs96.structures.model.iterator.Iterator;
import edu.co.diegoxs96.structures.model.list.AbstractList;
import edu.co.diegoxs96.structures.model.list.List;

/**
 * Concrete implementation of a priority queue backed by a List.
 *
 * @param <E> the type of elements in the priority queue.
 */
public class PriorityQueueClass<E> extends AbstractPriorityQueue<E> {

    private AbstractList<E> list;

    public PriorityQueueClass(AbstractList<E> list) {
        this.list = list;
    }

    @Override
    public boolean insert(E element) {
        return list.add(element);
    }

    @Override
    public boolean insert(int priority, E element) {
        return list.add(element);
    }

    @Override
    public E peek() {
        return list.peek();
    }

    @Override
    public E poll() {
        return list.poll();
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
}