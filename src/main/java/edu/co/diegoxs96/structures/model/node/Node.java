package edu.co.diegoxs96.structures.model.node;

public class Node<E> extends AbstractNode<E> {

  private Node<E> left;
  private Node<E> right;

  public Node(E element) {
    super(element);
    this.left = null;
    this.right = null;
  }

  public Node<E> getLeft() {
    return left;
  }

  public void setLeft(Node<E> left) {
    this.left = left;
  }

  public Node<E> getRight() {
    return right;
  }

  public void setRight(Node<E> right) {
    this.right = right;
  }
}