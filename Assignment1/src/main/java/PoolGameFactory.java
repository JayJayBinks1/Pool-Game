import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import org.json.simple.JSONObject;


public final class PoolGameFactory extends GameFactory {

    private static PoolGameFactory instance;

    private PoolGameFactory() {}

    public static PoolGameFactory getInstance() {
        if (instance == null) {
            instance = new PoolGameFactory();
        }

        return instance;
    }

    @Override
    public Table makeTable(JSONObject tableContents, int edgeWidth) {

        String colour = tableContents.get("colour").toString();
        JSONObject dimensions = (JSONObject) tableContents.get("size");
        double width = Double.parseDouble(dimensions.get("x").toString());
        double height = Double.parseDouble(dimensions.get("y").toString());
        double friction = Double.parseDouble(tableContents.get("friction").toString());
        Rectangle table = new Rectangle(edgeWidth, edgeWidth, (int)width, (int)height);
        table.setFill(Paint.valueOf(colour));

        return new Table(colour, width, height, friction, edgeWidth, table);
    }

    @Override
    public Ball makeBall(JSONObject ballContents, int ballRadius) {
        PoolBallBuilder builder = new PoolBallBuilder();

        builder.setColour(ballContents.get("colour").toString());

        JSONObject coordinates = (JSONObject) ballContents.get("position");
        builder.setxPosition(Double.parseDouble(coordinates.get("x").toString()));
        builder.setyPosition(Double.parseDouble(coordinates.get("y").toString()));

        JSONObject velocity = (JSONObject) ballContents.get("velocity");
        builder.setxVelocity(Double.parseDouble(velocity.get("x").toString()));
        builder.setyVelocity(Double.parseDouble(velocity.get("y").toString()));

        builder.setMass(Double.parseDouble(ballContents.get("mass").toString()));

        builder.drawBall(ballRadius);

        return builder.getBall();
    }
}
