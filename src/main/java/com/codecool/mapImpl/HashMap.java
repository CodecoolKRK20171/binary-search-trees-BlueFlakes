package com.codecool.mapImpl;

import java.util.Optional;
import java.util.stream.IntStream;

public class HashMap<K, V> {
    private static final Integer defaultCapacity = 16;
    private int capacity;
    private int size;
    private Node<K, V>[] container;

    public HashMap() {
        this(defaultCapacity);
    }

    public HashMap(int initialCapacity) {
        initializeInstance(initialCapacity);
    }

    @SuppressWarnings({"rawtypes","unchecked"})
    private void initializeInstance(int initialCapacity) {
        this.container = (Node<K, V>[]) new Node[initialCapacity];
        this.capacity = initialCapacity;
    }

    public void put(K key, V value) throws AlreadyOccupiedKeyException {
        checkWhetherKeyIsAlreadyOccupied(key);

        int hash = calculateHash(key);
        Node<K, V> newNode = new Node<>(hash, key, value);

        Optional<Node<K, V>> lastNode = findLastNode(hash);

        if (lastNode.isPresent()) {
            lastNode.get().setNextNode(newNode);
        } else {
            this.container[hash] = newNode;
        }

        this.size++;
        resizeIfNeeded();
    }

    private void checkWhetherKeyIsAlreadyOccupied(K key) throws AlreadyOccupiedKeyException {
        if (!isAbsent(key)) {
            throw new AlreadyOccupiedKeyException();
        }
    }

    private Optional<Node<K, V>> findLastNode(int hash) {
        Node<K, V> next = getFirstNode(hash);
        Node<K, V> lastNodeInRow = next;

        while (next != null) {
            lastNodeInRow = next;
            next = next.getNextNode();
        }

        return Optional.ofNullable(lastNodeInRow);
    }

    private boolean isAbsent(K key) {
        return !findNodeByHashAndKey(key).isPresent();
    }

    private void resizeIfNeeded() throws AlreadyOccupiedKeyException {
        if (this.size < this.capacity * 2) {
            return;
        }

        Node<K, V>[] actualContent = this.container;
        replaceContainerWithBiggerOne();
        copyActualContentToNewContainer(actualContent);
    }

    private void copyActualContentToNewContainer(Node<K, V>[] actualContent) throws AlreadyOccupiedKeyException {
        for (Node<K, V> node : actualContent) {
            Node<K, V> next = node;

            while (next != null) {
                this.put(next.getKey(), next.getValue());
                next = next.getNextNode();
            }
        }
    }

    @SuppressWarnings({"rawtypes","unchecked"})
    private void replaceContainerWithBiggerOne() {
        int increasedCapacity = this.capacity * 2;
        this.container = (Node<K, V>[]) new Node[increasedCapacity];
        this.capacity = increasedCapacity;
    }

    public void remove(K key) throws AbsentKeyException {
        int hash = calculateHash(key);
        Node<K, V> firstNodeInRow = getFirstNode(hash);

        if (firstNodeInRow != null) {
            if (isNodeEqual(key, hash, firstNodeInRow)) {
                this.container[hash] = firstNodeInRow.getNextNode();
                this.size--;
                return;
            }
            
            boolean wasSuccesfullyDeleted = handleRemovalIfNodeIsFurtherThanFirst(firstNodeInRow, key, hash);
            if (wasSuccesfullyDeleted)
                this.size--;
                return;
        }
        
        throw new AbsentKeyException();
    }

    private Node<K, V> getFirstNode(int hash) {
        return this.container[hash];
    }

    private boolean handleRemovalIfNodeIsFurtherThanFirst(Node<K, V> firstNodeInRow, K key, int hash) {
        Node<K, V> previousNode = firstNodeInRow;
        Node<K, V> currentNode = firstNodeInRow;

        while (currentNode != null) {
            if (isNodeEqual(key, hash, currentNode)) {
                previousNode.setNextNode(currentNode.getNextNode());
                return true;
            }

            previousNode = currentNode;
            currentNode = currentNode.getNextNode();
        }

        return false;
    }

    private boolean isNodeEqual(K key, int hash, Node<K, V> currentNode) {
        int nodeHash = currentNode.getHash();
        K nodeKey = currentNode.getKey();

        return hash == nodeHash && key.equals(nodeKey);
    }

    public void clearAll() {
        IntStream.range(0, this.container.length)
                 .forEach(idx -> this.container[idx] = null);
    }

    public V get(K key) throws AbsentKeyException {
        Optional<Node<K, V>> foundNode = findNodeByHashAndKey(key);

        return foundNode.map(Node::getValue)
                        .orElseThrow(AbsentKeyException::new);
    }

    private Optional<Node<K, V>> findNodeByHashAndKey(K key) {
        int hash = calculateHash(key);
        Node<K, V> next = getFirstNode(hash);

        while (next != null) {
            int nextHash = next.getHash();
            K nextKey = next.getKey();

            if (hash == nextHash && key.equals(nextKey)) {
                return Optional.of(next);
            }

            next = next.getNextNode();
        }

        return Optional.empty();
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public Integer size() {
        return this.size;
    }

    private int calculateHash(K key) {
        return key.hashCode() % this.container.length;
    }
}

