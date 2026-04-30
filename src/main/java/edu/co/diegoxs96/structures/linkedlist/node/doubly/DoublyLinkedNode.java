package edu.co.diegoxs96.structures.linkedlist.node.doubly;

import edu.co.diegoxs96.structures.model.node.AbstractNode;

public class DoublyLinkedNode<E> extends AbstractNode<E> {

    private DoublyLinkedNode<E> next;
    private DoublyLinkedNode<E> previous;

    public DoublyLinkedNode() {
        super();
        this.next = null;
        this.previous = null;
    }

    public DoublyLinkedNode(E element) {
        super(element);
        this.next = null;
        this.previous = null;
    }

    public DoublyLinkedNode(E element, DoublyLinkedNode<E> next, DoublyLinkedNode<E> previous) {
        super(element);
        this.next = next;
        this.previous = previous;
    }

    public DoublyLinkedNode<E> getNext() {
        return this.next;
    }

    public void setNext(DoublyLinkedNode<E> next) {
        this.next = next;
    }

    public DoublyLinkedNode<E> getPrevious() {
        return this.previous;
    }

    public void setPrevious(DoublyLinkedNode<E> previous) {
        this.previous = previous;
    }

    @Override
    public String toString() {
        return "DoublyLinkedNode{" +
                "element=" + get().toString() +
                ", next=" + (next != null ? "DoublyLinkedNode@" + System.identityHashCode(next) : "null") +
                ", previous=" + (previous != null ? "DoublyLinkedNode@" + System.identityHashCode(previous) : "null") +
                '}';
    }
}