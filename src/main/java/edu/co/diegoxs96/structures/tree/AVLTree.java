package edu.co.diegoxs96.structures.tree;

import java.util.Queue;

import edu.co.diegoxs96.structures.linkedlist.singly.LinkedList;
import edu.co.diegoxs96.structures.model.list.List;
import edu.co.diegoxs96.structures.model.node.Node;
import edu.co.diegoxs96.structures.model.search.SearchResult;
import edu.co.diegoxs96.structures.model.tree.AbstractTree;
import edu.co.diegoxs96.structures.model.tree.Tree;

public class AVLTree<E> extends AbstractTree<E> {

    private static class AVLNode<E> extends Node<E> {
        int height;

        AVLNode(E element) {
            super(element);
            this.height = 1;
        }

        AVLNode<E> left() {
            return (AVLNode<E>) getLeft();
        }

        AVLNode<E> right() {
            return (AVLNode<E>) getRight();
        }
    }

    private AVLNode<E> root;
    private int size;

    public AVLTree() {
        root = null;
        size = 0;
    }


    private int h(AVLNode<E> n) {
        return (n == null) ? 0 : n.height;
    }

    private void updateHeight(AVLNode<E> n) {
        n.height = 1 + Math.max(h(n.left()), h(n.right()));
    }

    private int balanceFactor(AVLNode<E> n) {
        return (n == null) ? 0 : h(n.left()) - h(n.right());
    }

    private AVLNode<E> rotateRight(AVLNode<E> y) {
        AVLNode<E> x = y.left();
        AVLNode<E> T2 = x.right();

        x.setRight(y);
        y.setLeft(T2);

        updateHeight(y);
        updateHeight(x);

        return x;
    }

    private AVLNode<E> rotateLeft(AVLNode<E> x) {
        AVLNode<E> y = x.right();
        AVLNode<E> T2 = y.left();

        y.setLeft(x);
        x.setRight(T2);

        updateHeight(x);
        updateHeight(y);

        return y;
    }

    private AVLNode<E> rebalance(AVLNode<E> node) {
        updateHeight(node);
        int bf = balanceFactor(node);

        // Caso LL
        if (bf > 1 && balanceFactor(node.left()) >= 0)
            return rotateRight(node);

        // Caso LR
        if (bf > 1 && balanceFactor(node.left()) < 0) {
            node.setLeft(rotateLeft(node.left()));
            return rotateRight(node);
        }

        // Caso RR
        if (bf < -1 && balanceFactor(node.right()) <= 0)
            return rotateLeft(node);

        // Caso RL
        if (bf < -1 && balanceFactor(node.right()) > 0) {
            node.setRight(rotateRight(node.right()));
            return rotateLeft(node);
        }

        return node; // ya está balanceado
    }


    @Override
    @SuppressWarnings("unchecked")
    public boolean insert(E element) {
        if (element == null)
            return false;
        root = insertR(root, element);
        size++;
        return true;
    }

    @SuppressWarnings("unchecked")
    private AVLNode<E> insertR(AVLNode<E> node, E element) {
        if (node == null)
            return new AVLNode<>(element);

        Comparable<E> cmp = (Comparable<E>) element;
        int r = cmp.compareTo(node.get());

        if (r < 0)
            node.setLeft(insertR(node.left(), element));
        else if (r > 0)
            node.setRight(insertR(node.right(), element));
        else
            return node; // duplicado: no inserta

        return rebalance(node);
    }

    @Override
    public boolean remove(E element) {
        if (!search(element))
            return false;
        root = deleteR(root, element);
        size--;
        return true;
    }

    @SuppressWarnings("unchecked")
    private AVLNode<E> deleteR(AVLNode<E> node, E element) {
        if (node == null)
            return null;

        Comparable<E> cmp = (Comparable<E>) element;
        int r = cmp.compareTo(node.get());

        if (r < 0)
            node.setLeft(deleteR(node.left(), element));
        else if (r > 0)
            node.setRight(deleteR(node.right(), element));
        else {

            if (node.left() == null)
                return node.right();
            if (node.right() == null)
                return node.left();

            AVLNode<E> successor = min(node.right());
            node.set(successor.get());
            node.setRight(deleteR(node.right(), successor.get()));
        }

        return rebalance(node);
    }

    private AVLNode<E> min(AVLNode<E> node) {
        while (node.left() != null)
            node = node.left();
        return node;
    }


    @Override
    public boolean search(E element) {
        return searchR(root, element);
    }

    @SuppressWarnings("unchecked")
    private boolean searchR(AVLNode<E> node, E element) {
        if (node == null)
            return false;

        int r = ((Comparable<E>) element).compareTo(node.get());

        if (r == 0)
            return true;
        if (r < 0)
            return searchR(node.left(), element);
        else
            return searchR(node.right(), element);
    }

    @SuppressWarnings("unchecked")
    public SearchResult searchWithMetrics(E element) {
        long start = System.nanoTime();
        int steps = 0;

        AVLNode<E> current = root;
        Comparable<E> cmp = (Comparable<E>) element;

        while (current != null) {
            steps++;
            int r = cmp.compareTo(current.get());

            if (r == 0)
                return new SearchResult(true, steps, System.nanoTime() - start);

            current = (r < 0) ? current.left() : current.right();
        }

        return new SearchResult(false, steps, System.nanoTime() - start);
    }

    public SearchResult searchByList(List<E> list, E target) {
        long start = System.nanoTime();
        int steps = 0;

        LinkedList<E> l = (LinkedList<E>) list;
        var current = l.getHead();

        while (current != null) {
            steps++;
            if (current.get().equals(target))
                return new SearchResult(true, steps, System.nanoTime() - start);
            current = current.getNext();
        }

        return new SearchResult(false, steps, System.nanoTime() - start);
    }


    @Override
    public List<E> inOrder() {
        List<E> list = new LinkedList<>();
        inOrderR(root, list);
        return list;
    }

    private void inOrderR(AVLNode<E> node, List<E> list) {
        if (node == null)
            return;
        inOrderR(node.left(), list);
        list.add(node.get());
        inOrderR(node.right(), list);
    }

    @Override
    public List<E> preOrder() {
        List<E> list = new LinkedList<>();
        preOrderR(root, list);
        return list;
    }

    private void preOrderR(AVLNode<E> node, List<E> list) {
        if (node == null)
            return;
        list.add(node.get());
        preOrderR(node.left(), list);
        preOrderR(node.right(), list);
    }

    @Override
    public List<E> postOrder() {
        List<E> list = new LinkedList<>();
        postOrderR(root, list);
        return list;
    }

    private void postOrderR(AVLNode<E> node, List<E> list) {
        if (node == null)
            return;
        postOrderR(node.left(), list);
        postOrderR(node.right(), list);
        list.add(node.get());
    }

    @Override
    public List<E> levelOrder() {
        List<E> list = new LinkedList<>();
        if (root == null)
            return list;

        Queue<AVLNode<E>> queue = new java.util.LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            AVLNode<E> temp = queue.poll();
            list.add(temp.get());

            if (temp.left() != null)
                queue.add(temp.left());
            if (temp.right() != null)
                queue.add(temp.right());
        }

        return list;
    }

    public void printTree() {
        printTree(root, "");
    }

    private void printTree(AVLNode<E> node, String indent) {
        if (node == null)
            return;

        System.out.println(indent + node.get() + "  [bf=" + balanceFactor(node) + "]");

        boolean hasLeft = node.left() != null;
        boolean hasRight = node.right() != null;

        if (hasLeft || hasRight) {
            System.out.println(indent + "  |");
            if (hasLeft)
                printTree(node.left(), indent + "  -- ");
            if (hasRight)
                printTree(node.right(), indent + "  -- ");
        }
    }

    public void balance() {
        java.util.List<E> sorted = new java.util.ArrayList<>();
        inOrderToList(root, sorted);
        root = buildBalanced(sorted, 0, sorted.size() - 1);
    }

    private void inOrderToList(AVLNode<E> node, java.util.List<E> list) {
        if (node == null)
            return;
        inOrderToList(node.left(), list);
        list.add(node.get());
        inOrderToList(node.right(), list);
    }

    private AVLNode<E> buildBalanced(java.util.List<E> list, int start, int end) {
        if (start > end)
            return null;

        int mid = (start + end) / 2;
        AVLNode<E> node = new AVLNode<>(list.get(mid));
        node.setLeft(buildBalanced(list, start, mid - 1));
        node.setRight(buildBalanced(list, mid + 1, end));
        updateHeight(node);

        return node;
    }

    public int getHeight() {
        return h(root);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return root == null;
    }

    @Override
    public int getGrade() {
        return 2;
    }

    @Override
    public double getLCI() {
        return lciR(root, 0);
    }

    private int lciR(AVLNode<E> node, int level) {
        if (node == null)
            return 0;
        return level + lciR(node.left(), level + 1) + lciR(node.right(), level + 1);
    }

    @Override
    public double getLCIM() {
        return (size == 0) ? 0 : getLCI() / size;
    }

    @Override
    public boolean isComplete() {
        if (root == null)
            return true;

        Queue<Node<E>> queue = new java.util.LinkedList<>();
        queue.add(root);
        boolean end = false;

        while (!queue.isEmpty()) {
            Node<E> temp = queue.poll();
            if (temp == null) {
                end = true;
            } else {
                if (end)
                    return false;
                queue.add(temp.getLeft());
                queue.add(temp.getRight());
            }
        }
        return true;
    }

    @Override
    public boolean isFull() {
        return isFullR(root);
    }

    private boolean isFullR(AVLNode<E> node) {
        if (node == null)
            return true;
        if (node.left() == null && node.right() == null)
            return true;
        if (node.left() != null && node.right() != null)
            return isFullR(node.left()) && isFullR(node.right());
        return false;
    }

    @Override
    public Tree<E> getSubtree(edu.co.diegoxs96.structures.model.node.AbstractNode<E> root) {
        return null;
    }

    @Override
    public Tree<E> getLeftSubtree(edu.co.diegoxs96.structures.model.node.AbstractNode<E> root) {
        return null;
    }

    @Override
    public Tree<E> getRightSubtree(edu.co.diegoxs96.structures.model.node.AbstractNode<E> root) {
        return null;
    }
}