package edu.co.diegoxs96.structures.model.Queue;

public interface Queue<E> {

    E peek();

    E extract();

    E insert(E element);

}
