import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
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
import java.util.ArrayList;


public class PoolGame extends Application {
    
    private static String[] arguments;
    private Line cue;
    private boolean isCueSet = false;
    private final double restState = 0.000001;

    public static void main(String[] args) {
        arguments = args;
        launch(args);
    }

    private JSONObject getConfig(String argument) {
        File file = new File(argument);
        JSONObject objects = null;
        try {
            FileReader reader = new FileReader(file);
            JSONParser parser = new JSONParser();
            objects = (JSONObject) parser.parse(reader);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return objects;
    }

    private void addHoles(Pane pane, Table table) {
        Circle holes[] = table.getHoles();

        for (Circle hole : holes) {
            pane.getChildren().addAll(hole);
        }
    }

    private void adjustBallPositions(Pane pane, ArrayList<Ball> poolBalls, Table table) {

        for (Ball ball : poolBalls) {

            if (ball.getxPosition() <= 50+15) {
                ball.setxPosition(50+15);
            }

            else if (ball.getxPosition() >= table.getWidth()) {
                ball.setxPosition(table.getWidth()+50-15);
            }

            else {
                ball.setxPosition(ball.getxPosition()+50);
            }

            if (ball.getyPosition() <= 50+15) {
                ball.setyPosition(50+15);
            }

            else if (ball.getyPosition() >= table.getHeight()) {
                ball.setyPosition(table.getHeight()+50-15);
            }

            else {
                ball.setyPosition(ball.getyPosition()+50);
            }

            pane.getChildren().add(ball.getBall());
        }
    }

    private void setMouseEvents(Pane pane) {

        pane.setOnMousePressed(event -> {
            isCueSet = false;
            cue = new Line(event.getX(), event.getY(), event.getX(), event.getY());
            cue.setStrokeWidth(5);
            cue.setStroke(Color.BURLYWOOD);
            pane.getChildren().add(cue);
        });

        pane.setOnMouseDragged(event -> {
            cue.setEndX(event.getX());
            cue.setEndY(event.getY());
        });

        pane.setOnMouseReleased(event -> {
            pane.getChildren().remove(cue);
            isCueSet = true;
        });
    }

    private Pane setUpGame(Stage primaryStage, Table table, ArrayList<Ball> poolBalls) {
        Rectangle tableShape = table.getShape();

        Pane pane = new Pane();  //The root of scene graph is a layout node
        // Creating a Scene by passing the group object, height and width
        Scene scene = new Scene(pane,tableShape.getWidth()+100,tableShape.getHeight()+100);
        //setting color to the scene
        scene.setFill(Color.rgb(165, 70, 7));

        pane.getChildren().add(tableShape);

        addHoles(pane, table);

        adjustBallPositions(pane, poolBalls, table);

        setMouseEvents(pane);

        //Setting the title to Pool Game.
        primaryStage.setTitle("Pool Game");

        //Adding the scene to Stage
        primaryStage.setScene(scene);
        //Displaying the contents of the stage
        primaryStage.show();

        return pane;
    }

    private void setUpCollision(Ball ball, Ball otherBall) {
        Point2D ballPos = new Point2D(ball.getxPosition(), ball.getyPosition());
        Point2D ballVel = new Point2D(ball.getxVelocity(), ball.getyVelocity());
        double ballMass = ball.getMass();

        Point2D otherPos = new Point2D(otherBall.getxPosition(), otherBall.getyPosition());
        Point2D otherVel = new Point2D(otherBall.getxVelocity(), otherBall.getyVelocity());
        double otherMass = otherBall.getMass();

        Point2D newVels[] = collide(ballPos, ballVel, ballMass, otherPos, otherVel, otherMass);

        if (newVels != null) {
            ballVel = newVels[0];
            otherVel = newVels[1];

            ball.setxVelocity(ballVel.getX());
            ball.setyVelocity(ballVel.getY());

            otherBall.setxVelocity(otherVel.getX());
            otherBall.setyVelocity(otherVel.getY());
        }
    }

    private Point2D[] collide(Point2D posA, Point2D velA, double massA, Point2D posB, Point2D velB, double massB) {

        //calculate their mass ratio
        double mR = massB/massA;

        //calculate the axis of collision
        Point2D collisionVector = posB.subtract(posA);
        collisionVector = collisionVector.normalize();

        //the proportion of each balls velocity along the axis of collision
        double vA = collisionVector.dotProduct(velA);
        double vB = collisionVector.dotProduct(velB);

        //if balls are moving away from each other
        if (vA <= 0 && vB >= 0) {
            return null;
        }

        //The velocity of each ball after a collision can be found by solving the quadratic equation
        //given by equating momentum energy and energy before and after the collision and finding the
        //velocities that satisfy this
        //-(mR+1)x^2 2*(mR*vB+vA)x -((mR-1)*vB^2+2*vA*vB)=0
        //first we find the discriminant
        double a = -(mR + 1);
        double b = 2 * (mR * vB + vA);
        double c = -((mR - 1) * vB * vB + 2 * vA * vB);
        double discriminant = Math.sqrt(b * b - 4 * a * c);
        double root = (-b + discriminant)/(2 * a);
        //only one of the roots is the solution, the other pertains to the current velocities
        if (root - vB < 0.01) {
            root = (-b - discriminant)/(2 * a);
        }

        //The resulting changes in velocity for ball A and B
        Point2D deltaVA = collisionVector.multiply(mR * (vB - root));
        Point2D deltaVB = collisionVector.multiply(mR * (root - vB));

        Point2D newVels[] = new Point2D[2];

        newVels[0] = deltaVA;
        newVels[1] = deltaVB;

        return newVels;
    }

    private void moveBall(Ball ball) {
        ball.setxPosition((ball.getxPosition()+ball.getxVelocity()));
        ball.setyPosition((ball.getyPosition()+ball.getyVelocity()));
    }

    private void setToRest(Ball ball) {
        if (ball.getxVelocity() == 0.0) {
            ball.setxVelocity(restState);
        }

        if (ball.getyVelocity() == 0.0) {
            ball.setyVelocity(restState);
        }
    }

    private void fallInPocket(Pane pane, Ball ball, Circle holes[], ArrayList<Ball> poolBalls) {
        for (Circle hole : holes) {
            double distance = calculateDistance(ball.getxPosition(), ball.getyPosition(), hole.getCenterX(), hole.getCenterY());

            if (distance <= 30) {
                poolBalls.remove(ball);
                pane.getChildren().remove(ball.getBall());
            }
        }
    }

    private double calculateDistance(double x1, double y1, double x2, double y2) {
        double deltaX = x1 - x2;
        double deltaY = y1 - y2;
        return Math.sqrt((deltaX*deltaX) + (deltaY*deltaY));
    }

    private Point2D getCueDirection(Ball ball, double xVelocity, double yVelocity) {

        if (cue.getEndX() >= ball.getxPosition()) {
            xVelocity *= -1;
        }

        if (cue.getEndY() >= ball.getyPosition()) {
            yVelocity *= -1;
        }

        return new Point2D(xVelocity, yVelocity);
    }

    private Point2D findCueVelocity(Ball ball, Table table) {

        double xVelocity = 0;
        double yVelocity = 0;
        double length = table.getWidth()/2;

        double cueLength = calculateDistance(cue.getStartX(), cue.getStartY(), cue.getEndX(), cue.getEndY());

        if (cueLength >= length) {
            //System.out.println("1.5");
            xVelocity = 1.5;
            yVelocity = 1.5;
        }

        else if (cueLength < length && cueLength >= length/2) {
            //System.out.println("1.0");
            xVelocity = 1;
            yVelocity = 1;
        }

        else if (cueLength < length/2 && cueLength >= length/4) {
            //System.out.println("0.75");
            xVelocity = 0.5;
            yVelocity = 0.5;
        }

        else if (cueLength < length/4) {
            //System.out.println("0.25");
            xVelocity = 0.25;
            yVelocity = 0.25;
        }

        return getCueDirection(ball, xVelocity, yVelocity);
    }

    private void strikeBall(Ball ball, Table table) {

        Double distance = calculateDistance(ball.getxPosition(), ball.getyPosition(), cue.getStartX(), cue.getStartY());

        if (distance <= 15) {

            Point2D ballPos = new Point2D(ball.getxPosition(), ball.getyPosition());
            Point2D ballVel = new Point2D(ball.getxVelocity(), ball.getyVelocity());
            double ballMass = ball.getMass();

            Point2D otherPos = new Point2D(cue.getEndX(), cue.getEndY());
            Point2D otherVel = findCueVelocity(ball, table);
            double otherMass = 1;

            Point2D newVels[] = collide(ballPos, ballVel, ballMass, otherPos, otherVel, otherMass);

            if (newVels != null) {
                ballVel = newVels[0];
                ball.setxVelocity(ballVel.getX());
                ball.setyVelocity(ballVel.getY());
            }

            isCueSet = false;
        }
    }

    private void bounceOffWalls(Ball ball, Table table) {
        if ((ball.getxPosition() + 15 + ball.getxVelocity()) >= (table.getWidth()+50) || (ball.getxPosition() + 15 + ball.getxVelocity()) <= (75)) {
            double xVelocity = ball.getxVelocity();
            ball.setxVelocity(xVelocity *= -1);
        }

        if ((ball.getyPosition() + 15 + ball.getyVelocity()) >= (table.getHeight()+50) || (ball.getyPosition() + 15 + ball.getyVelocity()) <= (75)) {
            double yVelocity = ball.getyVelocity();
            ball.setyVelocity(yVelocity *= -1);
        }
    }

    private void applyFriction(Ball ball, Table table) {
        if (ball.getxVelocity() > 0) {
            ball.setxVelocity(ball.getxVelocity() + (table.getFriction()*-(0.001)));
        }

        else if (ball.getxVelocity() < 0) {
            ball.setxVelocity(ball.getxVelocity() - (table.getFriction()*-(0.001)));
        }

        if (ball.getxVelocity() <= 0.001 && ball.getxVelocity() > -0.001) {
            ball.setxVelocity(restState);
        }

        if (ball.getyVelocity() > 0) {
            ball.setyVelocity(ball.getyVelocity() + (table.getFriction()*-(0.001)));
        }

        else if (ball.getyVelocity() < 0) {
            ball.setyVelocity(ball.getyVelocity() - (table.getFriction()*-(0.001)));
        }

        if (ball.getyVelocity() <= 0.001 && ball.getyVelocity() > -0.001) {
            ball.setyVelocity(restState);
        }
    }

    @Override
    public void start(Stage primaryStage) {
        JSONObject objects = getConfig(arguments[0]);

        JSONObject tableContents = (JSONObject) objects.get("Table");
        JSONArray balls = (JSONArray) ((JSONObject) objects.get("Balls")).get("ball");

        PoolGameFactory factory = PoolGameFactory.getInstance();

        Table table = factory.makeTable(tableContents);

        ArrayList<Ball> poolBalls = new ArrayList<>();


        for (int i = 0; i < balls.size(); i++) {
            poolBalls.add(factory.makeBall((JSONObject) balls.get(i)));
        }

        try {

            Pane pane = setUpGame(primaryStage, table, poolBalls);
            Circle holes[] = table.getHoles();

            AnimationTimer animator = new AnimationTimer() {
                @Override
                public void handle(long now) {

                    for (int i = 0; i < poolBalls.size(); i++) {
                        Ball ball = poolBalls.get(i);

                        moveBall(ball);

                        setToRest(ball);

                        fallInPocket(pane,ball, holes, poolBalls);

                        if (isCueSet && ball.getColour().equalsIgnoreCase("white") && ball.getxVelocity() == restState && ball.getyVelocity() == restState) {
                            strikeBall(ball, table);
                        }

                        for (int j = 0; j < poolBalls.size(); j++) {
                            if (i == j) {
                                continue;
                            }

                            Ball otherBall = poolBalls.get(j);

                            double distance = calculateDistance(ball.getxPosition(), ball.getyPosition(), otherBall.getxPosition(), otherBall.getyPosition());

                            if (distance <= 30) {
                                setUpCollision(ball, otherBall);
                            }
                        }

                        bounceOffWalls(ball, table);

                        ball.getBall().setCenterX(ball.getxPosition());
                        ball.getBall().setCenterY(ball.getyPosition());

                        applyFriction(ball, table);

                    }
                }
            };

            animator.start();

        }  catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}