import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

// pretty much the same as a normal doubly linked list except there is a tail node
// so both push and append are O(1)
public class DoublyLinkedList<T> implements Iterable<T> {
    private Node headNode, tailNode;

    public Node getHeadNode() {
        return headNode;
    }

    public Node getTailNode() {
        return tailNode;
    }

    private class Node{
        T data;

        Node previousNode;
        Node nextNode;

        Node(T data){
            setData(data);
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }

        public Node getPreviousNode() {
            return previousNode;
        }

        public void setPreviousNode(Node previousNode) {
            this.previousNode = previousNode;
        }
        
        public Node getNextNode() {
            return nextNode;
        }

        public void setNextNode(Node nextNode) {
            this.nextNode = nextNode;
        }
    }

    // push new node at the front of the list
    public void push(T newData){
        Node newNode = new Node(newData);
        newNode.setNextNode(headNode);
        newNode.setPreviousNode(null);

        // if the head was null, that means the tail needs to be updated
        if(headNode == null)
            tailNode = newNode;
        // else it's not the tail but the head's previous node needs to be updated
        else
            headNode.setPreviousNode(newNode);
        
        headNode = newNode;
    }

    // append new node at the back of the list
    // a mirror of push
    public void append(T newData){
        Node newNode = new Node(newData);
        newNode.setPreviousNode(tailNode);
        newNode.setNextNode(null);

        // if the tail was null, that means the head needs to be updated
        if(tailNode == null)
            headNode = newNode;
        // else it's not the head but the tail's next node needs to be updated
        else
            tailNode.setNextNode(newNode);
        
        tailNode = newNode;
    }

    public void deleteNode(Node toDelete){
        if(headNode == null || toDelete == null){
            System.out.println("Error. You can't delete a null node.");
        }
        if(headNode == toDelete){
            headNode = toDelete.nextNode;
        }
        if(tailNode == toDelete){
            tailNode = toDelete.previousNode;
        }
        if(toDelete.getNextNode() != null)
            toDelete.getNextNode().setPreviousNode(toDelete.getPreviousNode());
        if(toDelete.getPreviousNode() != null)
            toDelete.getPreviousNode().setNextNode(toDelete.getNextNode());
    }

    // assumes the existing list is an eulerian tour
    // returns the new number of items
    public int convertToHamiltonianCycle(){
        int newSize = deleteDuplicates();
        // it doesn't include the start vertex at the end of the cycle
        // append(headNode.data);
        // newSize++;
        return newSize;
    }

    // used to find TSP tour from Eulerian tour
    // returns the new number of items
    private int deleteDuplicates(){
        HashSet<T> set = new HashSet<>();
        Node currentNode = headNode;
        while(currentNode != null){
            T currentData = currentNode.data;
            if(set.contains(currentData))
                deleteNode(currentNode);// this doesn't affect the current node, just the list
            else
                set.add(currentData);
            currentNode = currentNode.getNextNode();
        }
        return set.size();
    }

    public void insertBefore(Node nextNode, T newData){
        if(nextNode == null){
            System.out.println("Error. You can't insert before null.");
            return;
        }
        Node newNode = new Node(newData);
        // set previous node for newNode
        newNode.setPreviousNode(nextNode.getPreviousNode());
        // update the previous node of nextNode
        nextNode.setPreviousNode(newNode);
        // set next node for newNode
        newNode.setNextNode(nextNode);
        // update the next node of newNode's previous node
        // if the previous node was null, then the new node was inserted at the head
        // then update the head
        if(newNode.getPreviousNode() == null)
            headNode = newNode;
        // else update the previous node's next node
        else
            newNode.getPreviousNode().setNextNode(newNode);
    }

    // a mirror of insertBefore
    public void insertAfter(Node previousNode, T newData){
        if(previousNode == null){
            System.out.println("Error. You can't insert after null.");
            return;
        }
        Node newNode = new Node(newData);
        // set next node for newNode
        newNode.setNextNode(previousNode.getNextNode());
        // update the next node of previousNode
        previousNode.setNextNode(newNode);
        // set previous node for newNode
        newNode.setPreviousNode(previousNode);
        // update the previous node of newNode's next node
        // if the next node was null, then the new node was inserted at the tail
        // then update the tail
        if(newNode.getNextNode() == null)
            tailNode = newNode;
        // else update the next node's previous node
        else
            newNode.getNextNode().setPreviousNode(newNode);
    }

    // for Hierholzer's Algorithm
    public void replaceNodeByList(T dataToReplace, DoublyLinkedList<T> list){
        Node toReplace = findNodeFromData(dataToReplace);
        if(toReplace == null){
            System.out.println("Error. There is no such item in the list.");
            return;
        }
        // update list's previous node
        list.headNode.setPreviousNode(toReplace.getPreviousNode());
        // if the previous node is null, then the current node is the head
        if(toReplace.getPreviousNode() == null)
            headNode = list.headNode;
        // else it's a non-head node, so update the previous node's next node
        else
            toReplace.getPreviousNode().setNextNode(list.headNode);
        // update list's next node
        list.tailNode.setNextNode(toReplace.getNextNode());
        // if the next node is null, then the current node is the tail
        if(toReplace.getNextNode() == null)
            tailNode = list.tailNode;
        // else it's a non-tail node, so update the next node's previous node
        else
            toReplace.getNextNode().setPreviousNode(list.tailNode);
    }

    // for Hierholzer's Algorithm... it's O(n)... ew
    public Node findNodeFromData(T needle){
        Node currentNode = headNode;
        while(currentNode != null){
            if(currentNode.data == needle)
                return currentNode;
            currentNode = currentNode.getNextNode();
        }
        return null;
    }

    public void printList(){
        System.out.println("Eulerian Path:");
        Node currentNode = headNode;
        while(currentNode != null){
            System.out.println("-> " + currentNode.getData().toString());
            currentNode = currentNode.getNextNode();
        }
    }

    public ArrayList<T> toArrayList(int quantity){
        Node currentNode = headNode;
        ArrayList<T> list = new ArrayList<>(quantity);
        while(currentNode != null){
            list.add(currentNode.getData());
            currentNode = currentNode.getNextNode();
        }
        return list;
    }

    @Override
    public Iterator<T> iterator() {
        return new ListIterator(this);
    }

    private class ListIterator implements Iterator<T>{
        DoublyLinkedList<T>.Node currentNode;

        public ListIterator(DoublyLinkedList<T> list) {
            currentNode = list.getHeadNode();
        }

        @Override
        public boolean hasNext() {
            return currentNode != null;
        }

        @Override
        public T next() {
            T currentData = currentNode.getData();
            currentNode = currentNode.getNextNode();
            return currentData;
        }
    }
}
