package com.jay.beanmatcher;

import com.jay.beanmatcher.exception.BeanMatcherException;

import java.lang.reflect.Field;

import static java.lang.String.format;

/**
 * @author jaycarey
 */
public class FieldComparatorFactory {

    private final ReflectionUtils reflectionUtils;

    public FieldComparatorFactory(ReflectionUtils reflectionUtils) {
        this.reflectionUtils = reflectionUtils;
    }

    public FieldComparator forAccessibleField(Field field) {
        field.setAccessible(true);

        // We know that all boxed primitives have equals methods.
        if (field.getType().isPrimitive()) {
            return createEqualsComparator(field);

        } else if (reflectionUtils.fieldTypeHasEqualsMethod(field.getType())) {
            return createEqualsComparator(field);
        }

        throw new BeanMatcherException(format("Unable to compare field of type [%s]", field.getType()));
    }

    private FieldComparator createEqualsComparator(final Field field) {
        return new FieldComparator(reflectionUtils, field) {
            @Override
            public Boolean areSame(Object one, Object other) {
                Object oneValue = reflectionUtils.getValue(field, one);
                Object otherValue = reflectionUtils.getValue(field, other);
                if (oneValue == null) return otherValue == null;
                return oneValue.equals(otherValue);
            }
        };
    }

    /**
     * @author jaycarey
     */
    public abstract static class FieldComparator {

        private final ReflectionUtils reflectionUtils;

        private final Field field;

        public FieldComparator(ReflectionUtils reflectionUtils, Field field) {
            this.reflectionUtils = reflectionUtils;
            this.field = field;
        }

        public abstract Boolean areSame(Object one, Object other);

        public String describe(Object one, Object other) {
            Object oneValue = reflectionUtils.getValue(field, one);
            Object otherValue = reflectionUtils.getValue(field, other);
            return format("%s[%s!=%s]", field.getName(), oneValue, otherValue);
        }
    }
}

