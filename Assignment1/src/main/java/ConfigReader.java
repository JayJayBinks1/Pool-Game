import javafx.scene.shape.Shape;
import org.json.simple.JSONObject;

public abstract class ConfigReader {

    static ConfigReader getReader(ObjectType type, JSONObject object) {
        final TableConfigReader tableReader = new TableConfigReader(object);
        final BallConfigReader ballReader = new BallConfigReader(object);

        switch (type) {
            case TABLE:
                return tableReader;
            case BALL:
                return ballReader;
            default:
                System.out.println("Reader not recognised.");
                return null;
        }
    }


    //Converts JSONObject to String array
    public abstract String[] getContents();
}
