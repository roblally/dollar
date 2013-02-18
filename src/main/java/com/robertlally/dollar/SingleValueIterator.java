package com.robertlally.dollar;

import java.util.Iterator;

class SingleValueIterator<T> implements Iterator<T> {
    private final T value;
    private boolean hasNext = true;

    SingleValueIterator(T value) {
        this.value = value;
    }

    public boolean hasNext() {
        return hasNext;
    }

    public T next() {
        hasNext = false;
        return value;
    }

    public void remove() {
        throw new UnsupportedOperationException("remove operation unsupported");
    }
}
