package com.robertlally.dollar;

class DropPipeline<T> implements Pipeline<T> {
    private final int discard;
    private final Pipeline<T> pipeline;
    private int discarded = 0;

    DropPipeline(Pipeline<T> pipeline, int discard) {
        this.discard = discard;
        this.pipeline = pipeline;
    }

    public Option<T> pull() {
        while(discarded < discard)
            discarded = pipeline.pull().isAbsent() ? discard : discarded + 1;

        return pipeline.pull();
    }
}
