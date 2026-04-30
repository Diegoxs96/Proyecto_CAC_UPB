package edu.co.diegoxs96.structures.model.PriorityQueue;

import edu.co.diegoxs96.structures.model.collection.Collection;

/**
 * Represents a priority queue data structure.
 *
 * @param <E> the type of elements in the priority queue.
 */
public interface PriorityQueue<E> extends Collection<E> {

    boolean insert(E element);

    boolean insert(int priority, E element);

    E peek();

    E poll();
}