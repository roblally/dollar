package com.robertlally.test.dollar;

import com.robertlally.dollar.*;
import java.awt.Color;
import java.util.*;
import org.junit.*;
import static com.robertlally.dollar.Dollar.$;
import static com.robertlally.dollar.Option.*;
import static java.awt.Color.*;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

public class DollarTest {
    private Iterable<String> stringInput;
    private Iterable<Color> colourInput;
    private Iterable<Integer> integerInput;
    private Iterable<Integer> emptyIntegerInput;
    private Iterable<String> emptyStringInput;

    private Fn<String, Integer> lengthOfString;

    private Predicate<String> isGhi;
    private Predicate<String> isDef;
    private Predicate<String> throwIfCalled;
    private Predicate<String> neverMatch;
    private Predicate<Integer> integerGreaterThan100;
    private Predicate<Integer> integerGreaterThan1;
    private Predicate<Integer> integerGreaterThan0;

    @Before
    public void setUp() throws Exception {
        stringInput = asList("abc1", "def", "ghi");

        colourInput = asList(GRAY, GREEN, BLUE, YELLOW, PINK);

        integerInput = asList(1, 2, 3);

        emptyIntegerInput = asList();

        emptyStringInput = asList();

        isDef = new Predicate<String>() {
            public boolean apply(String s) {
                return !s.equals("def");
            }
        };

        isGhi = new Predicate<String>() {
            public boolean apply(String s) {
                return !s.equals("ghi");
            }
        };

        throwIfCalled = new Predicate<String>() {
            public boolean apply(String s) {
                throw new RuntimeException("This predicate should never be evaluated");
            }
        };

        lengthOfString = new Fn<String, Integer>() {
            public Integer apply(String s) {
                return s.length();
            }
        };

        neverMatch = new Predicate<String>() {
            public boolean apply(String s) {
                return false;
            }
        };

        integerGreaterThan100 = integerGreaterThan(100);

        integerGreaterThan1 = integerGreaterThan(1);

        integerGreaterThan0 = integerGreaterThan(0);
    }

    private Predicate<Integer> integerGreaterThan(final int target) {
        return new Predicate<Integer>() {
            public boolean apply(Integer i) {
                return i > target;
            }
        };
    }

    @Test public void getUnmodifiedCollectionReturnsEquivalentCollection() {
        assertEquals(stringInput, $(stringInput).asIterable());
    }

    @Test public void getUnmodifiedCollectionDoesNotReturnOriginalCollection() {
        assertNotSame(stringInput, $(stringInput).asIterable());
    }

    @Test public void nullInputReturnsEmptyCollection() {
        assertEquals(emptyStringInput, $((Iterable<String>)null).asIterable());
    }

    @Test public void filterCollection() {
        assertEquals(
                asList("abc1", "def"),
                $(stringInput)
                        .filter(isGhi)
                        .asIterable());
    }

    @Test public void filterCollectionRepeatedly() {
        assertEquals(
                asList("abc1"),
                $(stringInput)
                        .filter(isDef)
                        .filter(isGhi)
                        .asIterable());
    }

    @Test public void operationsNotEvaluatedIfGetNotCalled() {
        $(stringInput).filter(throwIfCalled);
    }

    @Test public void filterPredicatesNotCalledIfExcludedByPreviousPredicate() {
        $(stringInput)
                .filter(neverMatch)
                .filter(throwIfCalled)
                .asIterable();
    }

    @Test public void takeReturnsCorrectNumberOfItems() {
        assertEquals(
                asList("abc1", "def"),
                $(stringInput)
                        .take(2)
                        .asIterable());
    }

    @Test public void takeOfMoreThanAvailableReturnsWhatItCan() {
        assertEquals(
                asList("abc1", "def", "ghi"),
                $(stringInput)
                        .take(1000)
                        .asIterable());
    }

    @Test public void takeCanBeCombinedWithFilter() {
        assertEquals(
                asList("def", "ghi"),
                $(stringInput)
                        .filter(new Predicate<String>() {
                            public boolean apply(String s) {
                                return !"abc1".equals(s);
                            }
                        })
                        .take(2)
                        .asIterable());
    }

    @Test public void mapTransformsInput() {
        assertEquals(
                asList(4, 3, 3),
                $(stringInput)
                        .map(lengthOfString)
                        .asIterable());
    }

    @Test public void mapTransformsCanBeCombinedWithFilters() {
        assertEquals(
                asList(3, 3),
                $(stringInput)
                        .map(lengthOfString)
                        .filter(new Predicate<Integer>() {
                            public boolean apply(Integer i) {
                                return i < 4;
                            }
                        })
                        .asIterable());
    }

    @Test public void dropDiscardsValues() {
        assertEquals(
                asList(BLUE, YELLOW, PINK),
                $(colourInput)
                        .drop(2)
                        .asIterable());
    }

    @Test public void dropOfMoreThanAvailableDropsAll() {
        //noinspection AssertEqualsBetweenInconvertibleTypes
        assertEquals(
                emptyStringInput,
                $(colourInput)
                        .drop(1000)
                        .asIterable());
    }

    @Test public void findValueThatIsPresentReturnsFirstPossibleValue() {
        assertEquals(
                some(2),
                $(integerInput)
                        .find(integerGreaterThan1)
        );
    }

    @Test public void findReturnsNoneIfEmptyCollection() {
        //noinspection AssertEqualsBetweenInconvertibleTypes
        assertEquals(
                none(),
                $(emptyIntegerInput)
                        .find(integerGreaterThan1)
        );
    }

    @Test public void findValueThatIsNotPresentReturnsNone() {
//        noinspection AssertEqualsBetweenInconvertibleTypes
        assertEquals(
                none(),
                $(integerInput)
                        .find(integerGreaterThan100)
        );
    }

    @Test public void anyReturnsTrueIfPredicateMatches() {
        assertTrue($(integerInput).any(integerGreaterThan1));
    }

    @Test public void anyReturnsFalseIfPredicateDoesNotMatch() {
        assertFalse($(integerInput).any(integerGreaterThan100));
    }

    @Test public void anyReturnsFalseIfEmptyCollection() {
        assertFalse($(emptyIntegerInput).any(integerGreaterThan1));
    }

    @Test public void allReturnsTrueIfAllMatch() {
        assertTrue($(integerInput).all(integerGreaterThan0));
    }

    @Test public void allReturnsFalseIfAnyDoNotMatch() {
        assertFalse($(integerInput).all(integerGreaterThan1));
    }

    @Test public void allReturnsTrueIfInputIsEmptyCollection() {
        assertTrue($(emptyIntegerInput).all(integerGreaterThan1));
    }

    @Test public void noneReturnsTrueIfPredicateDoesNotMatch() {
        assertTrue($(integerInput).none(integerGreaterThan100));
    }

    @Test public void noneReturnsFalseIfPredicateMatchesAny() {
        assertFalse($(integerInput).none(integerGreaterThan1));
    }

    @Test public void noneReturnsTrueIfInputEmpty() {
        assertTrue($(emptyIntegerInput).none(integerGreaterThan100));
    }

    @Test public void reduceWithSameTypeForAccumulatorAndEach() {
        assertEquals(
                new Integer(16),
                $(integerInput).reduce(10, new Fn2<Integer, Integer>() {
                    public Integer apply(Integer accumulator, Integer item) {
                        return accumulator + item;
                    }
                })
        );
    }

    @Test public void reduceWithDifferentTypesForAccumulatorAndEach() {
        assertEquals(
                asList(2, 4, 6),
                $(integerInput).reduce(new ArrayList<Integer>(), new Fn2<List<Integer>, Integer>() {
                    public List<Integer> apply(List<Integer> accumulator, Integer item) {
                        accumulator.add(item * 2);

                        return accumulator;
                    }
                })
        );
    }

    @Test public void readingAsIteratorCanProduceElementsWithoutCallingHasNext() {
        Iterator<Integer> iterator = $(integerInput).asIterator();

        assertEquals(new Integer(1), iterator.next());
        assertEquals(new Integer(2), iterator.next());
        assertEquals(new Integer(3), iterator.next());
    }

    @Test public void readingAsIteratorNextValueDoesNotChangeUntilNextIsCalled() {
        Iterator<Integer> iterator = $(integerInput).asIterator();

        for(int i = 0; i < 10; i++)
            assertTrue(iterator.hasNext());
    }

    @Test public void readingAsIteratorHasNextThenNextSequenceWorks() {
        Iterator<Integer> iterator = $(integerInput).asIterator();

        assertTrue(iterator.hasNext());
        assertEquals(new Integer(1), iterator.next());

        assertTrue(iterator.hasNext());
        assertEquals(new Integer(2), iterator.next());

        assertTrue(iterator.hasNext());
        assertEquals(new Integer(3), iterator.next());

        assertFalse(iterator.hasNext());
    }

    @Test(expected = NoSuchElementException.class)
    public void readingOfTheEndOfIteratorProducesNoSuchElementException() {
        Iterator<Integer> iterator = $(integerInput).asIterator();

        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
    }

    @Test public void resultCanBeExtractedAsCollection() {
        assertThat($(integerInput).asCollection(), instanceOf(Collection.class));

        assertEquals(stringInput, $(stringInput).asCollection());
    }

    @Test public void resultCanBeExtractedAsList() {
        assertThat($(integerInput).asList(), instanceOf(List.class));

        assertEquals(stringInput, $(stringInput).asList());
    }

    @Test public void resultCanBeExtractedAsArrayList() {
        assertThat($(integerInput).asArrayList(), instanceOf(ArrayList.class));

        assertEquals(stringInput, $(stringInput).asArrayList());
    }

    @Test public void resultCanBeExtractedAsLinkedList() {
        assertThat($(integerInput).asLinkedList(), instanceOf(LinkedList.class));

        assertEquals(stringInput, $(stringInput).asLinkedList());
    }

    @Test public void resultCanBeExtractedAsSet() {
        assertThat($(integerInput).asSet(), instanceOf(Set.class));

        Collection<String> stringInputAsSet = $(stringInput).asCollection();

        assertTrue($(stringInput).asSet().containsAll(stringInputAsSet));
        assertTrue(stringInputAsSet.containsAll($(stringInput).asSet()));
    }

    @Test public void resultCanBeExtractedAsHashSet() {
        assertThat($(integerInput).asHashSet(), instanceOf(HashSet.class));

        Collection<String> stringInputAsSet = $(stringInput).asCollection();

        assertTrue($(stringInput).asHashSet().containsAll(stringInputAsSet));
        assertTrue(stringInputAsSet.containsAll($(stringInput).asHashSet()));
    }

    @Test public void dollarCanBeBackedWithIterator() {
        assertEquals(stringInput, $(stringInput.iterator()).asList());
    }

    @Test public void tapViewsEachItemAsItPasses() {
        final List<Integer> tap1Collection = new ArrayList<>();
        final List<Integer> tap2Collection = new ArrayList<>();

        $(integerInput)
                .tap(new Do<Integer>() {
                    public void apply(Integer i) {
                        tap1Collection.add(i);
                    }
                })
                .filter(integerGreaterThan1)
                .tap(new Do<Integer>() {
                    public void apply(Integer i) {
                        tap2Collection.add(i);
                    }
                })
                .asIterable();

        assertEquals(asList(1, 2, 3), tap1Collection);
        assertEquals(asList(2, 3), tap2Collection);
    }

    @Test public void eachPullsCollectionThrough() {
        final List<Integer> processed = new ArrayList<>();

        $(integerInput)
                .filter(integerGreaterThan1)
                .each(new Do<Integer>() {
                    public void apply(Integer i) {
                        processed.add(i);
                    }
                });

        assertEquals(asList(2, 3), processed);
    }

    @Test public void countCountsAllTheThings() {
        assertEquals(
                2,
                $(integerInput)
                        .filter(integerGreaterThan1)
                        .count());

    }

    @Test public void rejectIsTheOppositeOfFilter() {
        assertEquals(
                asList(1),
                $(integerInput)
                        .reject(integerGreaterThan1)
                        .asIterable());

    }

    @Test public void uniqueDropsDuplicateItems() {
        assertEquals(
                asList(1, 2, 3),
                $(asList(1, 1, 2, 2, 1, 2, 3, 3, 1, 2, 3))
                        .unique()
                        .asIterable());
    }
}
