import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import org.json.simple.JSONObject;

public class TableConfigReader extends ConfigReader {

    private JSONObject tableContents;

    public TableConfigReader(JSONObject tableContents) {
        this.tableContents = tableContents;
    }

    @Override
    public String[] getContents() {
        String contents[] = new String[4];

        contents[0] = (String) this.tableContents.get("colour");
        JSONObject dimensions = (JSONObject) this.tableContents.get("size");
        contents[1] = (String) dimensions.get("x");
        contents[2] = (String) dimensions.get("y");
        contents[3] = (String) this.tableContents.get("friction");

        return contents;
    }


}
