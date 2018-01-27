package com.codecool.mapImpl;

class Node<K, V> {
    private final int hash;
    private final K key;
    private V value;
    private Node<K, V> nextNode;

    public Node(int hash, K key, V value) {
        this.hash = hash;
        this.key = key;
        this.value = value;
    }

    //region setters and getters
    //region setters

    public void setNextNode(Node<K, V> nextNode) {
        this.nextNode = nextNode;
    }

    //endregion
    //region getters

    public int getHash( ) {
        return hash;
    }

    public K getKey( ) {
        return key;
    }

    public V getValue( ) {
        return value;
    }

    public Node<K, V> getNextNode( ) {
        return nextNode;
    }

    //endregion
    //endregion
}
