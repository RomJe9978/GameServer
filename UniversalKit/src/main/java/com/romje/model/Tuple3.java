package com.romje.model;

/**
 * 三元组封装
 *
 * @author liu xuan jie
 */
public class Tuple3<K, V, U> {

    private K first;

    private V second;

    private U third;

    private Tuple3(K first, V second, U third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public static <K, V, U> Tuple3<K, V, U> of(K first, V second, U third) {
        return new Tuple3<>(first, second, third);
    }

    public K first() {
        return first;
    }

    public void setFirst(K first) {
        this.first = first;
    }

    public V second() {
        return second;
    }

    public void setSecond(V second) {
        this.second = second;
    }

    public U third() {
        return third;
    }

    public void setThird(U third) {
        this.third = third;
    }
}