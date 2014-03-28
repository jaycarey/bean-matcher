package com.jay.beanmatcher;

import com.jay.beanmatcher.exception.BeanMatcherException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static java.lang.String.format;

public class ReflectionUtils {

    public boolean fieldTypeHasEqualsMethod(Class<?> type) {
        for (Method method : type.getDeclaredMethods()) {
            if (method.getName().equals("equals")) {
                return true;
            }
        }
        return false;
    }

    public Object getValue(Field field, Object object) {
        try {
            return object == null ? null : field.get(object);
        } catch (IllegalAccessException e) {
            throw new BeanMatcherException(format("Unable to get value from field [%s]", field), e);
        }
    }
}
