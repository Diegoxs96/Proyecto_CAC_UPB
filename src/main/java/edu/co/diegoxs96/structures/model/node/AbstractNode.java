package edu.co.diegoxs96.structures.model.node;

public abstract class AbstractNode<E> {

  protected E element;

  public AbstractNode() {
    this.element = null;
  }

  public AbstractNode(E element) {
    this.element = element;
  }

  public E get() {
    return element;
  }

  public void set(E element) {
    this.element = element;
  }
}
