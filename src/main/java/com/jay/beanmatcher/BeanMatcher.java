package com.jay.beanmatcher;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author jaycarey
 */
public class BeanMatcher<T> extends BaseMatcher<T> {

    private final Object expected;

    private final Collection<FieldComparatorFactory.FieldComparator> fieldComparators;

    private final FieldComparatorFactory fieldComparatorFactory;

    private List<String> problems;

    private BeanMatcher(Object expected) {
        this.expected = expected;
        fieldComparators = new ArrayList<>();
        fieldComparatorFactory = new FieldComparatorFactory(new ReflectionUtils());
        problems = new ArrayList<>();
    }

    public static <T> BeanMatcher<T> comparesTo(final T expected) {
        return new BeanMatcher<>(expected);
    }

    @Override
    public boolean matches(Object o) {
        if (o == null) {
            problems.add("not a null value");
            return false;
        }
        return expected != null && compareFields(o);
    }

    private boolean compareFields(Object o) {
        boolean matches = true;
        for (FieldComparatorFactory.FieldComparator fieldComparator : fieldComparators) {
            if (!fieldComparator.areSame(expected, o)) {
                matches = false;
                problems.add(fieldComparator.describe(expected, o));
            }
        }
        return matches;
    }

    public BeanMatcher<T> onAllFields() {
        for (Field field : expected.getClass().getDeclaredFields()) {
            fieldComparators.add(fieldComparatorFactory.forAccessibleField(field));
        }
        return this;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(StringUtils.join(this.problems, ", "));
    }

}
