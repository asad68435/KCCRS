package KCCRS;

import java.util.*;

public class Main {
    private static Scanner sc = new Scanner(System.in);

    // Core structures
    private static SinglyLinkedList records = new SinglyLinkedList();
    private static HashTable ht = new HashTable(151); // prime bucket count
    private static MinHeap heap = new MinHeap();
    private static ComplaintQueue queue = new ComplaintQueue();
    private static OpStack opStack = new OpStack();
    private static DoublyLinkedList history = new DoublyLinkedList();

    // ID generator
    private static int idCounter = 1000;

    public static void main(String[] args) {
        System.out.println("=== KCCRS - Karachi City Complaint & Response System ===");
        boolean running = true;
        while (running) {
            showMenu();
            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1": addComplaint(); break;
                case "2": searchById(); break;
                case "3": displayAll(); break;
                case "4": pushToHeap(); break;
                case "5": popHeap(); break;
                case "6": showHeap(); break;
                case "7": enqueueProcessing(); break;
                case "8": dequeueProcessing(); break;
                case "9": showQueue(); break;
                case "10": generateDailyReport(); break;
                case "11": undoLast(); break;
                case "12": printReverseRecords(); break;
                case "13": saveToFile(); break;
                case "14": loadFromFile(); break;
                case "15": System.out.println("Exiting. Goodbye!"); running = false; break;
                default: System.out.println("Invalid option. Try again.");
            }
            System.out.println();
        }
    }

    private static void showMenu() {
        System.out.println("1. Add complaint");
        System.out.println("2. Search complaint by ID");
        System.out.println("3. Display all complaints");
        System.out.println("4. Push complaint to priority queue (heap)");
        System.out.println("5. Pop highest priority complaint (serve urgent)");
        System.out.println("6. Show heap contents");
        System.out.println("7. Enqueue complaint ID to processing queue (FIFO)");
        System.out.println("8. Dequeue (process) complaint ID");
        System.out.println("9. Display pending queue");
        System.out.println("10. Generate daily report (sorted)");
        System.out.println("11. Undo last operation (stack)");
        System.out.println("12. Print all complaints in reverse (recursion)");
        System.out.println("13. Save complaints to file (simple persistence)");
        System.out.println("14. Load complaints from file");
        System.out.println("15. Exit");
        System.out.print("Choose: ");
    }

    // Utility: generate unique ID
    private static String genId() {
        return "KCCRS-" + (idCounter++);
    }

    // Module 1: Add complaint with duplicate check (HashTable + SLL)
    private static void addComplaint() {
        System.out.print("Citizen name: ");
        String name = sc.nextLine().trim();
        System.out.print("Area (Korangi,Saddar,Clifton,Gulshan): ");
        String area = sc.nextLine().trim();
        System.out.print("Type (Water,Garbage,Electricity,Gas,Traffic): ");
        String type = sc.nextLine().trim();
        System.out.print("Severity (Low,Medium,High,Critical): ");
        String sev = sc.nextLine().trim();

        long now = System.currentTimeMillis();
        String id = genId();
        Complaint c = new Complaint(id, name, area, type, sev, now);

        // Duplicate detection
        if (ht.containsDuplicate(c)) {
            System.out.println("Duplicate complaint detected. Not added.");
            return;
        }

        // Add to structures
        records.add(c);
        ht.put(c);
        history.add(c);
        opStack.push(new Operation("ADD", c));

        System.out.println("Added: " + c);
    }

    // Search by ID (Singly Linked List)
    private static void searchById() {
        System.out.print("Enter ID: ");
        String id = sc.nextLine().trim();
        Complaint c = records.findById(id);
        if (c == null) System.out.println("Complaint not found.");
        else System.out.println("Found: " + c);
    }

    // Display all complaints
    private static void displayAll() {
        List<Complaint> all = records.toList();
        if (all.isEmpty()) {
            System.out.println("No complaints recorded.");
            return;
        }
        System.out.println("--- All complaints ---");
        for (Complaint c : all) System.out.println(c);
    }

    // Module 2: Heap operations
    private static void pushToHeap() {
        System.out.print("Enter complaint ID to push to heap: ");
        String id = sc.nextLine().trim();
        Complaint c = records.findById(id);
        if (c == null) System.out.println("ID not found.");
        else {
            heap.push(c);
            opStack.push(new Operation("PUSH_HEAP", c));
            System.out.println("Pushed to heap: " + c);
        }
    }

    private static void popHeap() {
        Complaint c = heap.pop();
        if (c == null) System.out.println("Heap empty.");
        else {
            opStack.push(new Operation("POP_HEAP", c));
            System.out.println("Popped (serve urgent): " + c);
        }
    }

    private static void showHeap() {
        List<Complaint> contents = heap.contents();
        if (contents.isEmpty()) System.out.println("Heap empty.");
        else {
            System.out.println("Heap contents (array order):");
            for (Complaint c : contents) System.out.println(c);
        }
    }

    // Module 3: Queue operations (FIFO)
    private static void enqueueProcessing() {
        System.out.print("Enter complaint ID to enqueue: ");
        String id = sc.nextLine().trim();
        Complaint c = records.findById(id);
        if (c == null) System.out.println("ID not found.");
        else {
            queue.enqueue(id);
            opStack.push(new Operation("ENQUEUE", c));
            System.out.println("Enqueued: " + id);
        }
    }

    private static void dequeueProcessing() {
        String id = queue.dequeue();
        if (id == null) System.out.println("Queue empty.");
        else {
            Complaint c = records.findById(id);
            opStack.push(new Operation("DEQUEUE", c));
            System.out.println("Dequeued (processing): " + id + " -> " + (c == null ? "(record missing)" : c));
        }
    }

    private static void showQueue() {
        List<String> q = queue.list();
        if (q.isEmpty()) System.out.println("Queue empty.");
        else {
            System.out.println("Pending queue IDs:");
            for (String s : q) System.out.println(s);
        }
    }

    // Module 4: Daily report generation (sorting)
    private static void generateDailyReport() {
        List<Complaint> list = records.toList();
        if (list.isEmpty()) {
            System.out.println("No complaints to report.");
            return;
        }

        Complaint[] arr = list.toArray(new Complaint[0]);

        System.out.println("Choose sort key: 1-Area, 2-Severity, 3-Time");
        String k = sc.nextLine().trim();

        Comparator<Complaint> cmp;
        if (k.equals("1")) {
            cmp = Comparator.comparing(c -> c.area.toLowerCase());
        } else if (k.equals("2")) {
            cmp = Comparator.comparingInt(c -> {
                switch (c.severity.toLowerCase()) {
                    case "critical": return 1;
                    case "high": return 2;
                    case "medium": return 3;
                    default: return 4;
                }
            });
        } else {
            cmp = Comparator.comparingLong(c -> c.timeEpoch);
        }

        System.out.println("Choose algorithm: 1-MergeSort (n log n), 2-InsertionSort (n^2)");
        String alg = sc.nextLine().trim();

        if (alg.equals("1")) Sorting.mergeSort(arr, cmp);
        else Sorting.insertionSort(arr, cmp);

        System.out.println("--- Daily Report ---");
        for (Complaint c : arr) System.out.println(c);

        // Complexity info printed for submission purposes
        System.out.println("Complexity: MergeSort O(n log n) time, O(n) space. InsertionSort O(n^2) time, O(1) space.");
    }

    // Module 5: Undo using stack + recursion
    private static void undoLast() {
        Operation op = opStack.pop();
        if (op == null) {
            System.out.println("Nothing to undo.");
            return;
        }

        switch (op.type) {
            case "ADD":
                boolean removed = records.removeById(op.complaint.id);
                System.out.println("Undo ADD: removed " + op.complaint.id + " success=" + removed);
                break;

            case "PUSH_HEAP":
                removeFromHeap(op.complaint.id);
                System.out.println("Undo PUSH_HEAP: removed from heap if existed.");
                break;

            case "POP_HEAP":
                heap.push(op.complaint);
                System.out.println("Undo POP_HEAP: pushed back " + op.complaint.id);
                break;

            case "ENQUEUE":
                removeFromQueue(op.complaint.id);
                System.out.println("Undo ENQUEUE: removed from queue if existed.");
                break;

            case "DEQUEUE":
                // Re-enqueue to front: reconstruct queue with this ID first
                List<String> cur = queue.list();
                ComplaintQueue newQ = new ComplaintQueue();
                newQ.enqueue(op.complaint.id);
                for (String s : cur) newQ.enqueue(s);
                queue = newQ;
                System.out.println("Undo DEQUEUE: re-enqueued " + op.complaint.id + " to front.");
                break;

            case "PUSH HEAP": // already handled above
            default:
                System.out.println("Undo: Operation type '" + op.type + "' not fully supported.");
        }
    }

    // helper: remove a complaint from heap by rebuilding it
    private static void removeFromHeap(String id) {
        List<Complaint> contents = heap.contents();
        MinHeap newHeap = new MinHeap();
        for (Complaint c : contents) {
            if (!c.id.equals(id)) newHeap.push(c);
        }
        heap = newHeap;
    }

    // helper: remove id from queue by rebuilding
    private static void removeFromQueue(String id) {
        List<String> contents = queue.list();
        ComplaintQueue newQ = new ComplaintQueue();
        for (String s : contents) {
            if (!s.equals(id)) newQ.enqueue(s);
        }
        queue = newQ;
    }

    // print reverse using recursion (SLL helper)
    private static void printReverseRecords() {
        System.out.println("Records in reverse order:");
        records.printReverse();
    }

    /*
     * Simple persistence (optional): save/load to/from a text file.
     * This is minimal and intended for demo only (no JSON lib used).
     * Format per line: id|name|area|type|severity|timeEpoch
     */

    private static void saveToFile() {
        System.out.print("Enter filename to save (e.g., complaints.txt): ");
        String fname = sc.nextLine().trim();
        List<Complaint> all = records.toList();
        try (java.io.PrintWriter pw = new java.io.PrintWriter(fname)) {
            for (Complaint c : all) {
                String line = String.join("|",
                        c.id,
                        escapePipe(c.citizenName),
                        escapePipe(c.area),
                        escapePipe(c.type),
                        escapePipe(c.severity),
                        String.valueOf(c.timeEpoch)
                );
                pw.println(line);
            }
            System.out.println("Saved " + all.size() + " complaints to " + fname);
        } catch (Exception e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    private static void loadFromFile() {
        System.out.print("Enter filename to load (e.g., complaints.txt): ");
        String fname = sc.nextLine().trim();
        java.io.File f = new java.io.File(fname);
        if (!f.exists()) { System.out.println("File not found."); return; }

        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(f))) {
            String line;
            int loaded = 0;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|", -1);
                if (parts.length < 6) continue;
                String id = parts[0];
                String name = unescapePipe(parts[1]);
                String area = unescapePipe(parts[2]);
                String type = unescapePipe(parts[3]);
                String sev = unescapePipe(parts[4]);
                long time = Long.parseLong(parts[5]);

                // avoid duplicates
                Complaint c = new Complaint(id, name, area, type, sev, time);
                if (!ht.containsDuplicate(c)) {
                    records.add(c);
                    ht.put(c);
                    history.add(c);
                    loaded++;
                }
            }
            System.out.println("Loaded " + loaded + " new complaints from " + fname);
        } catch (Exception e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
    }

    // helpers to allow '|' in text fields
    private static String escapePipe(String s) {
        return s.replace("|", "\\|");
    }
    private static String unescapePipe(String s) {
        return s.replace("\\|", "|");
    }
}