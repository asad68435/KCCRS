package KCCRS;

public class MinHeap {
    private java.util.ArrayList<Complaint> heap = new java.util.ArrayList<>();

    private int priority(Complaint c){
        switch(c.severity.toLowerCase()){
            case "critical": return 1;
            case "high":     return 2;
            case "medium":   return 3;
            default:         return 4; // low
        }
    }

    private boolean less(Complaint a, Complaint b){
        int pa = priority(a);
        int pb = priority(b);

        if(pa != pb) return pa < pb;
        return a.timeEpoch < b.timeEpoch;
    }

    public void push(Complaint c){
        heap.add(c);
        siftUp(heap.size() - 1);
    }

    public Complaint pop(){
        if(heap.isEmpty()) return null;

        Complaint top = heap.get(0);
        Complaint last = heap.remove(heap.size() - 1);

        if(!heap.isEmpty()){
            heap.set(0, last);
            siftDown(0);
        }
        return top;
    }

    public java.util.List<Complaint> contents(){
        return new java.util.ArrayList<>(heap);
    }

    private void siftUp(int i){
        while(i > 0){
            int parent = (i - 1) / 2;

            if(less(heap.get(i), heap.get(parent))){
                java.util.Collections.swap(heap, i, parent);
                i = parent;
            } else break;
        }
    }

    private void siftDown(int i){
        int n = heap.size();
        while(true){
            int left = 2*i + 1, right = 2*i + 2, smallest = i;

            if(left < n && less(heap.get(left), heap.get(smallest))) smallest = left;
            if(right < n && less(heap.get(right), heap.get(smallest))) smallest = right;

            if(smallest != i){
                java.util.Collections.swap(heap, i, smallest);
                i = smallest;
            } else break;
        }
    }
}