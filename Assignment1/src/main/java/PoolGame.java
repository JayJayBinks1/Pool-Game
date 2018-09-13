import javafx.application.Application;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;


public class PoolGame extends Application {

    private static String[] arguments;

    public static void main(String[] args) {
        arguments = args;
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        File file = new File(arguments[0]);
        JSONObject objects = null;
        try {
            FileReader reader = new FileReader(file);
            JSONParser parser = new JSONParser();
            objects = (JSONObject) parser.parse(reader);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        JSONObject tableContents = (JSONObject) objects.get("Table");
        Table table = new Table(tableContents);
        JSONObject ballList = (JSONObject) objects.get("Balls");
        JSONArray balls = (JSONArray) ballList.get("ball");

        for (int i = 0; i < balls.size(); i++) {
            System.out.println(balls.get(i));
        }

        try {

            Rectangle tableShape = table.getShape();

            Pane canvas = new Pane();  //The root of scene graph is a layout node
            // Creating a Scene by passing the group object, height and width
            Scene scene = new Scene(canvas,tableShape.getWidth()+150,tableShape.getHeight()+150);
            //setting color to the scene
            scene.setFill(Color.BLACK);

            canvas.getChildren().add(tableShape);

            Circle holes[] = table.getHoles();

            for (Circle hole : holes) {
                canvas.getChildren().addAll(hole);
            }

            //Setting the title to Stage.
            primaryStage.setTitle("Pool Game");

            //Adding the scene to Stage
            primaryStage.setScene(scene);
            //Displaying the contents of the stage
            primaryStage.show();
        }  catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}