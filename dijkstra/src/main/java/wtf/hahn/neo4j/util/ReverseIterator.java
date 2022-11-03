package wtf.hahn.neo4j.util;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class ReverseIterator<T> implements Iterator<T>, Iterable<T> {

    private final List<? extends T> list;
    private int position;

    public ReverseIterator(List<? extends T> list) {
        this.list = list;
        this.position = list.size() - 1;
    }

    @Override
    public Iterator<T> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        return position >= 0;
    }

    @Override
    public T next() {
        if (!hasNext()) throw new NoSuchElementException();
        return list.get(position--);
    }
}
