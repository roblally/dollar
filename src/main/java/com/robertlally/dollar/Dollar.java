package com.robertlally.dollar;

import java.util.*;
import static com.robertlally.dollar.Predicate.Util.reverse;

/**
 * Methods on this class are either pipelines or terminators.
 * <p/>
 * Pipelines filter, transform or otherwise modify the elements of the collection.
 * <p/>
 * Terminators render the outcome of the pipeline into some usable format.
 */
public class Dollar<T> {
    public static <T> Dollar<T> $(Iterable<T> iterable) {
        return new Dollar<>(iterable);
    }

    public static <T> Dollar<T> $(Iterator<T> iterator) {
        return new Dollar<>(iterator);
    }

    private Pipeline<T> pipeline;

    private Dollar(Pipeline<T> pipeline) {
        this.pipeline = pipeline;
    }

    private Dollar(Iterable<T> iterable) {
        this(new IterablePipeline<>(iterable));
    }

    private Dollar(Iterator<T> iterator) {
        this(new IteratorPipeline<>(iterator));
    }

    public Dollar<T> filter(Predicate<T> predicate) {
        pipeline = new FilterPipeline<>(pipeline, predicate);

        return this;
    }

    public Dollar<T> reject(Predicate<T> predicate) {
        return filter(reverse(predicate));
    }

    public <T2> Dollar<T2> map(Fn<T, T2> fn) {
        return new Dollar<>(new MapPipeline<>(pipeline, fn));
    }

    public Dollar<T> take(int number) {
        pipeline = new TakePipeline<>(pipeline, number);

        return this;
    }

    public Dollar<T> drop(int number) {
        pipeline = new DropPipeline<>(pipeline, number);

        return this;
    }

    public Dollar<T> tap(Do<T> action) {
        pipeline = new TapPipeline<>(pipeline, action);

        return this;
    }

    /**
     * Lazy.
     * <p/>
     * Does not fully realize the underlying collection, but does hold a set of all items seen so .
     */
    public Dollar<T> unique() {
        pipeline = new UniquePipeline<>(pipeline);

        return this;
    }

    public <T2> T2 reduce(T2 accumulator, Fn2<T2, T> reducer) {
        return new ReduceTerminator<>(pipeline, accumulator, reducer).pull();
    }

    public Option<T> find(Predicate<T> predicate) {
        return new FilterPipeline<>(pipeline, predicate).pull();
    }

    public boolean none(Predicate<T> predicate) {
        return find(predicate).isAbsent();
    }

    public boolean any(Predicate<T> predicate) {
        return !none(predicate);
    }

    public boolean all(Predicate<T> predicate) {
        return find(reverse(predicate)).isAbsent();
    }

    public void each(Do<T> action) {
        new EachTerminator<>(pipeline, action).extract();
    }

    public int count() {
        return new CountTerminator<>(pipeline).extract();

    }

    /**
     * Not lazy.
     * <p/>
     * Underlying collection must be fully realized so that iterator() can be called multiple times.
     */
    public Iterable<T> asIterable() {
        return asList();
    }

    /**
     * Lazy.
     */
    public Iterator<T> asIterator() {
        return new PipelineIterator<>(pipeline);
    }

    public List<T> asList() {
        return asArrayList();
    }

    public Collection<T> asCollection() {
        return asArrayList();
    }

    public ArrayList<T> asArrayList() {
        return asCollection(new ArrayList<T>());
    }

    public LinkedList<T> asLinkedList() {
        return asCollection(new LinkedList<T>());
    }

    public Set<T> asSet() {
        return asHashSet();
    }

    public HashSet<T> asHashSet() {
        return asCollection(new HashSet<T>());
    }

    private <C extends Collection> C asCollection(C collector) {
        return reduce(collector, new AddToCollection<T, C>());
    }
}