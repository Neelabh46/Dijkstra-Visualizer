package sample;

import javafx.scene.shape.Line;

public class MyEdge extends Line {

    private int wt;
    private String from;
    private String to;

    public MyEdge(int wt) {
        this.wt = wt;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public void setWt(int wt) {
        this.wt = wt;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public int getWt() {
        return wt;
    }
}
