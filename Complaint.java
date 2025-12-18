package KCCRS;

public class Complaint {
    public String id;
    public String citizenName;
    public String area;
    public String type;
    public String severity;
    public long timeEpoch;

    public Complaint(String id, String citizenName, String area, String type, String severity, long timeEpoch) {
        this.id = id;
        this.citizenName = citizenName;
        this.area = area;
        this.type = type;
        this.severity = severity;
        this.timeEpoch = timeEpoch;
    }

    public String toString(){
        java.util.Date d = new java.util.Date(timeEpoch);
        java.text.SimpleDateFormat fmt = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return "[ID:" + id + " | " + citizenName + " | " + area + " | " + type + " | " + severity + " | " + fmt.format(d) + "]";
    }
}