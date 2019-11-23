package sample;

public class MyCircle extends javafx.scene.shape.Circle {

    private String name ;
    public MyCircle(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
