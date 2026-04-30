package edu.co.diegoxs96.structures.model.tree;

import edu.co.diegoxs96.structures.model.list.List;
import edu.co.diegoxs96.structures.model.node.AbstractNode;

public interface Tree<E> {

    List<E> preOrder();

    List<E> inOrder();

    List<E> postOrder();

    List<E> levelOrder();

    boolean insert(E element);

    boolean remove(E element);

    boolean search(E element);

    int getGrade();

    int getHeight();

    int size();

    double getLCI();

    double getLCIM();

    boolean isEmpty();

    boolean isFull();

    boolean isComplete();

    Tree<E> getSubtree(AbstractNode<E> root);

    Tree<E> getLeftSubtree(AbstractNode<E> root);

    Tree<E> getRightSubtree(AbstractNode<E> root);
}