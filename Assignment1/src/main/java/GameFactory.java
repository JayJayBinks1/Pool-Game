import org.json.simple.JSONObject;

public abstract class GameFactory {

    public abstract Table makeTable(JSONObject tableContents, int edgeWidth);
    public abstract Ball makeBall(JSONObject ballContents, int ballRadius);
}
