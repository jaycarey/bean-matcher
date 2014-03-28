package com.jay.beanmatcher;

import com.jay.beanmatcher.exception.BeanMatcherException;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

import static junit.framework.Assert.fail;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * @author jaycarey
 */
public class ReflectionUtilsTest {


    private ReflectionUtils reflectionUtils;

    private Field integerField;

    private Field stringField;

    @Before
    public void before() throws Exception {
        reflectionUtils = new ReflectionUtils();

        integerField = TestObject.class.getDeclaredField("integer");
        integerField.setAccessible(true);
        stringField = TestObject.class.getDeclaredField("text");
        stringField.setAccessible(true);
    }

    @Test
    public void hasEqualsMethod() throws Exception {

        // String does have "equals".
        assertThat(reflectionUtils.fieldTypeHasEqualsMethod(integerField.getType()), is(false));

        // int itself doesn't have "equals".
        assertThat(reflectionUtils.fieldTypeHasEqualsMethod(stringField.getType()), is(true));
    }

    @Test
    public void canGetValue() throws Exception {
        TestObject testObject = new TestObject("text", 123);

        assertThat(reflectionUtils.getValue(integerField, testObject), equalTo((Object) 123));
        assertThat(reflectionUtils.getValue(stringField, testObject), equalTo((Object) "text"));
    }

    @Test
    public void getValueOnNullObjectReturnsNull() throws Exception {

        assertThat(reflectionUtils.getValue(integerField, null), equalTo(null));
    }

    @Test
    public void beanMatcherExceptionWhenGettingNonAccessibleField() throws Exception {
        TestObject testObject = new TestObject("text", 123);
        Field notAccessibleIntegerField = TestObject.class.getDeclaredField("integer");

        try {
            reflectionUtils.getValue(notAccessibleIntegerField, testObject);
            fail("Should cause an error and be wrapped in BeanMatcherException with nice message.");

        } catch (BeanMatcherException e) {
            assertThat(e.getMessage(), equalTo("Unable to get value from field [private final int com.jay.beanmatcher.TestObject.integer]"));
        }
    }
}
