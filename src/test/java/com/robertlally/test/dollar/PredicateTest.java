package com.robertlally.test.dollar;

import com.robertlally.dollar.Predicate;
import org.junit.*;
import static com.robertlally.dollar.Predicate.Util.reverse;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class PredicateTest {
    private Predicate<String> predicate;

    @Before public void setUp() throws Exception {
        predicate = new Predicate<String>() {
            public boolean apply(String s) {
                return s.length() > 5;
            }
        };
    }

    @Test public void reversePredicate() {
        assertThat(predicate.apply("abc"), is(false));
        assertThat(predicate.apply("1234567890"), is(true));

        Predicate<String> reversedPredicate = reverse(predicate);

        assertThat(reversedPredicate.apply("abc"), is(true));
        assertThat(reversedPredicate.apply("1234567890"), is(false));
    }
}
