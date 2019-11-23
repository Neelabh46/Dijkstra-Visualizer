package sample;

public class Edge {

    private String from;
    private String to;
    private int wt;

    public Edge(String f,String t,int w)
    {
        from = f;
        to = t;
        wt = w;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setWt(int wt) {
        this.wt = wt;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public int getWt() {
        return wt;
    }
}
