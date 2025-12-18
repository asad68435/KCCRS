package KCCRS;

public class ComplaintQueue {
    private java.util.LinkedList<String> q = new java.util.LinkedList<>();

    public void enqueue(String id){ q.addLast(id); }

    public String dequeue(){ return q.isEmpty() ? null : q.removeFirst(); }

    public java.util.List<String> list(){ return new java.util.ArrayList<>(q); }
}