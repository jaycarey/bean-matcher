package com.jay.beanmatcher.exception;

/**
 * @author jaycarey
 */
public class BeanMatcherException extends RuntimeException {

    public BeanMatcherException(String message) {
        super(message);
    }

    public BeanMatcherException(String message, Throwable e) {
        super(message, e);
    }
}
