package edu.co.diegoxs96.structures.linkedlist.node.doublyCircular;

import edu.co.diegoxs96.structures.model.node.AbstractNode;

public class DoublyCircularLinkedNode<E> extends AbstractNode<E> {

  private DoublyCircularLinkedNode<E> next;
  private DoublyCircularLinkedNode<E> previous;

  public DoublyCircularLinkedNode() {
    super();
    this.next = null;
    this.previous = null;
  }

  public DoublyCircularLinkedNode(E element) {
    super(element);
    this.next = null;
    this.previous = null;
  }

  public DoublyCircularLinkedNode(E element, DoublyCircularLinkedNode<E> next, DoublyCircularLinkedNode<E> previous) {
    super(element);
    this.next = next;
    this.previous = previous;
  }

  public DoublyCircularLinkedNode<E> getNext() {
    return this.next;
  }

  public void setNext(DoublyCircularLinkedNode<E> next) {
    this.next = next;
  }

  public DoublyCircularLinkedNode<E> getPrevious() {
    return this.previous;
  }

  public void setPrevious(DoublyCircularLinkedNode<E> previous) {
    this.previous = previous;
  }

  @Override
  public String toString() {
    return "DoublyCircularLinkedNode{" +
        "element=" + get().toString() +
        ", next=" + (next != null ? "DoublyCircularLinkedNode@" + System.identityHashCode(next) : "null") +
        ", previous=" + (previous != null ? "DoublyCircularLinkedNode@" + System.identityHashCode(previous) : "null") +
        '}';
  }
}