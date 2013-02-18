package com.robertlally.dollar;

class ReduceTerminator<T1, T2> {
    private final Pipeline<T1> pipeline;
    private final T2 accumulator;
    private final Fn2<T2, T1> reducer;

    ReduceTerminator(Pipeline<T1> pipeline, T2 accumulator, Fn2<T2, T1> reducer) {
        this.pipeline = pipeline;
        this.accumulator = accumulator;
        this.reducer = reducer;
    }

    T2 pull() {
        T2 result = accumulator;

        Option<T1> next;
        while(!(next = pipeline.pull()).isAbsent())
            result = reducer.apply(result, next.value());


        return result;
    }
}
