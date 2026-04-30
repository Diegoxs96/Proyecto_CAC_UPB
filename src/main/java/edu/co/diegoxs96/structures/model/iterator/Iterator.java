package edu.co.diegoxs96.structures.model.iterator;

/**
 * The IteratorInterface interface represents an iterator over a collection of
 * elements.
 * It provides methods to check if the iterator has more elements and retrieve
 * the next element.
 *
 * @param <E> the type of elements in the iterator.
 * 
 * @author juelian benitorevollo bernal
 * @version 1.0.20231115
 */
public interface Iterator<E> {
  /**
   * Determines if the iterator has more elements.
   *
   * @return 'true' if the iterator has more elements, otherwise 'false'.
   */
  boolean hasNext();

  /**
   * Gets the next element in the iteration.
   *
   * @return the next element in the iteration.
   */
  E next();
}
