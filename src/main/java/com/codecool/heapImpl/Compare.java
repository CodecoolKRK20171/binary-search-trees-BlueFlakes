package com.codecool.heapImpl;

import java.util.Comparator;

public class Compare<T> {
    private Comparator<T> comparator;
    
    public Compare(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    public Boolean isLower(T n1, T n2) {
        return this.comparator.compare(n1, n2) < 0;
    }

    public Boolean isHigher(T n1, T n2) {
        return this.comparator.compare(n1, n2) > 0;
    }

    public Boolean isEqual(T n1, T n2) {
        return this.comparator.compare(n1, n2) == 0;
    }

    public Boolean isLowerOrEqual(T n1, T n2) {
        return this.comparator.compare(n1, n2) <= 0;
    }

    public Boolean isHigherOrEqual(T n1, T n2) {
        return this.comparator.compare(n1, n2) >= 0;
    }
}

