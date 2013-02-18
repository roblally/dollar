Dollar
======

Yet another attempt to create a fluent collections API for java using anonymous inner classes as pretend closures.

This library is slower, and will generate more garbage than native for loops, but also (I think) more expressive; YMMV.

It tries to be lazy by default and avoids creating intermediate collections wherever possible.

None of the operations modify the original collection or their contents.

It should be usable, perhaps even nice, and quite probably redundant, with Java 8 closures.


Usage
======

It turns out that, just as in javascript, $ is a valid Java identifier. In an homage to jQuery, I decided to see what it would be like to create a fluid collections API based upon $().

Take an iterable or an iterator and wrap it in $(), you can now chain methods on it e.g.

```java
Iterable<Integer> input = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);

Iterable<String> output =
$(stringInput)
    .filter(new Predicate<Integer>() {
        public boolean apply(Integer i) {
            return i < 5;
        }
    })
    .unique()
    .map(Fn<String, Integer>() {
        public Integer apply(String s) {
                return s.length();
        }
    })
    .asIterable()
```

To Do
======

Perform some sort of performance analysis so that it is clear exactly how much less efficient it is than a for loop.

sort
sortBy
groupBy
flatMap
takeWhile
takeUntil

slice
range
last

tee

as -> less common collections

generators

And then do everything again for Map....

parallel versions?

wrappers for streams, readers and other useful things


