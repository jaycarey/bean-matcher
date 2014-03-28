package com.jay.beanmatcher;

import org.junit.Test;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author jaycarey
 */
public class StringUtilsTest {

    @Test
    public void canJoinStrings() throws Exception {
        assertThat(StringUtils.join(asList("one", "two"), ", "), equalTo("one, two"));
        assertThat(StringUtils.join(asList("a", "b", "c"), "-"), equalTo("a-b-c"));
    }
}
