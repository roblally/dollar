package com.robertlally.dollar;

class EachTerminator<T> {
    private final Pipeline<T> pipeline;
    private final Do<T> action;

    EachTerminator(Pipeline<T> pipeline, Do<T> action) {
        this.pipeline = pipeline;
        this.action = action;
    }

    void extract() {
        Option<T> next;

        while(!(next = pipeline.pull()).isAbsent())
            action.apply(next.value());
    }
}
