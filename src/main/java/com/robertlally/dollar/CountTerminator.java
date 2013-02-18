package com.robertlally.dollar;

class CountTerminator<T> {
    private final Pipeline<T> pipeline;

    CountTerminator(Pipeline<T> pipeline) {
        this.pipeline = pipeline;
    }

    int extract() {
        int count = 0;

        while(!pipeline.pull().isAbsent())
            count++;

        return count;
    }
}
