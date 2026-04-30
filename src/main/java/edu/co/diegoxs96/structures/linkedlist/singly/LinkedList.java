package edu.co.diegoxs96.structures.linkedlist.singly;

import java.util.function.Predicate;
import java.util.function.ToIntFunction;

import edu.co.diegoxs96.structures.linkedlist.node.singly.LinkedNode;
import edu.co.diegoxs96.structures.model.collection.Collection;
import edu.co.diegoxs96.structures.model.iterator.Iterator;
import edu.co.diegoxs96.structures.model.list.AbstractList;
import edu.co.diegoxs96.structures.model.list.List;

public class LinkedList<E> extends AbstractList<E> {

  private LinkedNode<E> head;
  private LinkedNode<E> tail;
  private int size;

  public LinkedList() {
    this.head = null;
    this.tail = null;
    this.size = 0;
  }

  @Override
  public boolean isEmpty() {
    return size == 0;
  }

  @Override
  public int size() {
    return size;
  }

  @Override
  public boolean clear() {
    head = null;
    tail = null;
    size = 0;
    return true;
  }

  @Override
  public void forEach(java.util.function.Function<E, Void> action) {
    LinkedNode<E> current = head;
    while (current != null) {
      action.apply(current.get());
      current = current.getNext();
    }
  }

  @Override
  public edu.co.diegoxs96.structures.model.iterator.Iterator<E> iterator() {
    return new edu.co.diegoxs96.structures.model.iterator.Iterator<E>() {

      private LinkedNode<E> current = head;

      @Override
      public boolean hasNext() {
        return current != null;
      }

      @Override
      public E next() {
        E value = current.get();
        current = current.getNext();
        return value;
      }
    };
  }

  @Override
  public boolean contains(E element) {
    LinkedNode<E> current = head;
    while (current != null) {
      if ((element == null && current.get() == null) ||
          (element != null && element.equals(current.get())))
        return true;
      current = current.getNext();
    }
    return false;
  }

  @Override
  public boolean contains(E[] array) {
    if (array == null)
      return false;
    for (E e : array)
      if (!contains(e))
        return false;
    return true;
  }

  @Override
  public boolean contains(edu.co.diegoxs96.structures.model.collection.Collection<E> collection) {
    if (collection == null)
      return false;
    edu.co.diegoxs96.structures.model.iterator.Iterator<E> it = collection.iterator();
    while (it.hasNext())
      if (!contains(it.next()))
        return false;
    return true;
  }

  public LinkedNode<E> getHead() {
    return head;
  }

  @Override
  public boolean reverse() {
    if (size < 2)
      return false;

    LinkedNode<E> prev = null;
    LinkedNode<E> current = head;
    tail = head;

    while (current != null) {
      LinkedNode<E> next = current.getNext();
      current.setNext(prev);
      prev = current;
      current = next;
    }

    head = prev;
    return true;
  }

  @Override
  public boolean add(E element) {
    LinkedNode<E> node = new LinkedNode<>(element);

    if (isEmpty()) {
      head = node;
      tail = node;
    } else {
      tail.setNext(node);
      tail = node;
    }

    size++;
    return true;
  }

  @Override
  public boolean add(E[] array) {
    if (array == null)
      return false;
    for (E e : array)
      add(e);
    return true;
  }

  @Override
  public boolean add(edu.co.diegoxs96.structures.model.collection.Collection<E> collection) {
    if (collection == null)
      return false;
    edu.co.diegoxs96.structures.model.iterator.Iterator<E> it = collection.iterator();
    while (it.hasNext())
      add(it.next());
    return true;
  }

  @Override
  public boolean addFirst(E element) {
    LinkedNode<E> node = new LinkedNode<>(element);

    if (isEmpty()) {
      head = node;
      tail = node;
    } else {
      node.setNext(head);
      head = node;
    }

    size++;
    return true;
  }

  @Override
  public boolean addFirst(E[] array) {
    if (array == null)
      return false;
    for (int i = array.length - 1; i >= 0; i--)
      addFirst(array[i]);
    return true;
  }

  @Override
  public boolean addFirst(edu.co.diegoxs96.structures.model.collection.Collection<E> collection) {
    if (collection == null)
      return false;

    LinkedList<E> temp = new LinkedList<>();
    edu.co.diegoxs96.structures.model.iterator.Iterator<E> it = collection.iterator();

    while (it.hasNext())
      temp.add(it.next());

    E[] arr = temp.toArray();
    for (int i = arr.length - 1; i >= 0; i--)
      addFirst(arr[i]);

    return true;
  }

  @Override
  public E peek() {
    return isEmpty() ? null : head.get();
  }

  @Override
  public E peekLast() {
    return isEmpty() ? null : tail.get();
  }

  @Override
  @SuppressWarnings("unchecked")
  public E[] peekArray(int n) {
    if (isEmpty() || n <= 0)
      return (E[]) new Object[0];

    int limit = Math.min(n, size);
    Object[] result = new Object[limit];

    LinkedNode<E> current = head;
    for (int i = 0; i < limit; i++) {
      result[i] = current.get();
      current = current.getNext();
    }

    return (E[]) result;
  }

  @Override
  @SuppressWarnings("unchecked")
  public E[] peekLastArray(int n) {
    if (isEmpty() || n <= 0)
      return (E[]) new Object[0];

    int limit = Math.min(n, size);
    Object[] result = new Object[limit];

    int start = size - limit;
    LinkedNode<E> current = head;

    for (int i = 0; i < start; i++)
      current = current.getNext();

    for (int i = 0; i < limit; i++) {
      result[i] = current.get();
      current = current.getNext();
    }

    return (E[]) result;
  }

  @Override
  public List<E> peekCollection(int n) {
    LinkedList<E> list = new LinkedList<>();
    list.add(peekArray(n));
    return list;
  }

  @Override
  public List<E> peekLastCollection(int n) {
    LinkedList<E> list = new LinkedList<>();
    list.add(peekLastArray(n));
    return list;
  }

  @Override
  public E poll() {
    if (isEmpty())
      return null;

    E value = head.get();
    head = head.getNext();
    size--;

    if (size == 0)
      tail = null;

    return value;
  }

  @Override
  public E pollLast() {
    if (isEmpty())
      return null;

    if (size == 1)
      return poll();

    LinkedNode<E> current = head;
    while (current.getNext() != tail)
      current = current.getNext();

    E value = tail.get();
    current.setNext(null);
    tail = current;
    size--;

    return value;
  }

  @Override
  @SuppressWarnings("unchecked")
  public E[] pollArray(int n) {
    if (isEmpty() || n <= 0)
      return (E[]) new Object[0];

    int limit = Math.min(n, size);
    Object[] result = new Object[limit];

    for (int i = 0; i < limit; i++)
      result[i] = poll();

    return (E[]) result;
  }

  @Override
  @SuppressWarnings("unchecked")
  public E[] pollLastArray(int n) {
    if (isEmpty() || n <= 0)
      return (E[]) new Object[0];

    int limit = Math.min(n, size);
    Object[] result = new Object[limit];

    for (int i = limit - 1; i >= 0; i--)
      result[i] = pollLast();

    return (E[]) result;
  }

  @Override
  public List<E> pollCollection(int n) {
    LinkedList<E> list = new LinkedList<>();
    list.add(pollArray(n));
    return list;
  }

  @Override
  public List<E> pollLastCollection(int n) {
    LinkedList<E> list = new LinkedList<>();
    list.add(pollLastArray(n));
    return list;
  }

  @Override
  public boolean remove(E element) {
    if (isEmpty())
      return false;

    if ((element == null && head.get() == null) ||
        (element != null && element.equals(head.get()))) {
      poll();
      return true;
    }

    LinkedNode<E> current = head;

    while (current.getNext() != null) {
      if ((element == null && current.getNext().get() == null) ||
          (element != null && element.equals(current.getNext().get()))) {

        if (current.getNext() == tail)
          tail = current;

        current.setNext(current.getNext().getNext());
        size--;
        return true;
      }
      current = current.getNext();
    }

    return false;
  }

  @Override
  public boolean remove(E[] array) {
    if (array == null)
      return false;
    boolean removed = false;
    for (E e : array)
      if (remove(e))
        removed = true;
    return removed;
  }

  @Override
  public boolean remove(edu.co.diegoxs96.structures.model.collection.Collection<E> collection) {
    if (collection == null)
      return false;
    boolean removed = false;
    edu.co.diegoxs96.structures.model.iterator.Iterator<E> it = collection.iterator();
    while (it.hasNext())
      if (remove(it.next()))
        removed = true;
    return removed;
  }

  public boolean removeAll(E element) {
    boolean removed = false;
    while (remove(element))
      removed = true;
    return removed;
  }

  public boolean removeAll(E[] array) {
    if (array == null)
      return false;
    boolean removed = false;
    for (E e : array)
      if (removeAll(e))
        removed = true;
    return removed;
  }

  public boolean removeAll(edu.co.diegoxs96.structures.model.collection.Collection<E> collection) {
    if (collection == null)
      return false;
    boolean removed = false;
    edu.co.diegoxs96.structures.model.iterator.Iterator<E> it = collection.iterator();
    while (it.hasNext())
      if (removeAll(it.next()))
        removed = true;
    return removed;
  }

  public boolean retain(E element) {
    boolean modified = false;

    LinkedNode<E> current = head;
    LinkedNode<E> prev = null;

    while (current != null) {
      if ((element == null && current.get() != null) ||
          (element != null && !element.equals(current.get()))) {

        if (prev == null) {
          head = current.getNext();
        } else {
          prev.setNext(current.getNext());
        }

        if (current == tail)
          tail = prev;

        size--;
        modified = true;
      } else {
        prev = current;
      }

      current = current.getNext();
    }

    return modified;
  }

  @Override
  public boolean retain(E[] array) {
    if (array == null)
      return false;
    boolean modified = false;

    LinkedNode<E> current = head;
    while (current != null) {
      if (!contains(array, current.get())) {
        remove(current.get());
        modified = true;
      }
      current = current.getNext();
    }

    return modified;
  }

  private boolean contains(E[] array, E element) {
    for (E e : array) {
      if ((e == null && element == null) ||
          (e != null && e.equals(element)))
        return true;
    }
    return false;
  }

  @Override
  public boolean retain(edu.co.diegoxs96.structures.model.collection.Collection<E> collection) {
    if (collection == null)
      return false;
    boolean modified = false;

    LinkedNode<E> current = head;
    while (current != null) {
      if (!collection.contains(current.get())) {
        remove(current.get());
        modified = true;
      }
      current = current.getNext();
    }

    return modified;
  }

  public boolean replace(E oldElement, E newElement, int n) {
    if (isEmpty() || n <= 0)
      return false;

    LinkedNode<E> current = head;
    int count = 0;

    while (current != null) {
      if ((oldElement == null && current.get() == null) ||
          (oldElement != null && oldElement.equals(current.get()))) {

        current.set(newElement);
        count++;

        if (count == n)
          return true;
      }
      current = current.getNext();
    }

    return false;
  }

  @Override
  @SuppressWarnings("unchecked")
  public E[] toArray() {
    Object[] array = new Object[size];
    LinkedNode<E> current = head;
    int index = 0;

    while (current != null) {
      array[index++] = current.get();
      current = current.getNext();
    }

    return (E[]) array;
  }

  public List<E> subList(int fromIndex, int toIndex) {
    LinkedList<E> sub = new LinkedList<>();

    if (fromIndex < 0 || toIndex > size || fromIndex >= toIndex)
      return sub;

    LinkedNode<E> current = head;
    int index = 0;

    while (current != null) {
      if (index >= fromIndex && index < toIndex)
        sub.add(current.get());

      if (index >= toIndex)
        break;

      current = current.getNext();
      index++;
    }

    return sub;
  }

  @SuppressWarnings("unchecked")
  public boolean sort() {
    if (size < 2)
      return false;

    Object[] array = toArray();

    java.util.Arrays.sort(array);

    clear();

    for (Object obj : array)
      add((E) obj);

    return true;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!(obj instanceof LinkedList))
      return false;

    LinkedList<?> other = (LinkedList<?>) obj;

    if (this.size != other.size)
      return false;

    LinkedNode<E> current1 = this.head;
    LinkedNode<?> current2 = other.head;

    while (current1 != null) {
      Object val1 = current1.get();
      Object val2 = current2.get();

      if (val1 == null) {
        if (val2 != null)
          return false;
      } else {
        if (!val1.equals(val2))
          return false;
      }

      current1 = current1.getNext();
      current2 = current2.getNext();
    }

    return true;
  }

  @Override
  @SuppressWarnings("unchecked")
  public LinkedList<E> clone() {
    LinkedList<E> copy = new LinkedList<>();
    LinkedNode<E> current = head;

    while (current != null) {
      copy.add(current.get());
      current = current.getNext();
    }

    return copy;
  }

  @Override
  public boolean set(E index, E element) {
    LinkedNode<E> current = head;

    while (current != null) {
      if ((index == null && current.get() == null) ||
          (index != null && index.equals(current.get()))) {

        current.set(element);
        return true;
      }
      current = current.getNext();
    }

    return false;
  }

  @Override
  public boolean replace(E element, E newElement, java.util.function.Predicate<E> comparator) {
    LinkedNode<E> current = head;
    boolean replaced = false;

    while (current != null) {
      if (comparator.test(current.get())) {
        current.set(newElement);
        replaced = true;
      }
      current = current.getNext();
    }

    return replaced;
  }

  @Override
  public boolean replace(E[] array, E[] newArray, java.util.function.Predicate<E> comparator) {
    if (array == null || newArray == null)
      return false;

    boolean replaced = false;

    for (int i = 0; i < array.length && i < newArray.length; i++) {
      if (replace(array[i], newArray[i], comparator)) {
        replaced = true;
      }
    }

    return replaced;
  }

  @Override
  public boolean replace(
      edu.co.diegoxs96.structures.model.collection.Collection<E> collection,
      edu.co.diegoxs96.structures.model.collection.Collection<E> newCollection,
      java.util.function.Predicate<E> comparator) {

    if (collection == null || newCollection == null)
      return false;

    boolean replaced = false;

    edu.co.diegoxs96.structures.model.iterator.Iterator<E> it1 = collection.iterator();
    edu.co.diegoxs96.structures.model.iterator.Iterator<E> it2 = newCollection.iterator();

    while (it1.hasNext() && it2.hasNext()) {
      if (replace(it1.next(), it2.next(), comparator)) {
        replaced = true;
      }
    }

    return replaced;
  }

  @Override
  public List<E> subList(E from, E to) {
    LinkedList<E> subList = new LinkedList<>();
    LinkedNode<E> current = head;
    boolean adding = false;

    while (current != null) {

      if ((from == null && current.get() == null) ||
          (from != null && from.equals(current.get()))) {
        adding = true;
      }

      if (adding) {
        subList.add(current.get());
      }

      if ((to == null && current.get() == null) ||
          (to != null && to.equals(current.get()))) {
        break;
      }

      current = current.getNext();
    }

    return subList;
  }

  @Override
  public boolean remove(java.util.function.Predicate<E> filter) {

    if (filter == null)
      return false;

    boolean removed = false;

    LinkedNode<E> current = head;
    LinkedNode<E> previous = null;

    while (current != null) {

      if (filter.test(current.get())) {

        if (previous == null) {
          head = current.getNext();
        } else {
          previous.setNext(current.getNext());
        }

        if (current == tail) {
          tail = previous;
        }

        size--;
        removed = true;
      } else {
        previous = current;
      }

      current = current.getNext();
    }

    return removed;
  }

  @Override
  public boolean sort(java.util.function.ToIntFunction<E> toInt) {

    if (size < 2 || toInt == null)
      return false;

    boolean swapped;

    do {
      swapped = false;
      LinkedNode<E> current = head;

      while (current != null && current.getNext() != null) {

        int value1 = toInt.applyAsInt(current.get());
        int value2 = toInt.applyAsInt(current.getNext().get());

        if (value1 > value2) {

          E temp = current.get();
          current.set(current.getNext().get());
          current.getNext().set(temp);

          swapped = true;
        }

        current = current.getNext();
      }

    } while (swapped);

    return true;
  }

}
