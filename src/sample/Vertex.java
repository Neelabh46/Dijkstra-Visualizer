package sample;

public class Vertex {

    private int x_coordinate;
    private int y_coordinate;
    private String name;
    public Vertex(int x,int y,String n)
    {
        x_coordinate = x ;
        y_coordinate = y;
        name = n;
    }

    public int getX_coordinate() {
        return x_coordinate;
    }

    public int getY_coordinate() {
        return y_coordinate;
    }

    public String getName() {
        return name;
    }

    public void setX_coordinate(int x_coordinate) {
        this.x_coordinate = x_coordinate;
    }

    public void setY_coordinate(int y_coordinate) {
        this.y_coordinate = y_coordinate;
    }

    public void setName(String name) {
        this.name = name;
    }
}
