import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class Table {

    private JSONObject tableContents;
    private String colour;
    private int width;
    private int height;
    private int friction;
    private Rectangle table;

    public Table(JSONObject tableContents) {
        ConfigReader reader = ConfigReader.getReader(ObjectType.TABLE, tableContents);
        String contents[] = reader.getContents();
        this.colour = contents[0];
        this.width = Integer.parseInt(contents[1]);
        this.height = Integer.parseInt(contents[2]);
        this.friction = Integer.parseInt(contents[3]);
        this.table = new Rectangle(75, 75, this.width, this.height);
        this.table.setFill(Color.DARKGREEN);
        this.table.setStroke(Color.rgb(165, 70, 7));
        this.table.setStrokeWidth(50);
    }

    public String getColour() {return this.colour;}

    public int getFriction() {return this.friction;}

    public Rectangle getShape() {return this.table;}

    public Circle[] getHoles() {
        Circle circles[] = new Circle[6];
        int x = 100;
        int y = 100;
        int holeNum = 0;
        for (int i = 0; i < 6; i++) {
            if (i == 3) {
                y += (height-50);
                holeNum = 0;
            }
            circles[i] = new Circle(x+holeNum*((width-50)/2), y, 30, Color.BLACK);
            holeNum++;
        }
        return circles;
    }

}
