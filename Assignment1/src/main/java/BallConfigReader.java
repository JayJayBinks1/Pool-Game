import org.json.simple.JSONObject;

public class BallConfigReader extends ConfigReader {

    private JSONObject ballContents;

    public BallConfigReader(JSONObject ballContents) {
        this.ballContents = ballContents;
    }

    @Override
    public String[] getContents() {
        return null;
    }

}
