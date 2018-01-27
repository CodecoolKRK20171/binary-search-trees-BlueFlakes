package com.codecool.heapImpl;

import java.util.Arrays;
import java.util.Comparator;

public class Heap<T> {
    private int capacity = 10;
    private int size = 0;
    private Compare<T> compare;
    private T[] container;

    public Heap(Comparator<T> comparator) {
        container = (T[]) new Object[this.capacity];
        this.compare = new Compare<>(comparator);
    }

    //region accessors
    private int getLeftChildIndex(int parentIndex) {
        return 2 * parentIndex + 1;
    }

    private int getRightChildIndex(int parentIndex) {
        return 2 * parentIndex + 2;
    }

    private int getParentIndex(int childIndex) {
        return (childIndex - 1) / 2;
    }

    private boolean hasLeftChild(int index) {
        return getLeftChildIndex(index) < this.size;
    }

    private boolean hasRightChild(int index) {
        return getRightChildIndex(index) < this.size;
    }

    private boolean hasParent(int index) {
        return getParentIndex(index) >= 0;
    }

    private T leftChild(int index) {
        return this.container[getLeftChildIndex(index)];
    }

    private T rightChild(int index) {
        return this.container[getRightChildIndex(index)];
    }

    private T parent(int index) {
        return container[getParentIndex(index)];
    }
    //endregion

    private void swap(int firstIndex, int secondIndex) {
        T temp = container[firstIndex];
        container[firstIndex] = container[secondIndex];
        container[secondIndex] = temp;
    }

    public void ensureExtraCapacity() {
        if (this.size == this.capacity) {
            int increasedCapacity = this.capacity * 2;
            container = Arrays.copyOf(container, increasedCapacity);
            this.capacity = increasedCapacity;
        }
    }

    public T peek() {
        if (size == 0) throw new IllegalArgumentException();
        return container[0];
    }

    public T poll() {
        if (size == 0) throw new IllegalArgumentException();

        T item = container[0];
        container[0] = container[size - 1];
        size--;
        heapifyDown();
        return item;
    }

    private void heapifyDown( ) {
        int index = 0;

        while (hasLeftChild(index)) {
            int smallerChildIndex = getLeftChildIndex(index);
            if (hasRightChild(index) && this.compare.isLower(rightChild(index), leftChild(index))) {
                smallerChildIndex = getRightChildIndex(index);
            }

            if (this.compare.isLower(container[index], container[smallerChildIndex])) {
                break;
            } else {
                swap(index, smallerChildIndex);
            }

            index = smallerChildIndex;
        }
    }

    public void add(T item) {
        ensureExtraCapacity();
        container[size] = item;
        size++;
        heapifyUp();
    }

    private void heapifyUp( ) {
        int index = size - 1;
        while (hasParent(index) && this.compare.isHigher(parent(index), container[index])) {
            swap(getParentIndex(index), index);
            index = getParentIndex(index);
        }
    }
}