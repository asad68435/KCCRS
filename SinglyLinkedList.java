package KCCRS;

class SLLNode {
    public Complaint data;
    public SLLNode next;

    public SLLNode(Complaint c){ data = c; next = null; }
}

public class SinglyLinkedList {
    private SLLNode head;
    public int size = 0;

    public void add(Complaint c){
        SLLNode n = new SLLNode(c);
        if(head == null) head = n;
        else {
            SLLNode cur = head;
            while(cur.next != null) cur = cur.next;
            cur.next = n;
        }
        size++;
    }

    public Complaint findById(String id){
        SLLNode cur = head;
        while(cur != null){
            if(cur.data.id.equals(id)) return cur.data;
            cur = cur.next;
        }
        return null;
    }

    public boolean removeById(String id){
        if(head == null) return false;
        if(head.data.id.equals(id)){
            head = head.next;
            size--;
            return true;
        }
        SLLNode cur = head;
        while(cur.next != null){
            if(cur.next.data.id.equals(id)){
                cur.next = cur.next.next;
                size--;
                return true;
            }
            cur = cur.next;
        }
        return false;
    }

    public java.util.List<Complaint> toList(){
        java.util.List<Complaint> list = new java.util.ArrayList<>();
        SLLNode cur = head;
        while(cur != null){
            list.add(cur.data);
            cur = cur.next;
        }
        return list;
    }

    public void printReverse(){
        printReverseHelper(head);
    }

    private void printReverseHelper(SLLNode node){
        if(node == null) return;
        printReverseHelper(node.next);
        System.out.println(node.data);
    }
}