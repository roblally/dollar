package com.robertlally.dollar;

class MapPipeline<T, T2> implements Pipeline<T2> {
    private final Pipeline<T> pipeline;
    private final Fn<T, T2> fn;

    MapPipeline(Pipeline<T> pipeline, Fn<T, T2> fn) {
        this.pipeline = pipeline;
        this.fn = fn;
    }

    public Option<T2> pull() {
        Option<T> option = pipeline.pull();

        if(option.isAbsent()) return Option.none();

        return Option.some(fn.apply(option.value()));
    }
}
