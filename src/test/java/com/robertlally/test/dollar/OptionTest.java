package com.robertlally.test.dollar;

import com.robertlally.dollar.Option;
import org.junit.Test;
import static com.robertlally.dollar.Dollar.$;
import static com.robertlally.dollar.Option.*;
import static java.awt.Color.CYAN;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class OptionTest {
    @Test public void noneIsAbsent() {
        assertThat(none().isAbsent(), is(true));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void noneThrowsExceptionIfValueAccessed() {
        none().value();
    }

    @Test public void someIsNotAbsent() {
        assertThat(some("blah").isAbsent(), is(false));
    }

    @Test public void someReturnsValuePassedToConstructor() {
        assertThat(some("blah").value(), is("blah"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void someThrowsAnExceptionIfFedNull() {
        some(null);
    }

    @Test public void optionNoneIsIterableButHasNoValues() {
        assertEquals(
                asList(),
                $(Option.none()).asIterable());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void optionNoneThrowsExceptionOnIteratorRemove() {
        none().iterator().remove();
    }

    @Test public void optionSomeIsIterableWithSingleValue() {
        assertEquals(
                asList("cheese"),
                $(Option.some("cheese")).asIterable());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void optionSomeThrowsExceptionOnIteratorRemove() {
        some(CYAN).iterator().remove();
    }

    @Test public void noneIsEqualToNone() {
        assertThat(none(), is(none()));
        assertThat(none().hashCode(), is(none().hashCode()));
    }

    @Test public void someDelegatesEqualityAndHashCodeToUnderlying() {
        assertThat(some("abc"), is(some("abc")));
        assertThat(some("abc"), is(not(some("def"))));

        assertThat(some("abc").hashCode(), is(some("abc").hashCode()));
        assertThat(some("abc").hashCode(), is("abc".hashCode()));
    }
}
