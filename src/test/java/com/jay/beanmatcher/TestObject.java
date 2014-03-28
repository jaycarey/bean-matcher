package com.jay.beanmatcher;

public class TestObject {

    private final String text;

    private final int integer;

    TestObject(String text, int integer) {
        this.text = text;
        this.integer = integer;
    }

    public String getText() {
        return text;
    }

    public int getInteger() {
        return integer;
    }
}
