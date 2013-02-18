package com.robertlally.dollar;

import java.util.*;

class PipelineIterator<T> implements Iterator<T> {
    private final Pipeline<T> pipeline;

    private boolean isHoldingNextValue = false;
    private T nextValue;

    PipelineIterator(Pipeline<T> pipeline) {
        this.pipeline = pipeline;
    }

    public boolean hasNext() {
        if(isHoldingNextValue) return true;

        Option<T> next = pipeline.pull();

        if(next.isAbsent()) return false;

        isHoldingNextValue = true;
        nextValue = next.value();

        return true;
    }

    public T next() {
        if(isHoldingNextValue) {
            isHoldingNextValue = false;
            return nextValue;
        }
        Option<T> next = pipeline.pull();

        if(next.isAbsent()) throw new NoSuchElementException();
        return next.value();
    }

    public void remove() {
        throw new UnsupportedOperationException("remove operation not supported");
    }
}
