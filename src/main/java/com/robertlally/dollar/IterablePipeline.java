package com.robertlally.dollar;

class IterablePipeline<T> implements Pipeline<T> {
    private final IteratorPipeline<T> delegate;

    IterablePipeline(Iterable<T> iterable) {
        this.delegate = new IteratorPipeline<>((iterable == null) ? null : iterable.iterator());
    }

    public Option<T> pull() {
        return delegate.pull();
    }
}
