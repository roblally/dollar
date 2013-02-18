package com.robertlally.dollar;

import java.util.Iterator;

public abstract class Option<T> implements Iterable<T> {
    private static final Option<Object> NONE = new NoneOption();

    public static <T> Option<T> none() {
        //noinspection unchecked
        return (Option<T>)NONE;
    }

    public static <T> Option<T> some(final T value) {
        return new SomeOption<>(value);
    }

    //TODO Reverse this to make it positive - hasValue - rather than negative
    public abstract boolean isAbsent();

    public abstract T value();

    private static class SomeOption<T> extends Option<T> {
        private final T value;

        SomeOption(T value) {
            if(value == null) throw new IllegalArgumentException("value cannot be null");

            this.value = value;
        }

        public boolean isAbsent() {
            return false;
        }

        public T value() {
            return value;
        }

        public Iterator<T> iterator() {
            return new SingleValueIterator<>(value);
        }

        public int hashCode() {
            return value.hashCode();
        }

        public boolean equals(Object other) {
            return other.getClass() == getClass() && value.equals(((Option)other).value());
        }
    }

    private static class NoneOption extends Option<Object> {
        public boolean isAbsent() {
            return true;
        }

        public Object value() {
            throw new UnsupportedOperationException("NONE has no value");
        }

        public Iterator<Object> iterator() {
            return new NullIterator<>();
        }
    }
}
