package com.jay.beanmatcher;

import com.jay.beanmatcher.exception.BeanMatcherException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;

import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

/**
 * @author jaycarey
 */
public class FieldComparatorFactoryTest {

    private FieldComparatorFactory fieldComparatorFactory;

    @Mock
    private ReflectionUtils mockReflectionUtils;

    private Object testObject1;

    private Object testObject2;

    private Object testObject3;

    @Before
    public void before() throws Exception {
        MockitoAnnotations.initMocks(this);

        fieldComparatorFactory = new FieldComparatorFactory(mockReflectionUtils);
        when(mockReflectionUtils.fieldTypeHasEqualsMethod(Error.class)).thenReturn(false);

        testObject1 = new Object();
        testObject2 = new Object();
        testObject3 = new Object();
    }

    @Test
    public void canCreateSimpleEqualsFieldMatcher() throws Exception {

        Field field = TestObject.class.getDeclaredField("text");
        when(mockReflectionUtils.fieldTypeHasEqualsMethod(String.class)).thenReturn(true);

        FieldComparatorFactory.FieldComparator fieldComparator = fieldComparatorFactory.forAccessibleField(field);

        // Object 1 and 2 are the same.
        when(mockReflectionUtils.getValue(field, testObject1)).thenReturn("test");
        when(mockReflectionUtils.getValue(field, testObject2)).thenReturn("test");
        assertThat(fieldComparator.areSame(testObject1, testObject2), is(true));

        // Object 3 is different.
        when(mockReflectionUtils.getValue(field, testObject3)).thenReturn("different");
        assertThat(fieldComparator.areSame(testObject1, testObject3), is(false));
        assertThat(fieldComparator.describe(testObject1, testObject3), is("text[test!=different]"));
    }

    @Test
    public void canCreateEqualsMatcherForPrimitives() throws Exception {

        Field field = TestObject.class.getDeclaredField("integer");

        FieldComparatorFactory.FieldComparator fieldComparator = fieldComparatorFactory.forAccessibleField(field);

        // Object 1 and 2 are the same.
        when(mockReflectionUtils.getValue(field, testObject1)).thenReturn(123);
        when(mockReflectionUtils.getValue(field, testObject2)).thenReturn(123);
        assertThat(fieldComparator.areSame(testObject1, testObject2), is(true));

        // Object 3 is different.
        when(mockReflectionUtils.getValue(field, testObject3)).thenReturn(321);
        assertThat(fieldComparator.areSame(testObject1, testObject3), is(false));
        assertThat(fieldComparator.describe(testObject1, testObject3), is("integer[123!=321]"));
    }

    @Test
    public void throwsExceptionForUnknownType() throws Exception {

        try {
            Field mockField = FieldComparatorFactoryTest.class.getDeclaredField("fieldComparatorFactory");
            fieldComparatorFactory.forAccessibleField(mockField);
            fail("Should throw an exception due to unknown type.");
        } catch (BeanMatcherException e) {
            assertThat(e.getMessage(), equalTo(format("Unable to compare field of type [%s]", FieldComparatorFactory.class)));
        }
    }
}
