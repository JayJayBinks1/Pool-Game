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

/**
 * The PoolGame program simulates a game of Pool
 * using JavaFX. It sets up the game using information
 * given by a config JSON file from the command line
 * and then runs it.
 *
 * @author Jayden Zangari
 * @version 1.0
 * @since 2018-09-11emai
 */

public class PoolGame extends Application {
    
    private static String[] arguments; // Contains the file name given from the command line
    private Line cue; // The cue stick
    private boolean isCueSet = false; // Boolean variable that checks if player has let go of cue
    private final double restState = 0.000001; // State when balls aren't moving. Using 0 causes problems in removing objects from Pane
    private final int edgeWidth = 50; // Width of the table's edge (the brown part)
    private final int ballRadius = 15; // Radius of the cue balls

    /**
     * Main method. Takes in a JSON file through command line
     * and launches the game setup
     * @param args A string of the JSON file's path given through command line
     */
    public static void main(String[] args) {
        arguments = args;
        launch(args);
    }

    /**
     * Reads the config JSON file and returns
     * it as a JSONObject
     * @param argument String of the config file's path
     * @return JSONObject the contents of the file as a JSONObject
     */
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

    /**
     * Adds the pockets into the pane so that
     * they can be visualised on screen
     * @param pane Layout pane for the game scene
     * @param table Table object containing the pockets
     */
    private void addpockets(Pane pane, Table table) {
        Circle pockets[] = table.getPockets();

        // Adds each pocket to the pane to be visualised
        for (Circle pocket : pockets) {
            pane.getChildren().addAll(pocket);
        }
    }

    /**
     * Returns true if a ball is beyond the
     * left side of the table
     * @param xPosition The ball's x coordinate
     * @return true if beyond, false if else
     */
    private boolean isOutOfBoundsLeft(double xPosition) {
        return xPosition <= edgeWidth;
    }

    /**
     * Returns true if a ball is beyond the
     * right side of the table
     * @param xPosition The ball's x coordinate
     * @param tableWidth The width of the table
     * @return true if beyond, false if else
     */
    private boolean isOutOfBoundsRight(double xPosition, double tableWidth) {
        return xPosition >= tableWidth;
    }

    /**
     * Returns true if a ball is beyond
     * the upper bound of the table
     * @param yPosition The ball's y coordinate
     * @return true if beyond, false if else
     */
    private boolean isOutOfBoundsUp(double yPosition) {
        return yPosition <= edgeWidth;
    }

    /**
     * Returns true if a ball is beyond
     * the lower bound of the table
     * @param yPosition The ball's y coordinate
     * @param tableHeight The height of the table
     * @return true if beyond, false if else
     */
    private boolean isOutOfBoundsDown(double yPosition, double tableHeight) {
        return yPosition >= tableHeight;
    }

    /**
     * Adjusts the initial ball positions read
     * in from the config file to account for the
     * edgeWidth. Also checks if balls are out of bounds
     * And places them on the edge if so
     * @param pane Layout pane for the game scene
     * @param balls The list of balls
     * @param table The table object
     */
    private void adjustBallPositions(Pane pane, ArrayList<Ball> balls, Table table) {
        // For each ball's x and y coordinates
        // If they are out of bounds, reset them to edge
        // Else readjust position to account for edgeWidth
        for (Ball ball : balls) {
            //x coordinate
            if (isOutOfBoundsLeft(ball.getxPosition()+ballRadius)) {
                ball.setxPosition(edgeWidth+ballRadius);
            }

            else if (isOutOfBoundsRight(ball.getxPosition(), table.getWidth())) {
                ball.setxPosition(table.getWidth()+edgeWidth-ballRadius);
            }

            else {
                ball.setxPosition(ball.getxPosition()+edgeWidth);
            }

            //y coordinate
            if (isOutOfBoundsUp(ball.getyPosition()+ballRadius)) {
                ball.setyPosition(edgeWidth+ballRadius);
            }

            else if (isOutOfBoundsDown(ball.getyPosition()+ballRadius, table.getHeight())) {
                ball.setyPosition(table.getHeight()+edgeWidth-ballRadius);
            }

            else {
                ball.setyPosition(ball.getyPosition()+edgeWidth);
            }

            // Add the ball to the pane
            pane.getChildren().add(ball.getBall());
        }
    }

    /**
     * Sets the mouse events for
     * the drawing of the cue
     * @param pane Layout pane for the game scene
     */
    private void setMouseEvents(Pane pane) {

        // When mouse is pressed, create a cue object
        pane.setOnMousePressed(event -> {
            isCueSet = false;
            cue = new Line(event.getX(), event.getY(), event.getX(), event.getY());
            cue.setStrokeWidth(5);
            cue.setStroke(Color.BURLYWOOD);
            pane.getChildren().add(cue);
        });

        // When dragged, adjust cue to follow
        // mouse as a straight line
        pane.setOnMouseDragged(event -> {
            cue.setEndX(event.getX());
            cue.setEndY(event.getY());
        });

        // When released, remove the cue from the
        // pane and indicate that cue has been set
        pane.setOnMouseReleased(event -> {
            pane.getChildren().remove(cue);
            isCueSet = true;
        });
    }

    /**
     * Sets the game by creating the scene and
     * adding the table, pockets and balls to it
     * @param primaryStage The stage that the scene is set on
     * @param table The Table object
     * @param balls The list of balls
     * @return The pane with all objects added to it
     */
    private Pane setUpGame(Stage primaryStage, Table table, ArrayList<Ball> balls) {
        Rectangle tableShape = table.getShape();

        Pane pane = new Pane();
        // Creating a Scene large enough to contain the table and the edges
        Scene scene = new Scene(pane,tableShape.getWidth()+2*edgeWidth,tableShape.getHeight()+2*edgeWidth);
        //setting color to the scene
        scene.setFill(Color.rgb(165, 70, 7));

        // Adds the table drawing to the pane
        pane.getChildren().add(tableShape);
        // Adds the pockets to the pane
        addpockets(pane, table);
        // Adjusts the initial ball positions
        // To account for the edgeWidth
        adjustBallPositions(pane, balls, table);

        setMouseEvents(pane);

        primaryStage.setTitle("Pool Game");

        //Adding the scene to Stage
        primaryStage.setScene(scene);
        //Displaying the contents of the stage
        primaryStage.show();

        return pane;
    }

    /**
     * Updates the velocity of a ball
     * @param ball The ball object
     * @param newVelocity 2D object containing x,y velocities
     */
    private void updateVelocity(Ball ball, Point2D newVelocity) {
        ball.setxVelocity(newVelocity.getX());
        ball.setyVelocity(newVelocity.getY());
    }

    /**
     * Calculates the distance between
     * two sets of coordinates
     * @param x1 First x coordinate
     * @param y1 First y coordinate
     * @param x2 Second x coordinate
     * @param y2 Second y coordinate
     * @return The distance between the two sets
     */
    private double calculateDistance(double x1, double y1, double x2, double y2) {
        double deltaX = x1 - x2;
        double deltaY = y1 - y2;
        return Math.sqrt((deltaX*deltaX) + (deltaY*deltaY));
    }

    /**
     * Checks if a ball is close enough
     * to any other ball to set up a collision
     * @param ball The ball object
     * @param balls The list of balls
     * @param ballIndex The index of the ball in balls (allows it to be skipped)
     */
    private void detectCollisions(Ball ball, ArrayList<Ball> balls, int ballIndex) {

        // For each ball in balls, check to see if there
        // is at least a diameter distance between them
        // Set up collision if so
        for (int j = 0; j < balls.size(); j++) {
            // If index of original ball, skip
            if (ballIndex == j) {
                continue;
            }

            Ball otherBall = balls.get(j);

            // Calculates distance between two balls
            double distance = calculateDistance(ball.getxPosition(), ball.getyPosition(), otherBall.getxPosition(), otherBall.getyPosition());

            // Sets up collision if close enough
            if (distance <= 2*ballRadius) {
                setUpCollision(ball, otherBall);
            }
        }
    }

    /**
     * Gets the positions, velocities and masses
     * of two balls to set up a collision, then
     * updates the new velocities based on the collision
     * @param ball The ball object
     * @param otherBall The second ball object
     */
    private void setUpCollision(Ball ball, Ball otherBall) {

        // Gets the positions and velocities of both balls
        Point2D ballPos = new Point2D(ball.getxPosition(), ball.getyPosition());
        Point2D ballVel = new Point2D(ball.getxVelocity(), ball.getyVelocity());

        Point2D otherPos = new Point2D(otherBall.getxPosition(), otherBall.getyPosition());
        Point2D otherVel = new Point2D(otherBall.getxVelocity(), otherBall.getyVelocity());

        // Collide the two objects and return new velocities
        Point2D newVels[] = collide(ballPos, ballVel, ball.getMass(), otherPos, otherVel, otherBall.getMass());

        // If new velocities are not null, update the balls
        if (newVels != null) {
            ballVel = newVels[0];
            otherVel = newVels[1];

            updateVelocity(ball, ballVel);
            updateVelocity(otherBall, otherVel);
        }

    }

    /**
     * Collides two balls and gets the updated
     * velocities
     * NOTE: THIS IS NOT MY OWN WORK AND WAS TAKEN
     * FROM THE ASSIGNMENT SPEC
     * @param positionA x,y position of the first ball
     * @param velocityA x,y velocity of the first ball
     * @param massA Mass of the first ball
     * @param positionB x,y position of the second ball
     * @param velocityB x,y velocity of the second ball
     * @param massB Mass of the second ball
     * @return Array of size 2, containing new x,y velocities for both balls
     */
    private Point2D[] collide(Point2D positionA, Point2D velocityA, double massA, Point2D positionB, Point2D velocityB, double massB) {

        // Find the angle of the collision - basically where is ball B relative to ball A. We aren't concerned with
        // distance here, so we reduce it to unit (1) size with normalize() - this allows for arbitrary radii
        Point2D collisionVector = positionA.subtract(positionB);
        collisionVector = collisionVector.normalize();

        // Here we determine how 'direct' or 'glancing' the collision was for each ball
        double vA = collisionVector.dotProduct(velocityA);
        double vB = collisionVector.dotProduct(velocityB);

        // If you don't detect the collision at just the right time, balls might collide again before they leave
        // each others' collision detection area, and bounce twice. This stops these secondary collisions by detecting
        // whether a ball has already begun moving away from its pair, and returns the original velocities
        if (vB <= 0 && vA >= 0) {
            return null;
        }

        // This is the optimisation function described in the gamasutra link. Rather than handling the full quadratic
        // (which as we have discovered allowed for sneaky typos) this is a much simpler - and faster - way of obtaining
        // the same results.
        double optimizedP = (2.0 * (vA - vB)) / (massA + massB);

        // Now we apply that calculated function to the pair of balls to obtain their final velocities
        Point2D velAPrime = velocityA.subtract(collisionVector.multiply(optimizedP).multiply(massB));
        Point2D velBPrime = velocityB.add(collisionVector.multiply(optimizedP).multiply(massA));

        Point2D newVels[] = new Point2D[2];

        newVels[0] = velAPrime;
        newVels[1] = velBPrime;

        return newVels;
    }

    /**
     * Updates a ball's position based on its velocity
     * @param ball The ball object
     */
    private void moveBall(Ball ball) {
        ball.setxPosition((ball.getxPosition()+ball.getxVelocity()));
        ball.setyPosition((ball.getyPosition()+ball.getyVelocity()));
    }

    /**
     * Removes a ball from the pane if it moves
     * into a pocket
     * Simulates falling into pocket
     * @param pane Layout pane for the game scene
     * @param ball The ball object
     * @param pockets The list of pockets
     * @param balls The list of balls
     */
    private void fallInPocket(Pane pane, Ball ball, Circle pockets[], ArrayList<Ball> balls) {
        for (Circle pocket : pockets) {
            // For each pocket, calculate the distance between its centre and the ball
            double distance = calculateDistance(ball.getxPosition(), ball.getyPosition(), pocket.getCenterX(), pocket.getCenterY());

            // If there is less than one ball diameter between the two
            // Remove ball from pane
            if (distance <= 2*ballRadius) {
                balls.remove(ball);
                pane.getChildren().remove(ball.getBall());
            }
        }
    }

    /**
     * Adjusts the cue's velocity based on
     * its direction
     * @param ball The ball object
     * @param xVelocity x velocity of the cue
     * @param yVelocity y velocity of the cue
     * @return Point2D of new cue x,y velocity
     */
    private Point2D getCueDirection(Ball ball, double xVelocity, double yVelocity) {
        // If cue is to the right of the ball
        // Set cue velocity to the left
        if (cue.getEndX() >= ball.getxPosition()) {
            xVelocity *= -1;
        }

        // If cue is below the ball
        // Set cue velocity upwards
        if (cue.getEndY() >= ball.getyPosition()) {
            yVelocity *= -1;
        }

        return new Point2D(xVelocity, yVelocity);
    }

    /**
     * Sets the power of the cue velocity based
     * on the cue's length, then calls the getCueDirection
     * method and returns new velocities
     * @param ball The ball object
     * @param table The table object
     * @return Point2D of cue new x,y velocity
     */
    private Point2D findCueVelocity(Ball ball, Table table) {

        double xVelocity = 0;
        double yVelocity = 0;
        double length = table.getWidth()/2;

        // Calculate length of cue
        double cueLength = calculateDistance(cue.getStartX(), cue.getStartY(), cue.getEndX(), cue.getEndY());

        // If cue greater than half of table width
        // Set velocity to 1.5
        if (cueLength >= length) {
            xVelocity = 1.5;
            yVelocity = 1.5;
        }

        // If cue between half and a quarter of table length
        // Set velocity to 1
        else if (cueLength < length && cueLength >= length/2) {
            xVelocity = 1;
            yVelocity = 1;
        }

        // If cue between a quarter and an eighth of table length
        // Set velocity to 0.5
        else if (cueLength < length/2 && cueLength >= length/4) {
            xVelocity = 0.5;
            yVelocity = 0.5;
        }

        // If cue less than an eighth of table length
        // Set velocity to 0.25
        else if (cueLength < length/4) {
            xVelocity = 0.25;
            yVelocity = 0.25;
        }

        // Find directions and return new velocities
        return getCueDirection(ball, xVelocity, yVelocity);
    }

    /**
     * Strikes a ball with the cue stick if
     * it start of the cue is within the ball's radius
     * @param ball The ball object
     * @param table The table object
     */
    private void strikeBall(Ball ball, Table table) {

        // Calculate distance between start of cue and ball centre
        Double distance = calculateDistance(ball.getxPosition(), ball.getyPosition(), cue.getStartX(), cue.getStartY());

        // If distance within ball radius
        // Collide the cue and ball
        // Update ball's velocity based on collision
        if (distance <= ballRadius) {

            // Gets the positions and velocities of ball and end of cue
            Point2D ballPos = new Point2D(ball.getxPosition(), ball.getyPosition());
            Point2D ballVel = new Point2D(ball.getxVelocity(), ball.getyVelocity());

            Point2D otherPos = new Point2D(cue.getEndX(), cue.getEndY());
            Point2D otherVel = findCueVelocity(ball, table);

            // Collides the ball and cue
            Point2D newVels[] = collide(ballPos, ballVel, ball.getMass(), otherPos, otherVel, 1);

            // Updates the velocity
            if (newVels != null) {
                updateVelocity(ball, newVels[0]);
            }

            // Indicate that the player holds the cue again
            isCueSet = false;
        }
    }

    /**
     * Bounces balls off the edges
     * @param ball A ball object
     * @param table The table object
     */
    private void bounceOffWalls(Ball ball, Table table) {
        // if the ball goes out of the left or right bounds
        // Reverse its x velocity
        if (isOutOfBoundsRight(ball.getxPosition() + ballRadius + ball.getxVelocity() - edgeWidth, table.getWidth()) || isOutOfBoundsLeft(ball.getxPosition() - ballRadius + ball.getxVelocity())) {
            double xVelocity = ball.getxVelocity();
            ball.setxVelocity(xVelocity *= -1);
        }

        // If the ball goes out of the upper or lower bounds
        // Reverse its y velocity
        if (isOutOfBoundsDown(ball.getyPosition() + ballRadius + ball.getyVelocity() - edgeWidth, table.getHeight()) || isOutOfBoundsUp(ball.getyPosition() - ballRadius + ball.getyVelocity())) {
            double yVelocity = ball.getyVelocity();
            ball.setyVelocity(yVelocity *= -1);
        }
    }

    /**
     * Applies friction to a ball, slowing
     * down its x and y velocities
     * @param ball A ball object
     * @param table The table object
     */
    private void applyFriction(Ball ball, Table table) {
        // If x velocity > 0, subtract friction from it
        if (ball.getxVelocity() > 0) {
            ball.setxVelocity(ball.getxVelocity() + (table.getFriction()*-(0.001)));
        }

        // Else if x velocity < 0 add friction to it
        // Still slows down, but in opposite direction
        else if (ball.getxVelocity() < 0) {
            ball.setxVelocity(ball.getxVelocity() - (table.getFriction()*-(0.001)));
        }

        // Else if x velocity small enough, set to rest state
        if (ball.getxVelocity() <= 0.001 && ball.getxVelocity() > -0.001) {
            ball.setxVelocity(restState);
        }

        // If y velocity > 0, subtract friction from it
        if (ball.getyVelocity() > 0) {
            ball.setyVelocity(ball.getyVelocity() + (table.getFriction()*-(0.001)));
        }

        // Else if y velocity < 0 add friction to it
        // Still slows down, but in opposite direction
        else if (ball.getyVelocity() < 0) {
            ball.setyVelocity(ball.getyVelocity() - (table.getFriction()*-(0.001)));
        }

        // Else if y velocity small enough, set to rest state
        if (ball.getyVelocity() <= 0.001 && ball.getyVelocity() > -0.001) {
            ball.setyVelocity(restState);
        }
    }

    /**
     * Determines whether a ball is at
     * rest state
     * @param ball A ball object
     * @return true if x,y velocities at rest state, false otherwise
     */
    public boolean isAtRestState(Ball ball) {
        return ball.getxVelocity() == restState && ball.getyVelocity() == restState;
    }

    /**
     * Determines whether a ball is the cue
     * ball or not based on its colour
     * @param ball A ball object
     * @return true if ball is cue, false otherwise
     */
    public boolean isCueBall(Ball ball) {
        return ball.getColour().equalsIgnoreCase("white");
    }

    /**
     * Runs the game
     * @param primaryStage Stage at which the scene is set on
     */
    @Override
    public void start(Stage primaryStage) {
        // Reads the config file into a JSONObject
        JSONObject objects = getConfig(arguments[0]);

        // Gets tableContents and array of ball contents from objects
        JSONObject tableContents = (JSONObject) objects.get("Table");
        JSONArray balls = (JSONArray) ((JSONObject) objects.get("Balls")).get("ball");

        // Concrete factory used to make table and balls
        PoolGameFactory factory = PoolGameFactory.getInstance();

        Table table = factory.makeTable(tableContents, edgeWidth);

        ArrayList<Ball> poolBalls = new ArrayList<>();

        for (int i = 0; i < balls.size(); i++) {
            poolBalls.add(factory.makeBall((JSONObject) balls.get(i), ballRadius));
        }

        try {
            // Sets up the game, adding all objects to pane
            Pane pane = setUpGame(primaryStage, table, poolBalls);
            Circle pockets[] = table.getPockets();

            // Animates the game
            AnimationTimer animator = new AnimationTimer() {
                /**
                 * Loops over the scene frame by frame
                 * @param now timestamp of current frame in nanoseconds (from oracle docs)
                 */
                @Override
                public void handle(long now) {

                    // For each ball on the table
                    for (int i = 0; i < poolBalls.size(); i++) {
                        Ball ball = poolBalls.get(i);

                        // Move the ball based on its velocity
                        moveBall(ball);

                        // Falls into pocket if within pocket's radius
                        fallInPocket(pane, ball, pockets, poolBalls);

                        // If the player has let go of the cue, the ball is the cue,
                        // and is at rest, strike the ball
                        if (isCueSet && isCueBall(ball) && isAtRestState(ball)) {
                            strikeBall(ball, table);
                        }

                        // Detect collisions between balls and update velocities
                        // based on them
                        detectCollisions(ball, poolBalls, i);

                        // If ball reaches boundary, bounce off it
                        bounceOffWalls(ball, table);

                        // Updates the ball drawing's coordinates
                        ball.getBall().setCenterX(ball.getxPosition());
                        ball.getBall().setCenterY(ball.getyPosition());

                        // Slows down ball based on table velocity
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