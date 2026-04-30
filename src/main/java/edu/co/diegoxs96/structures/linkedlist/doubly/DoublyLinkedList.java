package edu.co.diegoxs96.structures.linkedlist.doubly;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

import edu.co.diegoxs96.structures.linkedlist.node.doubly.DoublyLinkedNode;
import edu.co.diegoxs96.structures.model.collection.Collection;
import edu.co.diegoxs96.structures.model.iterator.Iterator;
import edu.co.diegoxs96.structures.model.list.AbstractList;
import edu.co.diegoxs96.structures.model.list.List;

public class DoublyLinkedList<E> extends AbstractList<E> {

    private transient DoublyLinkedNode<E> head;
    private transient DoublyLinkedNode<E> tail;
    private transient DoublyLinkedNode<E> inode;
    private transient int size;

    public DoublyLinkedList() {
        head = tail = inode = null;
        size = 0;
    }

    public DoublyLinkedList(E element) {
        this.head = tail = inode = new DoublyLinkedNode<>(element);
        this.size = 1;
    }

    @Override
    public boolean add(E element) {
        try {
            if (isEmpty()) {
                DoublyLinkedNode<E> node = new DoublyLinkedNode<>(element);
                this.head = this.tail = this.inode = node;
            } else {
                DoublyLinkedNode<E> node = new DoublyLinkedNode<>(element);
                this.tail.setNext(node);
                node.setPrevious(this.tail);
                this.tail = node;
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
            DoublyLinkedNode<E> node = new DoublyLinkedNode<>(element);

            if (isEmpty()) {
                head = tail = node;
            } else {
                node.setNext(head);
                head.setPrevious(node);
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

        DoublyLinkedNode<E> current = head;
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

        DoublyLinkedNode<E> current = tail;
        for (int i = arraySize - 1; i >= 0 && current != null; i--) {
            result[i] = current.get();
            current = current.getPrevious();
        }

        return (E[]) result;
    }

    @Override
    public List<E> peekCollection(int n) {
        DoublyLinkedList<E> result = new DoublyLinkedList<>();

        if (isEmpty() || n <= 0) {
            return result;
        }

        DoublyLinkedNode<E> current = head;
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
        DoublyLinkedList<E> result = new DoublyLinkedList<>();

        if (isEmpty() || n <= 0) {
            return result;
        }

        int elementsToTake = Math.min(n, this.size);
        DoublyLinkedNode<E> current = tail;

        for (int i = 0; i < elementsToTake && current != null; i++) {
            result.addFirst(current.get());
            current = current.getPrevious();
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
            head.setPrevious(null);
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
        } else {
            tail = tail.getPrevious();
            tail.setNext(null);
            size--;
        }

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
        DoublyLinkedList<E> result = new DoublyLinkedList<>();

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
        DoublyLinkedList<E> result = new DoublyLinkedList<>();

        if (isEmpty() || n <= 0) {
            return result;
        }

        int elementsToRemove = Math.min(n, this.size);

        for (int i = 0; i < elementsToRemove; i++) {
            E element = pollLast();
            if (element != null) {
                result.addFirst(element);
            }
        }

        return result;
    }

    @Override
    public boolean remove(E element) {
        if (isEmpty() || element == null) {
            return false;
        }

        DoublyLinkedNode<E> current = head;

        while (current != null) {
            if (current.get().equals(element)) {
                if (current == head) {
                    poll();
                } else if (current == tail) {
                    pollLast();
                } else {
                    current.getPrevious().setNext(current.getNext());
                    current.getNext().setPrevious(current.getPrevious());
                    size--;
                }
                return true;
            }
            current = current.getNext();
        }

        return false;
    }

    @Override
    public boolean remove(E[] array) {
        if (array == null) {
            return false;
        }

        boolean removed = false;
        for (E element : array) {
            if (remove(element)) {
                removed = true;
            }
        }
        return removed;
    }

    @Override
    public boolean remove(Collection<E> collection) {
        if (collection == null) {
            return false;
        }

        boolean removed = false;
        Iterator<E> iterator = collection.iterator();
        while (iterator.hasNext()) {
            if (remove(iterator.next())) {
                removed = true;
            }
        }
        return removed;
    }

    @Override
    public boolean remove(Predicate<E> filter) {
        if (filter == null || isEmpty()) {
            return false;
        }

        boolean anyRemoved = false;
        DoublyLinkedNode<E> current = head;

        while (current != null) {
            DoublyLinkedNode<E> next = current.getNext();

            if (filter.test(current.get())) {
                if (current == head) {
                    poll();
                } else if (current == tail) {
                    pollLast();
                } else {
                    current.getPrevious().setNext(current.getNext());
                    current.getNext().setPrevious(current.getPrevious());
                    size--;
                }
                anyRemoved = true;
            }

            current = next;
        }

        return anyRemoved;
    }

    @Override
    public boolean replace(E element, E newElement, Predicate<E> comparator) {
        if (isEmpty() || element == null || newElement == null) {
            return false;
        }

        DoublyLinkedNode<E> current = head;

        while (current != null) {
            if (comparator.test(current.get())) {
                current.set(newElement);
                return true;
            }
            current = current.getNext();
        }

        return false;
    }

    @Override
    public boolean replace(E[] array, E[] newArray, Predicate<E> comparator) {
        if (array == null || newArray == null || array.length != newArray.length) {
            return false;
        }

        boolean replaced = false;
        for (int i = 0; i < array.length; i++) {
            if (replace(array[i], newArray[i], comparator)) {
                replaced = true;
            }
        }
        return replaced;
    }

    @Override
    public boolean replace(Collection<E> collection, Collection<E> newCollection, Predicate<E> comparator) {
        if (collection == null || newCollection == null) {
            return false;
        }

        int countOld = 0;
        int countNew = 0;

        Iterator<E> counterIter1 = collection.iterator();
        while (counterIter1.hasNext()) {
            counterIter1.next();
            countOld++;
        }

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
        DoublyLinkedNode<E> current = head;

        while (current != null) {
            boolean found = false;

            for (E element : array) {
                if (current.get().equals(element)) {
                    found = true;
                    break;
                }
            }

            DoublyLinkedNode<E> next = current.getNext();

            if (!found) {
                if (current == head) {
                    poll();
                } else if (current == tail) {
                    pollLast();
                } else {
                    current.getPrevious().setNext(current.getNext());
                    current.getNext().setPrevious(current.getPrevious());
                    size--;
                }
                modified = true;
            }

            current = next;
        }

        return modified;
    }

    @Override
    public boolean retain(Collection<E> collection) {
        if (collection == null || isEmpty()) {
            return false;
        }

        boolean modified = false;
        DoublyLinkedNode<E> current = head;

        while (current != null) {
            boolean found = false;

            Iterator<E> iterator = collection.iterator();
            while (iterator.hasNext()) {
                if (current.get().equals(iterator.next())) {
                    found = true;
                    break;
                }
            }

            DoublyLinkedNode<E> next = current.getNext();

            if (!found) {
                if (current == head) {
                    poll();
                } else if (current == tail) {
                    pollLast();
                } else {
                    current.getPrevious().setNext(current.getNext());
                    current.getNext().setPrevious(current.getPrevious());
                    size--;
                }
                modified = true;
            }

            current = next;
        }

        return modified;
    }

    @Override
    public boolean set(E index, E element) {
        if (isEmpty() || index == null || element == null) {
            return false;
        }

        DoublyLinkedNode<E> current = head;

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
            DoublyLinkedNode<E> current = head;

            while (current != null && current.getNext() != null) {
                int currentValue = toInt.applyAsInt(current.get());
                int nextValue = toInt.applyAsInt(current.getNext().get());

                if (currentValue > nextValue) {
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

    @Override
    public List<E> subList(E from, E to) {
        if (isEmpty() || from == null || to == null) {
            return new DoublyLinkedList<>();
        }

        DoublyLinkedList<E> result = new DoublyLinkedList<>();
        DoublyLinkedNode<E> current = head;
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
        DoublyLinkedNode<E> current = head;
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
        DoublyLinkedNode<E> current = head;
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

        DoublyLinkedNode<E> current = head;
        DoublyLinkedNode<E> temp = null;

        while (current != null) {
            temp = current.getPrevious();
            current.setPrevious(current.getNext());
            current.setNext(temp);
            current = current.getPrevious();
        }

        temp = head;
        head = tail;
        tail = temp;

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
        return "DoublyLinkedList [head=" + head + "]";
    }

    @Override
    public void forEach(Function<E, Void> action) {
        if (action == null || isEmpty()) {
            return;
        }

        DoublyLinkedNode<E> current = head;
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