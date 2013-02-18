package com.robertlally.dollar;

class FilterPipeline<T> implements Pipeline<T> {
    private final Predicate<T> predicate;
    private final Pipeline<T> pipeline;

    FilterPipeline(Pipeline<T> pipeline, Predicate<T> predicate) {
        this.predicate = predicate;
        this.pipeline = pipeline;
    }

    public Option<T> pull() {
        Option<T> next;

        while(!(next = pipeline.pull()).isAbsent())
            if(predicate.apply(next.value()))
                break;

        return next;
    }
}
