package com.robertlally.dollar;

import java.util.Iterator;

class IteratorPipeline<T> implements Pipeline<T> {
    private final Iterator<T> iterator;

    IteratorPipeline(Iterator<T> iterator) {
        this.iterator = (iterator == null) ? new NullIterator<T>() : iterator;
    }

    public Option<T> pull() {
        if(iterator.hasNext())
            return Option.some(iterator.next());
        else
            return Option.none();
    }
}
