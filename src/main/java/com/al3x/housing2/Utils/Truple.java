package com.al3x.housing2.Utils;

/**
 * Represents a generic tuple with two elements of types T and E.
 * Provides methods to retrieve the first and second elements of the tuple.
 *
 * @param <T> the type of the first element
 * @param <E> the type of the second element
 */
public class Truple<T, E, B> {
    private T first;
    private E second;
    private B third;

    public Truple(T first, E second, B third) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public E getSecond() {
        return second;
    }

    public B getThird() {
        return third;
    }
}
