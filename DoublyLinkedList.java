package KCCRS;

class DLLNode {
    public Complaint data;
    public DLLNode prev, next;

    public DLLNode(Complaint c){
        data = c;
        prev = next = null;
    }
}

public class DoublyLinkedList {
    private DLLNode head, tail;

    public void add(Complaint c){
        DLLNode n = new DLLNode(c);

        if(head == null) head = tail = n;
        else {
            tail.next = n;
            n.prev = tail;
            tail = n;
        }
    }

    public Complaint removeLast(){
        if(tail == null) return null;
        Complaint c = tail.data;

        tail = tail.prev;

        if(tail == null) head = null;
        else tail.next = null;

        return c;
    }
}