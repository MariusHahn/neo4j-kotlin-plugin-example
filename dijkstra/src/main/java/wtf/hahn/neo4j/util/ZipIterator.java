package wtf.hahn.neo4j.util;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class ZipIterator<T> implements Iterator<T>, Iterable<T> {

    private final Iterator<? extends T> first;
    private final Iterator<? extends T> second;
    boolean flip = true;

    public ZipIterator(List<? extends T> first, List<? extends T> second) {
        this.first = first.iterator();
        this.second = second.iterator();
    }

    @Override
    public Iterator<T> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        return first.hasNext() || second.hasNext();
    }

    @Override
    public T next() {
        if (!hasNext()) throw new NoSuchElementException();
        if (flip) {
            flip = false;
            return first.hasNext() ? first.next() : next();
        } else {
            flip = true;
            return second.hasNext() ? second.next() : next();
        }
    }
}
