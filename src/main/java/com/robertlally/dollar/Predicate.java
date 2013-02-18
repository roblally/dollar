package com.robertlally.dollar;

public interface Predicate<T> {
    boolean apply(T t);

    static class Util {
        public static <T> Predicate<T> reverse(final Predicate<T> predicate) {
            return new Predicate<T>() {
                public boolean apply(T t) {
                    return !predicate.apply(t);
                }
            };
        }
    }
}

