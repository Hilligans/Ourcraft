package dev.hilligans.engine.util;

public class DaisyChain<T> {

    Node<T> headNode = new Node<>(null);
    Node<T> tailNode = headNode;

    public boolean complete = false;

    public boolean isComplete() {
        return complete;
    }

    public boolean isEmpty() {
        return headNode == tailNode;
    }

    public synchronized T get() {
        if(tailNode != headNode) {
            Node<T> node = tailNode.next;
            tailNode = node;
            return node.element;
        }
        return null;
    }

    public synchronized void add(T element) {
        Node<T> nextNode = new Node<>(element);
        headNode.next = nextNode;
        headNode = nextNode;
    }

    static class Node<T> {
        public T element;
        public Node<T> next;

        public Node(T element) {
            this.element = element;
        }
    }
}
