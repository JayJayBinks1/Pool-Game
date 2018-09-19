import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import org.json.simple.JSONObject;

public class Table {

    private String colour;
    private double width;
    private double height;
    private double friction;
    private int edgeWidth;
    private Rectangle table;

    public Table(String colour, double width, double height, double friction, int edgeWidth, Rectangle table) {

        this.colour = colour;
        this.width = width;
        this.height = height;
        this.friction = friction;
        this.edgeWidth = edgeWidth;
        this.table = table;
    }

    public String getColour() {return this.colour;}

    public double getFriction() {return this.friction;}

    public int getEdgeWidth() { return edgeWidth; }

    public double getWidth() {return this.width;}

    public double getHeight() {return this.height;}

    public Rectangle getShape() {return this.table;}

    public Circle[] getPockets() {
        Circle circles[] = new Circle[6];
        int x = edgeWidth;
        int y = edgeWidth;
        int holeNum = 0;
        for (int i = 0; i < 6; i++) {
            if (i == 3) {
                y += (height);
                holeNum = 0;
            }
            circles[i] = new Circle(x+holeNum*((width)/2), y, 35, Color.BLACK);
            holeNum++;
        }
        return circles;
    }

}
