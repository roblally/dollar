package com.robertlally.dollar;

class TakePipeline<T> implements Pipeline<T> {
    private final int required;
    private final Pipeline<T> pipeline;
    private int taken = 0;

    TakePipeline(Pipeline<T> pipeline, int required) {
        this.required = required;
        this.pipeline = pipeline;
    }

    public Option<T> pull() {
        if(taken == required) return Option.none();

        taken++;
        return pipeline.pull();
    }
}
