package KCCRS;

public class HashTable {
    private int capacity;
    private java.util.LinkedList<Complaint>[] buckets;

    @SuppressWarnings("unchecked")
    public HashTable(int capacity){
        this.capacity = capacity;
        buckets = new java.util.LinkedList[capacity];

        for(int i=0; i<capacity; i++)
            buckets[i] = new java.util.LinkedList<>();
    }

    private int hash(String key){
        int h = 0;
        for(char ch : key.toCharArray())
            h = (31 * h + ch) % capacity;

        return h;
    }

    private String keyOf(Complaint c){
        return c.citizenName.toLowerCase()+"|"+c.area.toLowerCase()+"|"+c.type.toLowerCase()+"|"+c.severity.toLowerCase()+"|"+c.timeEpoch;
    }

    public boolean containsDuplicate(Complaint c){
        String k = keyOf(c);
        int idx = hash(k);

        for(Complaint existing : buckets[idx]){
            if(keyOf(existing).equals(k))
                return true;
        }
        return false;
    }

    public void put(Complaint c){
        if(containsDuplicate(c)) return;
        String k = keyOf(c);
        int idx = hash(k);
        buckets[idx].add(c);
    }
}