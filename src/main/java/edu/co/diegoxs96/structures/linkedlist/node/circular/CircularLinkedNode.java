package edu.co.diegoxs96.structures.linkedlist.node.circular;

import edu.co.diegoxs96.structures.model.node.AbstractNode;

/**
 * Node for a singly circular linked list.
 *
 * @param <E> the type of element stored in the node.
 */
public class CircularLinkedNode<E> extends AbstractNode<E> {

    private CircularLinkedNode<E> next;

    public CircularLinkedNode() {
        super();
        this.next = null;
    }

    public CircularLinkedNode(E element) {
        super(element);
        this.next = null;
    }

    public CircularLinkedNode(E element, CircularLinkedNode<E> next) {
        super(element);
        this.next = next;
    }

    public CircularLinkedNode<E> getNext() {
        return this.next;
    }

    public void setNext(CircularLinkedNode<E> next) {
        this.next = next;
    }

    @Override
    public String toString() {
        return "CircularLinkedNode{" +
                "element=" + get().toString() +
                ", next=" + (next != null ? "CircularLinkedNode@" + System.identityHashCode(next) : "null") +
                '}';
    }
}