package edu.co.diegoxs96.structures.model.list;

import java.util.function.Predicate;
import java.util.function.ToIntFunction;

import edu.co.diegoxs96.structures.model.collection.Collection;

/**
 * Represents a generic list data structure.
 * 
 * @param <E> the type of elements in the list.
 * 
 * @author julian benitorevollo bernal
 * @version 1.0.20231125
 */
public interface List<E> {
  /**
   * Appends the specified element to the end of this list.
   * 
   * @param element the element to be appended to this list.
   * @return 'true' if the element was added successfully, otherwise 'false'.
   */
  boolean add(E element);

  /**
   * Appends all the objects in an array to the end of the list.
   * 
   * @param array the array whose elements are to be added to the list.
   * @return 'true' if the array was added successfully, otherwise 'false'.
   */
  boolean add(E[] array);

  /**
   * Appends all of the elements in the specified collection to the end of this
   * list.
   * 
   * @param collection the collection whose elements are to be added to the list.
   * @return 'true' if the element was added successfully, otherwise 'false'.
   */
  boolean add(Collection<E> collection);

  /**
   * Appends the specified element at the beginning of this list.
   * 
   * @param element the element to be appended to this list.
   * @return 'true' if the element was added successfully, otherwise 'false'.
   */
  boolean addFirst(E element);

  /**
   * Appends all of the elements in the specified array at the beginning of this
   * list, in the order that they are returned by the specified collection's
   * iterator.
   * 
   * @param element the element to be appended to this list.
   * @return 'true' if the collection was added successfully, otherwise 'false'.
   */
  boolean addFirst(E[] array);

  /**
   * Appends all of the elements in the specified collection at the beginning of
   * this list, in the order that they are returned by the specified collection's
   * iterator.
   * 
   * @param element the element to be appended to this list.
   * @return 'true' if the collection was added successfully, otherwise 'false'.
   */
  boolean addFirst(Collection<E> collection);

  /**
   * Retrieves, but does not remove, the head (first element) of this list.
   * 
   * @return the element in the head of this list, or 'null' if this list is
   *         empty.
   */
  E peek();

  /**
   * Retrieves, but does not remove, the last element of this list.
   * 
   * @return the element in the last of this list, or 'null' if this list is
   *         empty.
   */
  E peekLast();

  /**
   * Retrieves, but does not remove, the first n elements of the list.
   * 
   * @param n the number of elements to get.
   * @return an array containing the first n elements of the list.
   */
  E[] peekArray(int n);

  /**
   * Retrieves, but does not remove, the last n elements of the list.
   * 
   * @param n the number of elements to get.
   * @return an array containing the last n elements of the list.
   */
  E[] peekLastArray(int n);

  /**
   * Retrieves, but does not remove, the first n elements of the list.
   * 
   * @param n the number of elements to get.
   * @return a collection containing the first n elements of the list.
   */
  List<E> peekCollection(int n);

  /**
   * Retrieves, but does not remove, the last n elements of the list.
   * 
   * @param n the number of elements to get.
   * @return a collection containing the last n elements of the list.
   */
  List<E> peekLastCollection(int n);

  /**
   * Retrieves and removes the head (first element) of this list.
   * 
   * @return the element in the head of this list, or 'null' if this list is
   *         empty.
   */
  E poll();

  /**
   * Retrieves and removes the last element of this list.
   * 
   * @return the element in the last of this list, or 'null' if this list is
   *         empty.
   */
  E pollLast();

  /**
   * Retrieves and removes the first n elements of the list.
   * 
   * @param n the number of elements to get.
   * @return an array containing the first n elements of the list.
   */
  E[] pollArray(int n);

  /**
   * Retrieves and removes the last n elements of the list.
   * 
   * @param n the number of elements to get.
   * @return an array containing the first n elements of the list.
   */
  E[] pollLastArray(int n);

  /**
   * Retrieves and removes the first n elements of the list.
   * 
   * @param n the number of elements to get.
   * @return a collection containing the first n elements of the list.
   */
  List<E> pollCollection(int n);

  /**
   * Retrieves and removes the last n elements of the list.
   * 
   * @param n the number of elements to get.
   * @return a collection containing the last n elements of the list.
   */
  List<E> pollLastCollection(int n);

  /**
   * Removes the first occurrence of the specified element from this list, if it
   * is present.
   * 
   * @param element the element to be removed from this list, if present.
   * @return 'true' if the element was removed successfully, otherwise 'false'.
   */
  boolean remove(E element);

  /**
   * Removes from this list all of its elements that are contained in the
   * specified collection.
   * 
   * @param array the array containing elements to be removed from this list.
   * @return 'true' if the collection elements was removed successfully,
   *         otherwise 'false'.
   */
  boolean remove(E[] array);

  /**
   * Removes from this list all of its elements that are contained in the
   * specified collection.
   * 
   * @param collection the collection containing elements to be removed from this
   *                   list.
   * @return 'true' if the collection elements was removed successfully,
   *         otherwise 'false'.
   */
  boolean remove(Collection<E> collection);

  /**
   * Removes all of the elements of this list that satisfy the given predicate.
   * Errors or runtime exceptions thrown during iteration or by the predicate are
   * relayed to the caller.
   * 
   * @param filter a predicate which returns 'true' for elements to be removed.
   * @return 'true' if the list was removed successfully, otherwise 'false'.
   */
  boolean remove(Predicate<E> filter);

  /**
   * Replaces each element of this list with the result of applying the function
   * to that element.
   * 
   * @param element    the element to be replaced in this list.
   * @param newElement the element to be added to this list.
   * @param comparator the function to apply to each element.
   * @return 'true' if the list was replaced successfully, otherwise 'false'.
   */
  boolean replace(E element, E newElement, Predicate<E> comparator);

  /**
   * Replaces each element of this list with the result of applying the function
   * to that element.
   * 
   * @param array      the array containing elements to be replaced in this list.
   * @param newArray   the array containing elements to be added to this list.
   * @param comparator the function to apply to each element.
   * @return 'true' if the list was replaced successfully, otherwise 'false'.
   */
  boolean replace(E[] array, E[] newArray, Predicate<E> comparator);

  /**
   * Replaces each element of this list with the result of applying the function
   * to that element.
   * 
   * @param collection    the collection containing elements to be replaced in
   *                      this
   *                      list.
   * @param newCollection the collection containing elements to be added to this
   *                      list.
   * @param comparator    the function to apply to each element.
   * @return 'true' if the list was replaced successfully, otherwise 'false'.
   */
  boolean replace(Collection<E> collection, Collection<E> newCollection, Predicate<E> comparator);

  /**
   * Retains only the elements in this list that are contained in the specified
   * collection.
   * 
   * @param array the array containing elements to be retained in this list.
   * @return 'true' if the list was retained successfully, otherwise 'false'.
   */
  boolean retain(E[] array);

  /**
   * Retains only the elements in this list that are contained in the specified
   * collection.
   * 
   * @param collection the collection containing elements to be retained in this
   *                   list.
   * @return 'true' if the list was retained successfully, otherwise 'false'.
   */
  boolean retain(Collection<E> collection);

  /**
   * Replaces the specified element by a new element in this list. Only the first
   * occurrence is replaced.
   * 
   * @param index   the element to be replaced in this list.
   * @param element the element to be added to this list.
   * @return 'true' if the element was replaced successfully, otherwise 'false'.
   */
  boolean set(E index, E element);

  /**
   * Sorts this list according to the order induced by the specified Comparator.
   * 
   * @param toInt the Comparator used to compare list elements.
   * @return 'true' if the list was sorted successfully, otherwise 'false'.
   */
  boolean sort(ToIntFunction<E> toInt);

  /**
   * return a sub list of this list between the specified "from" and "to".
   * 
   * @param from the initial element of the sub list, inclusive.
   * @param to   the final element of the sub list, exclusive.
   * @return a sub list of this list between the specified "from", inclusive, and
   *         "to", exclusive.
   */
  List<E> subList(E from, E to);

  /**
   * return an array containing all of the elements in this list.
   * 
   * @return an array containing all of the elements in this list.
   */
  E[] toArray();
}
