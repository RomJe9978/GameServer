package com.romje.model;

/**
 * 二元组封装
 *
 * @author liu xuan jie
 */
public class Tuple2<K, V> {

    private K first;

    private V second;

    private Tuple2(K first, V second) {
        this.first = first;
        this.second = second;
    }

    public static <K, V> Tuple2<K, V> of(K first, V second) {
        return new Tuple2<>(first, second);
    }

    public void setFirst(K first) {
        this.first = first;
    }

    public void setSecond(V second) {
        this.second = second;
    }

    public K first() {
        return this.first;
    }

    public V second() {
        return this.second;
    }
}