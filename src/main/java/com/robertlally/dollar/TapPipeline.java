package com.robertlally.dollar;

class TapPipeline<T> implements Pipeline<T> {
    private final Pipeline<T> pipeline;
    private final Do action;

    TapPipeline(Pipeline<T> pipeline, Do<T> action) {
        this.pipeline = pipeline;
        this.action = action;
    }

    public Option<T> pull() {
        Option<T> next = pipeline.pull();

        if(!next.isAbsent())
            action.apply(next.value());

        return next;
    }
}
