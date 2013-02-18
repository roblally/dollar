package com.robertlally.dollar;

import java.util.*;

class UniquePipeline<T> implements Pipeline<T> {
    private final Set<T> seenItems = new HashSet<>();

    private final Pipeline<T> pipeline;

    UniquePipeline(Pipeline<T> pipeline) {
        this.pipeline = pipeline;
    }

    public Option<T> pull() {
        Option<T> next;

        while(!(next = pipeline.pull()).isAbsent())
            if(seenItems.add(next.value()))
                break;

        return next;
    }
}
