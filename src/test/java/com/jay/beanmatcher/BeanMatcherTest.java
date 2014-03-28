package com.jay.beanmatcher;

import org.junit.Before;
import org.junit.Test;

import static com.jay.beanmatcher.BeanMatcher.comparesTo;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;

/**
 * @author jaycarey
 */
public class BeanMatcherTest {

    private TestObject referenceObject;

    @Before
    public void before() throws Exception {
        referenceObject = new TestObject("string", 123);
    }

    @Test
    public void canCompareSimpleObject() throws Exception {

        assertThat(new TestObject("string", 123), comparesTo(referenceObject).onAllFields());
    }

    @Test
    public void detectsDifferentInt() throws Exception {

        try {
            assertThat(new TestObject("string", 321), comparesTo(referenceObject).onAllFields());
            fail("Should have detected difference");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), containsString("integer"));
            assertThat(e.getMessage(), containsString("123"));
            assertThat(e.getMessage(), containsString("321"));
        }
    }

    @Test
    public void detectsDifferentString() throws Exception {

        try {
            assertThat(new TestObject("different", 123), comparesTo(referenceObject).onAllFields());
            fail("Should have detected difference");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), containsString("text"));
            assertThat(e.getMessage(), containsString("string"));
            assertThat(e.getMessage(), containsString("different"));
        }
    }

    /**
     * Can't use the normal 'fail', as we are catching assertion errors!
     *
     * @param message The message for the exception.
     */
    public void fail(String message) {
        throw new RuntimeException(message);
    }
}
