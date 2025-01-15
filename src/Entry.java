public class Entry {
    private int id;
    private String studentName;
    private Haus haus;
    private String lehrerName;
    private int punkte;

    public Entry(int id, String studentName,Haus haus, String lehrerName, int punkte) {
        this.id = id;
        this.studentName = studentName;
        this.haus = haus;
        this.lehrerName = lehrerName;
        this.punkte = punkte;
    }

    public String getStudentName() {
        return studentName;
    }

    public Haus getHaus() {
        return haus;
    }

    public int getPunkte() {
        return punkte;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public void setHaus(Haus haus) {
        this.haus = haus;
    }

    public void setPunkte(int punkte) {
        this.punkte = punkte;
    }

    public void setLehrerName(String lehrerName) {
        this.lehrerName = lehrerName;
    }
}
