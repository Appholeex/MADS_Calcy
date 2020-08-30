package com.appholeex.madscalcy.utils;

public enum Operators {
    MULTIPLICATION(3), ADDITION(2), DIVISION(1), SUBTRACTION(0);

    private int value;

    Operators(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
