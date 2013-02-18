package com.robertlally.dollar;

import java.util.Iterator;

class NullIterator<T> implements Iterator<T> {
    public boolean hasNext() {
        return false;
    }

    public T next() {
        return null;
    }

    public void remove() {
        throw new UnsupportedOperationException("remove operation not supported");
    }
}
