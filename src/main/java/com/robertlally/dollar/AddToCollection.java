package com.robertlally.dollar;

import java.util.Collection;

class AddToCollection<T, C extends Collection> implements Fn2<C, T> {
    public C apply(C collection, T item) {
        //noinspection unchecked
        collection.add(item);

        return collection;
    }
}
