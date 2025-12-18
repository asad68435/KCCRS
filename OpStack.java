package KCCRS;

public class OpStack {
    private java.util.LinkedList<Operation> stack = new java.util.LinkedList<>();

    public void push(Operation op){ stack.addFirst(op); }

    public Operation pop(){ return stack.isEmpty() ? null : stack.removeFirst(); }
}