import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class Ball {

    private String colour;
    private double xPosition;
    private double yPosition;
    private double xInitialVel;
    private double yInitialVel;
    private double xVelocity;
    private double yVelocity;
    private double mass;
    private Circle ball;

    public Ball(String colour, double xPosition, double yPosition, double xVelocity, double yVelocity, double mass, Circle ball) {
        this.colour = colour;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.xVelocity = xVelocity;
        this.yVelocity = yVelocity;
        this.mass = mass;
        this.ball = ball;
    }

    public String getColour() { return colour; }

    public double getxPosition() {return this.xPosition;}

    public void setxPosition(double newxPosition) {this.xPosition = newxPosition;}

    public double getyPosition() {return this.yPosition;}

    public void setyPosition(double newyPosition) {this.yPosition = newyPosition;}

    public double getxVelocity() { return this.xVelocity; }

    public void setxVelocity(double xVelocity) {
        this.xVelocity = xVelocity;
    }

    public double getyVelocity() {
        return this.yVelocity;
    }

    public void setyVelocity(double yVelocity) { this.yVelocity = yVelocity; }

    public double getMass() {return this.mass;}

    public Circle getBall() { return this.ball; }
}
