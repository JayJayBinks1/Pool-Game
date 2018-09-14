import org.json.simple.JSONObject;

public class BallConfigReader extends ConfigReader {

    private JSONObject ballContents;

    public BallConfigReader(JSONObject ballContents) {
        this.ballContents = ballContents;
    }

    @Override
    public String[] getContents() {
        String contents[] = new String[6];
        contents[0] = (String) this.ballContents.get("colour");
        JSONObject coordinates = (JSONObject) this.ballContents.get("position");
        contents[1] = (String) coordinates.get("x");
        contents[2] = (String) coordinates.get("y");
        JSONObject velocity = (JSONObject) this.ballContents.get("velocity");
        contents[3] = (String) velocity.get("x");
        contents[4] = (String) velocity.get("y");
        contents[5] = (String) this.ballContents.get("mass");
        return contents;
    }

}
