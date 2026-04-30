package edu.co.diegoxs96.structures.linkedlist.circular;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

import edu.co.diegoxs96.structures.linkedlist.node.circular.CircularLinkedNode;
import edu.co.diegoxs96.structures.linkedlist.node.singly.LinkedNode;
import edu.co.diegoxs96.structures.model.collection.Collection;
import edu.co.diegoxs96.structures.model.iterator.Iterator;
import edu.co.diegoxs96.structures.model.list.AbstractList;
import edu.co.diegoxs96.structures.model.list.List;

public class CircularLinkedList<E> extends AbstractList<E> {

  private transient CircularLinkedNode<E> head;
  private transient CircularLinkedNode<E> tail;
  private transient CircularLinkedNode<E> inode;
  private transient int size;

  public CircularLinkedList() {
    head = tail = inode = null;
    size = 0;
  }
  // en esta parte se da el orden de el head como primer elemento y tail como
  // ultimo el inode para recrrer y el null
  // para verificar q no hay nada mas y al cerrar la lista el contador vuelve a 0
  // y cada vez que se agrege el numero aument con size++

  public CircularLinkedList(E element) {
    this.head = tail = inode = new CircularLinkedNode<>(element);
    this.size = 1;
  }

  public boolean add(E element) {
    try {
      if (isEmpty()) {
        CircularLinkedNode<E> node = new CircularLinkedNode<>(element);
        this.head = this.tail = this.inode = node;
      } else {
        CircularLinkedNode<E> node = new CircularLinkedNode<>(element);
        this.tail.setNext(node);
        this.tail = node;
        tail.setNext(head);
      }
      this.size++;
      return true;
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return false;
    }
  }

  @Override
  public boolean add(E[] array) {
    if (array == null) {
      return false;
    }
    boolean allAdded = true;
    for (E element : array) {
      if (!this.add(element)) {
        allAdded = false;
      }
    }
    return allAdded;
  }

  @Override
  public boolean add(Collection<E> collection) {
    if (collection == null) {
      return false;
    }
    Iterator<E> iterator = collection.iterator();
    while (iterator.hasNext()) {
      this.add(iterator.next());
    }
    return true;
  }

  @Override
  public boolean addFirst(E element) {
    try {
      CircularLinkedNode<E> node = new CircularLinkedNode<>(element);

      if (isEmpty()) {
        head = tail = node;
        tail.setNext(head);
      } else {
        node.setNext(head);
        head = node;
      }

      size++;
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public boolean addFirst(E[] array) {
    if (array == null) {
      return false;
    }

    for (int i = array.length - 1; i >= 0; i--) {
      if (!this.addFirst(array[i])) {
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean addFirst(Collection<E> collection) {
    if (collection == null) {
      return false;
    }

    int count = 0;
    Iterator<E> counterIter = collection.iterator();
    while (counterIter.hasNext()) {
      counterIter.next();
      count++;
    }

    Object[] tempArray = new Object[count];
    int i = 0;
    Iterator<E> iterator = collection.iterator();
    while (iterator.hasNext()) {
      tempArray[i++] = iterator.next();
    }

    for (int j = tempArray.length - 1; j >= 0; j--) {
      if (!this.addFirst((E) tempArray[j])) {
        return false;
      }
    }
    return true;
  }

  @Override
  public E peek() {
    if (isEmpty()) {
      return null;
    }
    return this.head.get();
  }

  @Override
  public E peekLast() {
    if (isEmpty()) {
      return null;
    }
    return this.tail.get();
  }

  @Override
  public E[] peekArray(int n) {
    if (isEmpty() || n <= 0) {
      return (E[]) new Object[0];
    }

    int arraySize = Math.min(n, this.size);
    Object[] result = new Object[arraySize];

    CircularLinkedNode<E> current = head;
    for (int i = 0; i < arraySize && current != null; i++) {
      result[i] = current.get();
      current = current.getNext();
    }

    return (E[]) result;
  }

  @Override
  public E[] peekLastArray(int n) {
    if (isEmpty() || n <= 0) {
      return (E[]) new Object[0];
    }

    int arraySize = Math.min(n, this.size);
    Object[] result = new Object[arraySize];

    int startPosition = this.size - arraySize;

    CircularLinkedNode<E> current = head;
    for (int i = 0; i < startPosition; i++) {
      current = current.getNext();
    }

    for (int i = 0; i < arraySize && current != null; i++) {
      result[i] = current.get();
      current = current.getNext();
    }

    return (E[]) result;
  }

  @Override
  public List<E> peekCollection(int n) {
    CircularLinkedList<E> result = new CircularLinkedList<>();

    if (isEmpty() || n <= 0) {
      return result;
    }

    CircularLinkedNode<E> current = head;
    int count = 0;

    while (current != null && count < n) {
      result.add(current.get());
      current = current.getNext();
      count++;
    }

    return result;
  }

  @Override
  public List<E> peekLastCollection(int n) {
    CircularLinkedList<E> result = new CircularLinkedList<>();

    if (isEmpty() || n <= 0) {
      return result;
    }

    int elementsToTake = Math.min(n, this.size);
    int startPosition = this.size - elementsToTake;

    CircularLinkedNode<E> current = head;
    for (int i = 0; i < startPosition; i++) {
      current = current.getNext();
    }

    while (current != null) {
      result.add(current.get());
      current = current.getNext();
    }

    return result;
  }

  @Override
  public E poll() {
    if (isEmpty()) {
      return null;
    }

    E element = head.get();

    if (size == 1) {
      clear();
    } else {
      head = head.getNext();
      if (tail != null)
        tail.setNext(head);
      size--;
    }

    return element;
  }

  @Override
  public E pollLast() {
    if (isEmpty()) {
      return null;
    }

    E element = tail.get();

    if (size == 1) {
      clear();
      return element;
    }

    CircularLinkedNode<E> current = head;
    while (current.getNext() != tail) {
      current = current.getNext();
    }

    current.setNext(null);
    tail = current;
    size--;

    return element;
  }

  @Override
  public E[] pollArray(int n) {
    if (isEmpty() || n <= 0) {
      return (E[]) new Object[0];
    }

    int arraySize = Math.min(n, this.size);
    Object[] result = new Object[arraySize];

    for (int i = 0; i < arraySize; i++) {
      result[i] = poll();
    }

    return (E[]) result;
  }

  @Override
  public E[] pollLastArray(int n) {
    if (isEmpty() || n <= 0) {
      return (E[]) new Object[0];
    }

    int arraySize = Math.min(n, this.size);
    Object[] result = new Object[arraySize];

    for (int i = arraySize - 1; i >= 0; i--) {
      result[i] = pollLast();
    }

    return (E[]) result;
  }

  @Override
  public List<E> pollCollection(int n) {
    CircularLinkedList<E> result = new CircularLinkedList<>();

    if (isEmpty() || n <= 0) {
      return result;
    }

    int elementsToRemove = Math.min(n, this.size);

    for (int i = 0; i < elementsToRemove; i++) {
      E element = poll();
      if (element != null) {
        result.add(element);
      }
    }

    return result;
  }

  @Override
  public List<E> pollLastCollection(int n) {
    CircularLinkedList<E> result = new CircularLinkedList<>();

    if (isEmpty() || n <= 0) {
      return result;
    }

    int elementsToRemove = Math.min(n, this.size);

    Object[] temp = new Object[elementsToRemove];

    for (int i = elementsToRemove - 1; i >= 0; i--) {
      temp[i] = pollLast();
    }

    for (Object element : temp) {
      if (element != null) {
        result.add((E) element);
      }
    }

    return result;
  }

  @Override
  public boolean remove(E element) {
    if (isEmpty() || element == null) {
      return false;
    }

    if (head.get().equals(element)) {
      if (size == 1) {
        return clear();
      } else {
        head = head.getNext();
        if (tail != null)
          tail.setNext(head);
        size--;
        return true;
      }
    }

    CircularLinkedNode<E> current = head;
    CircularLinkedNode<E> previous = null;

    while (current != null && !current.get().equals(element)) {
      previous = current;
      current = current.getNext();
    }

    if (current != null) {
      previous.setNext(current.getNext());

      if (current == tail) {
        tail = previous;
      }

      size--;
      return true;
    }

    return false;
  }

  @Override
  public boolean remove(E[] array) {
    if (array == null) {
      return false;
    }

    boolean anyRemoved = false;
    for (E element : array) {
      if (this.remove(element)) {
        anyRemoved = true;
      }
    }
    return anyRemoved;
  }

  @Override
  public boolean remove(Collection<E> collection) {
    if (collection == null) {
      return false;
    }

    boolean anyRemoved = false;
    Iterator<E> iterator = collection.iterator();
    while (iterator.hasNext()) {
      if (this.remove(iterator.next())) {
        anyRemoved = true;
      }
    }
    return anyRemoved;
  }

  @Override
  public boolean remove(Predicate<E> filter) {
    if (filter == null || isEmpty()) {
      return false;
    }

    boolean anyRemoved = false;
    CircularLinkedNode<E> current = head;
    CircularLinkedNode<E> previous = null;

    while (current != null) {
      if (filter.test(current.get())) {
        if (current == head) {
          head = head.getNext();
          if (tail != null)
            tail.setNext(head);
          if (size == 1) {
            tail = null;
          }
        } else {
          previous.setNext(current.getNext());
          if (current == tail) {
            tail = previous;
          }
        }
        size--;
        anyRemoved = true;

        current = (previous == null) ? head : previous.getNext();
      } else {
        previous = current;
        current = current.getNext();
      }
    }

    return anyRemoved;
  }

  @Override
  public boolean replace(E element, E newElement, Predicate<E> comparator) {
    if (isEmpty() || element == null || newElement == null) {
      return false;
    }

    CircularLinkedNode<E> current = head;
    boolean replaced = false;

    while (current != null) {
      if (comparator != null) {
        if (comparator.test(current.get())) {
          current.set(newElement);
          replaced = true;
        }
      } else {
        if (current.get().equals(element)) {
          current.set(newElement);
          replaced = true;
        }
      }
      current = current.getNext();
    }

    return replaced;
  }

  @Override
  public boolean replace(E[] array, E[] newArray, Predicate<E> comparator) {
    if (array == null || newArray == null || array.length != newArray.length) {
      return false;
    }

    boolean anyReplaced = false;
    for (int i = 0; i < array.length; i++) {
      if (replace(array[i], newArray[i], comparator)) {
        anyReplaced = true;
      }
    }

    return anyReplaced;
  }

  @Override
  public boolean replace(Collection<E> collection, Collection<E> newCollection, Predicate<E> comparator) {
    if (collection == null || newCollection == null) {
      return false;
    }

    int countOld = 0;
    Iterator<E> counterIter1 = collection.iterator();
    while (counterIter1.hasNext()) {
      counterIter1.next();
      countOld++;
    }

    int countNew = 0;
    Iterator<E> counterIter2 = newCollection.iterator();
    while (counterIter2.hasNext()) {
      counterIter2.next();
      countNew++;
    }

    if (countOld != countNew) {
      return false;
    }

    Object[] oldArray = new Object[countOld];
    Object[] newArray = new Object[countNew];

    int i = 0;
    Iterator<E> iterator1 = collection.iterator();
    while (iterator1.hasNext()) {
      oldArray[i++] = iterator1.next();
    }

    i = 0;
    Iterator<E> iterator2 = newCollection.iterator();
    while (iterator2.hasNext()) {
      newArray[i++] = iterator2.next();
    }

    return replace((E[]) oldArray, (E[]) newArray, comparator);
  }

  @Override
  public boolean retain(E[] array) {
    if (array == null || isEmpty()) {
      return false;
    }

    boolean modified = false;
    CircularLinkedNode<E> current = head;
    CircularLinkedNode<E> previous = null;

    while (current != null) {
      boolean found = false;

      for (E element : array) {
        if (current.get().equals(element)) {
          found = true;
          break;
        }
      }

      if (!found) {
        if (current == head) {
          head = head.getNext();
          if (tail != null)
            tail.setNext(head);
          if (size == 1) {
            tail = null;
          }
        } else {
          previous.setNext(current.getNext());
          if (current == tail) {
            tail = previous;
          }
        }
        size--;
        modified = true;

        current = (previous == null) ? head : previous.getNext();
      } else {
        previous = current;
        current = current.getNext();
      }
    }

    return modified;
  }

  @Override
  public boolean retain(Collection<E> collection) {
    if (collection == null || isEmpty()) {
      return false;
    }

    boolean modified = false;
    CircularLinkedNode<E> current = head;
    CircularLinkedNode<E> previous = null;

    while (current != null) {
      boolean found = false;

      Iterator<E> iterator = collection.iterator();
      while (iterator.hasNext()) {
        if (current.get().equals(iterator.next())) {
          found = true;
          break;
        }
      }

      if (!found) {
        if (current == head) {
          head = head.getNext();
          if (tail != null)
            tail.setNext(head);
          if (size == 1) {
            tail = null;
          }
        } else {
          previous.setNext(current.getNext());
          if (current == tail) {
            tail = previous;
          }
        }
        size--;
        modified = true;

        current = (previous == null) ? head : previous.getNext();
      } else {
        previous = current;
        current = current.getNext();
      }
    }

    return modified;
  }

  @Override
  public boolean set(E index, E element) {
    if (isEmpty() || index == null || element == null) {
      return false;
    }

    CircularLinkedNode<E> current = head;

    while (current != null) {
      if (current.get().equals(index)) {
        current.set(element);
        return true;
      }
      current = current.getNext();
    }

    return false;
  }

  @Override
  public boolean sort(ToIntFunction<E> toInt) {
    if (isEmpty() || size == 1 || toInt == null) {
      return false;
    }

    boolean swapped;
    do {
      swapped = false;
      CircularLinkedNode<E> current = head;
      CircularLinkedNode<E> previous = null;
      CircularLinkedNode<E> next = head.getNext();

      while (next != null) {
        int currentValue = toInt.applyAsInt(current.get());
        int nextValue = toInt.applyAsInt(next.get());

        if (currentValue > nextValue) {
          swapped = true;

          if (previous != null) {
            CircularLinkedNode<E> temp = next.getNext();
            previous.setNext(next);
            next.setNext(current);
            current.setNext(temp);
          } else {
            CircularLinkedNode<E> temp = next.getNext();
            head = next;
            next.setNext(current);
            current.setNext(temp);
          }

          previous = next;
          next = current.getNext();
        } else {
          previous = current;
          current = next;
          next = next.getNext();
        }
      }

      tail = current;
    } while (swapped);

    return true;
  }

  @Override
  public List<E> subList(E from, E to) {
    if (isEmpty() || from == null || to == null) {
      return new CircularLinkedList<>();
    }

    CircularLinkedList<E> result = new CircularLinkedList<>();
    CircularLinkedNode<E> current = head;
    boolean started = false;

    while (current != null) {
      if (current.get().equals(from)) {
        started = true;
      }

      if (started) {
        result.add(current.get());

        if (current.get().equals(to)) {
          break;
        }
      }

      current = current.getNext();
    }

    return result;
  }

  @Override
  public E[] toArray() {
    if (isEmpty()) {
      return (E[]) new Object[0];
    }

    Object[] array = new Object[this.size];
    CircularLinkedNode<E> current = head;
    int i = 0;

    while (current != null) {
      array[i++] = current.get();
      current = current.getNext();
    }

    return (E[]) array;
  }

  @Override
  public boolean clear() {
    head = tail = inode = null;
    size = 0;
    return true;
  }

  @Override
  public boolean contains(E element) {
    if (isEmpty()) {
      return false;
    }
    CircularLinkedNode<E> current = head;
    while (current != null) {
      if (current.get().equals(element)) {
        return true;
      }
      current = current.getNext();
    }
    return false;
  }

  @Override
  public boolean contains(E[] array) {
    if (array == null) {
      return false;
    }
    for (E item : array) {
      if (!contains(item)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean contains(Collection<E> collection) {
    if (collection == null) {
      return false;
    }
    Iterator<E> iterator = collection.iterator();
    while (iterator.hasNext()) {
      if (!contains(iterator.next())) {
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean reverse() {
    if (isEmpty() || size == 1) {
      return true;
    }

    CircularLinkedNode<E> previous = null;
    CircularLinkedNode<E> current = head;
    CircularLinkedNode<E> next = null;

    tail = head;

    while (current != null) {
      next = current.getNext();
      current.setNext(previous);
      previous = current;
      current = next;
    }

    head = previous;
    return true;
  }

  @Override
  public int size() {
    return this.size;
  }

  public boolean isEmpty() {
    return this.head == null;
  }

  @Override
  public String toString() {
    return "LinkedList [head=" + head + "]";
  }

  @Override
  public void forEach(Function<E, Void> action) {
    if (action == null || isEmpty()) {
      return;
    }

    CircularLinkedNode<E> current = head;
    while (current != null) {
      action.apply(current.get());
      current = current.getNext();
    }
  }

  @Override
  public Iterator<E> iterator() {
    inode = head;

    return new Iterator<E>() {

      public boolean hasNext() {
        return inode != null;
      }

      public E next() {
        if (!hasNext()) {
          throw new IllegalStateException("No more elements in the iterator");
        }

        E element = inode.get();
        inode = inode.getNext();
        return element;
      }
    };
  }
}
