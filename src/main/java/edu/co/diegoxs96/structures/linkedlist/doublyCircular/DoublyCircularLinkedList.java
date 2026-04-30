package edu.co.diegoxs96.structures.linkedlist.doublyCircular;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

import edu.co.diegoxs96.structures.linkedlist.node.doublyCircular.DoublyCircularLinkedNode;
import edu.co.diegoxs96.structures.model.collection.Collection;
import edu.co.diegoxs96.structures.model.iterator.Iterator;
import edu.co.diegoxs96.structures.model.list.AbstractList;
import edu.co.diegoxs96.structures.model.list.List;

public class DoublyCircularLinkedList<E> extends AbstractList<E> {

    private transient DoublyCircularLinkedNode<E> head;
    private transient DoublyCircularLinkedNode<E> tail;
    private transient DoublyCircularLinkedNode<E> inode;
    private transient int size;

    public DoublyCircularLinkedList() {
        head = tail = inode = null;
        size = 0;
    }

    public DoublyCircularLinkedList(E element) {
        DoublyCircularLinkedNode<E> node = new DoublyCircularLinkedNode<>(element);
        this.head = this.tail = this.inode = node;
        node.setNext(node);
        node.setPrevious(node);
        this.size = 1;
    }

    @Override
    public boolean add(E element) {
        try {
            if (isEmpty()) {
                DoublyCircularLinkedNode<E> node = new DoublyCircularLinkedNode<>(element);
                this.head = this.tail = this.inode = node;
                node.setNext(node);
                node.setPrevious(node);
            } else {
                DoublyCircularLinkedNode<E> node = new DoublyCircularLinkedNode<>(element);
                this.tail.setNext(node);
                node.setPrevious(this.tail);
                node.setNext(this.head);
                this.head.setPrevious(node);
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
            DoublyCircularLinkedNode<E> node = new DoublyCircularLinkedNode<>(element);

            if (isEmpty()) {
                head = tail = node;
                node.setNext(node);
                node.setPrevious(node);
            } else {
                node.setNext(head);
                node.setPrevious(tail);
                head.setPrevious(node);
                tail.setNext(node);
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

        DoublyCircularLinkedNode<E> current = head;
        for (int i = 0; i < arraySize; i++) {
            result[i] = current.get();
            current = current.getNext();
            if (current == head && i < arraySize - 1) {
                break;
            }
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

        DoublyCircularLinkedNode<E> current = tail;
        for (int i = arraySize - 1; i >= 0; i--) {
            result[i] = current.get();
            current = current.getPrevious();
            if (current == tail && i > 0) {
                break;
            }
        }

        return (E[]) result;
    }

    @Override
    public List<E> peekCollection(int n) {
        DoublyCircularLinkedList<E> result = new DoublyCircularLinkedList<>();

        if (isEmpty() || n <= 0) {
            return result;
        }

        DoublyCircularLinkedNode<E> current = head;
        int count = 0;

        while (count < n && count < size) {
            result.add(current.get());
            current = current.getNext();
            count++;
        }

        return result;
    }

    @Override
    public List<E> peekLastCollection(int n) {
        DoublyCircularLinkedList<E> result = new DoublyCircularLinkedList<>();

        if (isEmpty() || n <= 0) {
            return result;
        }

        int elementsToTake = Math.min(n, this.size);
        DoublyCircularLinkedNode<E> current = tail;

        for (int i = 0; i < elementsToTake; i++) {
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
            head.setPrevious(tail);
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
        } else {
            tail = tail.getPrevious();
            tail.setNext(head);
            head.setPrevious(tail);
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
        DoublyCircularLinkedList<E> result = new DoublyCircularLinkedList<>();

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
        DoublyCircularLinkedList<E> result = new DoublyCircularLinkedList<>();

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

        DoublyCircularLinkedNode<E> current = head;
        int count = 0;

        do {
            if (current.get().equals(element)) {
                if (size == 1) {
                    clear();
                } else if (current == head) {
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
            count++;
        } while (current != head && count < size);

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
        DoublyCircularLinkedNode<E> current = head;
        int count = 0;

        while (count < size) {
            DoublyCircularLinkedNode<E> next = current.getNext();

            if (filter.test(current.get())) {
                if (size == 1) {
                    clear();
                    return true;
                } else if (current == head) {
                    poll();
                } else if (current == tail) {
                    pollLast();
                } else {
                    current.getPrevious().setNext(current.getNext());
                    current.getNext().setPrevious(current.getPrevious());
                    size--;
                }
                anyRemoved = true;
            } else {
                count++;
            }

            current = next;
            if (isEmpty()) {
                break;
            }
        }

        return anyRemoved;
    }

    @Override
    public boolean replace(E element, E newElement, Predicate<E> comparator) {
        if (isEmpty() || element == null || newElement == null) {
            return false;
        }

        DoublyCircularLinkedNode<E> current = head;
        int count = 0;

        do {
            if (comparator.test(current.get())) {
                current.set(newElement);
                return true;
            }
            current = current.getNext();
            count++;
        } while (current != head && count < size);

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
        DoublyCircularLinkedNode<E> current = head;
        int count = 0;

        while (count < size) {
            boolean found = false;

            for (E element : array) {
                if (current.get().equals(element)) {
                    found = true;
                    break;
                }
            }

            DoublyCircularLinkedNode<E> next = current.getNext();

            if (!found) {
                if (size == 1) {
                    clear();
                    return true;
                } else if (current == head) {
                    poll();
                } else if (current == tail) {
                    pollLast();
                } else {
                    current.getPrevious().setNext(current.getNext());
                    current.getNext().setPrevious(current.getPrevious());
                    size--;
                }
                modified = true;
            } else {
                count++;
            }

            current = next;
            if (isEmpty()) {
                break;
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
        DoublyCircularLinkedNode<E> current = head;
        int count = 0;

        while (count < size) {
            boolean found = false;

            Iterator<E> iterator = collection.iterator();
            while (iterator.hasNext()) {
                if (current.get().equals(iterator.next())) {
                    found = true;
                    break;
                }
            }

            DoublyCircularLinkedNode<E> next = current.getNext();

            if (!found) {
                if (size == 1) {
                    clear();
                    return true;
                } else if (current == head) {
                    poll();
                } else if (current == tail) {
                    pollLast();
                } else {
                    current.getPrevious().setNext(current.getNext());
                    current.getNext().setPrevious(current.getPrevious());
                    size--;
                }
                modified = true;
            } else {
                count++;
            }

            current = next;
            if (isEmpty()) {
                break;
            }
        }

        return modified;
    }

    @Override
    public boolean set(E index, E element) {
        if (isEmpty() || index == null || element == null) {
            return false;
        }

        DoublyCircularLinkedNode<E> current = head;
        int count = 0;

        do {
            if (current.get().equals(index)) {
                current.set(element);
                return true;
            }
            current = current.getNext();
            count++;
        } while (current != head && count < size);

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
            DoublyCircularLinkedNode<E> current = head;
            int count = 0;

            while (count < size - 1) {
                DoublyCircularLinkedNode<E> next = current.getNext();
                int currentValue = toInt.applyAsInt(current.get());
                int nextValue = toInt.applyAsInt(next.get());

                if (currentValue > nextValue) {
                    E temp = current.get();
                    current.set(next.get());
                    next.set(temp);
                    swapped = true;
                }

                current = next;
                count++;
            }
        } while (swapped);

        return true;
    }

    @Override
    public List<E> subList(E from, E to) {
        if (isEmpty() || from == null || to == null) {
            return new DoublyCircularLinkedList<>();
        }

        DoublyCircularLinkedList<E> result = new DoublyCircularLinkedList<>();
        DoublyCircularLinkedNode<E> current = head;
        boolean started = false;
        int count = 0;

        do {
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
            count++;
        } while (current != head && count < size);

        return result;
    }

    @Override
    public E[] toArray() {
        if (isEmpty()) {
            return (E[]) new Object[0];
        }

        Object[] array = new Object[this.size];
        DoublyCircularLinkedNode<E> current = head;
        int i = 0;

        do {
            array[i++] = current.get();
            current = current.getNext();
        } while (current != head && i < size);

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
        DoublyCircularLinkedNode<E> current = head;
        int count = 0;
        do {
            if (current.get().equals(element)) {
                return true;
            }
            current = current.getNext();
            count++;
        } while (current != head && count < size);
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

        DoublyCircularLinkedNode<E> current = head;
        DoublyCircularLinkedNode<E> temp = null;
        int count = 0;

        do {
            temp = current.getPrevious();
            current.setPrevious(current.getNext());
            current.setNext(temp);
            current = current.getPrevious();
            count++;
        } while (count < size);

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
        return "DoublyCircularLinkedList [head=" + head + "]";
    }

    @Override
    public void forEach(Function<E, Void> action) {
        if (action == null || isEmpty()) {
            return;
        }

        DoublyCircularLinkedNode<E> current = head;
        int count = 0;
        do {
            action.apply(current.get());
            current = current.getNext();
            count++;
        } while (current != head && count < size);
    }

    @Override
    public Iterator<E> iterator() {
        inode = head;
        final int currentSize = size;
        final DoublyCircularLinkedNode<E> startNode = head;

        return new Iterator<E>() {
            private int count = 0;

            public boolean hasNext() {
                return inode != null && count < currentSize;
            }

            public E next() {
                if (!hasNext()) {
                    throw new IllegalStateException("No more elements in the iterator");
                }

                E element = inode.get();
                inode = inode.getNext();
                count++;
                return element;
            }
        };
    }
}