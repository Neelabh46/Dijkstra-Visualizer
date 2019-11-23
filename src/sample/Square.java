package sample;

import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class Square extends Rectangle {


    private Double x;
    private Double y;

    public Square(Double x, Double y,Double side) {

        super(x,y,side,side);
        this.x=x;
        this.y=y;

    }

}
