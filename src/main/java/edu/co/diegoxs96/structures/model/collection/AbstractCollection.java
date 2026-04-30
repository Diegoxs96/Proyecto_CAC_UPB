package edu.co.diegoxs96.structures.model.collection;

import java.io.Serializable;
import java.util.function.Function;

import edu.co.diegoxs96.structures.model.iterator.Iterator;

/**
 * The Abstract Collection represents a collection that supports
 * collection-like operations.
 *
 * @param <E> the type of elements in the collection.
 *
 * @author julian benitorevollo bernal
 * @version 1.0.20240219
 */
public abstract class AbstractCollection<E> implements Collection<E>, Cloneable, Serializable {

    @Override
    public void forEach(Function<E, Void> action) {
        if (action == null)
            return;
        for (Iterator<E> it = iterator(); it.hasNext();) {
            action.apply(it.next());
        }
    }
}
