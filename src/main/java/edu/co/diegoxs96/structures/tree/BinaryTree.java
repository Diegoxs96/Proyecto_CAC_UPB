package edu.co.diegoxs96.structures.tree;

import java.util.Queue;

import edu.co.diegoxs96.structures.linkedlist.singly.LinkedList;
import edu.co.diegoxs96.structures.model.list.List;
import edu.co.diegoxs96.structures.model.node.Node;
import edu.co.diegoxs96.structures.model.search.SearchResult;
import edu.co.diegoxs96.structures.model.tree.AbstractTree;
import edu.co.diegoxs96.structures.model.tree.Tree;

public class BinaryTree<E> extends AbstractTree<E> {

    private Node<E> root;
    private int size;

    public BinaryTree() {
        root = null;
        size = 0;
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
    private Node<E> insertR(Node<E> node, E element) {
        if (node == null)
            return new Node<>(element);

        Comparable<E> cmp = (Comparable<E>) element;

        if (cmp.compareTo(node.get()) < 0) {
            node.setLeft(insertR(node.getLeft(), element));
        } else {
            node.setRight(insertR(node.getRight(), element));
        }

        return node;
    }

    @Override
    public boolean search(E element) {
        return searchR(root, element);
    }

    @SuppressWarnings("unchecked")
    private boolean searchR(Node<E> node, E element) {
        if (node == null)
            return false;

        Comparable<E> cmp = (Comparable<E>) element;
        int result = cmp.compareTo(node.get());

        if (result == 0)
            return true;
        if (result < 0)
            return searchR(node.getLeft(), element);
        else
            return searchR(node.getRight(), element);
    }

    public SearchResult searchWithMetrics(E element) {
        long start = System.nanoTime();
        int steps = 0;

        Node<E> current = root;

        @SuppressWarnings("unchecked")
        Comparable<E> cmpElement = (Comparable<E>) element;

        while (current != null) {
            steps++;

            int cmp = cmpElement.compareTo(current.get());

            if (cmp == 0) {
                long end = System.nanoTime();
                return new SearchResult(true, steps, end - start);
            }

            if (cmp < 0)
                current = current.getLeft();
            else
                current = current.getRight();
        }

        long end = System.nanoTime();
        return new SearchResult(false, steps, end - start);
    }

    public SearchResult searchByList(List<E> list, E target) {
        long start = System.nanoTime();
        int steps = 0;

        LinkedList<E> l = (LinkedList<E>) list;
        var current = l.getHead();

        while (current != null) {
            steps++;

            if (current.get().equals(target)) {
                long end = System.nanoTime();
                return new SearchResult(true, steps, end - start);
            }

            current = current.getNext();
        }

        long end = System.nanoTime();
        return new SearchResult(false, steps, end - start);
    }

    public void printTree() {
        printTree(root, "");
    }

    private void printTree(Node<E> node, String indent) {
        if (node == null)
            return;

        System.out.println(indent + "\"" + node.get() + "\"");

        if (node.getLeft() != null || node.getRight() != null) {
            System.out.println(indent + "  |");

            printTree(node.getLeft(), indent + "  -- ");
            printTree(node.getRight(), indent + "  -- ");
        }
    }

    @Override
    public List<E> preOrder() {
        List<E> list = new LinkedList<>();
        preOrderR(root, list);
        return list;
    }

    private void preOrderR(Node<E> node, List<E> list) {
        if (node == null)
            return;

        list.add(node.get());
        preOrderR(node.getLeft(), list);
        preOrderR(node.getRight(), list);
    }

    @Override
    public List<E> inOrder() {
        List<E> list = new LinkedList<>();
        inOrderR(root, list);
        return list;
    }

    private void inOrderR(Node<E> node, List<E> list) {
        if (node == null)
            return;

        inOrderR(node.getLeft(), list);
        list.add(node.get());
        inOrderR(node.getRight(), list);
    }

    @Override
    public List<E> postOrder() {
        List<E> list = new LinkedList<>();
        postOrderR(root, list);
        return list;
    }

    private void postOrderR(Node<E> node, List<E> list) {
        if (node == null)
            return;

        postOrderR(node.getLeft(), list);
        postOrderR(node.getRight(), list);
        list.add(node.get());
    }

    @Override
    public List<E> levelOrder() {
        List<E> list = new LinkedList<>();

        if (root == null)
            return list;

        Queue<Node<E>> queue = new java.util.LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            Node<E> temp = queue.poll();
            list.add(temp.get());

            if (temp.getLeft() != null)
                queue.add(temp.getLeft());
            if (temp.getRight() != null)
                queue.add(temp.getRight());
        }

        return list;
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
    public int getHeight() {
        return heightR(root);
    }

    private int heightR(Node<E> node) {
        if (node == null)
            return 0;
        return 1 + Math.max(heightR(node.getLeft()), heightR(node.getRight()));
    }

    @Override
    public int getGrade() {
        return 2;
    }

    @Override
    public double getLCI() {
        return lciR(root, 0);
    }

    private int lciR(Node<E> node, int level) {
        if (node == null)
            return 0;

        return level +
                lciR(node.getLeft(), level + 1) +
                lciR(node.getRight(), level + 1);
    }

    @Override
    public double getLCIM() {
        if (size == 0)
            return 0;
        return getLCI() / size;
    }

    @Override
    public boolean isFull() {
        return isFullR(root);
    }

    private boolean isFullR(Node<E> node) {
        if (node == null)
            return true;

        if (node.getLeft() == null && node.getRight() == null)
            return true;

        if (node.getLeft() != null && node.getRight() != null)
            return isFullR(node.getLeft()) && isFullR(node.getRight());

        return false;
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

    public int getLevels() {
        return getHeight();
    }

    @Override
    public boolean remove(E element) {
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