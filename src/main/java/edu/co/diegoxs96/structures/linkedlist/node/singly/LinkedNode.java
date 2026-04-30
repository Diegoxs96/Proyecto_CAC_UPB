package edu.co.diegoxs96.structures.linkedlist.node.singly;

import edu.co.diegoxs96.structures.model.node.AbstractNode;

public class LinkedNode<E> extends AbstractNode<E> {

  private LinkedNode<E> next;

  public LinkedNode() {
    super();
    this.next = null;
  }

  public LinkedNode(E element) {
    super(element);
    this.next = null;
  }

  public LinkedNode(E element, LinkedNode<E> next) {
    super(element);
    this.next = next;
  }

  public LinkedNode<E> getNext() {
    return this.next;
  }

  public void setNext(LinkedNode<E> next) {
    this.next = next;
  }

  @Override
  public String toString() {
    return "LinkedNode{" +
        "element=" + get().toString() +
        ", next=" + next +
        '}';
  }
}
