package com.al3x.housing2.Utils;

/**
 * Represents a generic tuple with two elements of types T and E.
 * Provides methods to retrieve the first and second elements of the tuple.
 *
 * @param <T> the type of the first element
 * @param <E> the type of the second element
 */
public class Duple<T, E> {
    private T first;
    private E second;

    public Duple(T first, E second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public E getSecond() {
        return second;
    }
}
