package KCCRS;

public class Operation {
    public String type;
    public Complaint complaint;

    public Operation(String type, Complaint c){
        this.type = type;
        this.complaint = c;
    }
}