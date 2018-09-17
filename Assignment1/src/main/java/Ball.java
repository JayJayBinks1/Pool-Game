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

    public Ball(String colour, double xPosition, double yPosition, double xVelocity, double yVelocity, double mass) {
        this.colour = colour;

//        if (xPosition <= 50) {
//            this.xPosition = 75+xPosition;
//        }
//
//        else {
//            this.xPosition = xPosition-25;
//        }
//
//        if (yPosition <= 50) {
//            this.yPosition = 75+yPosition;
//        }
//
//        else {
//            this.yPosition = yPosition-25;
//        }

        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.xVelocity = xVelocity;
        this.yVelocity = yVelocity;
        this.mass = mass;
        this.ball = new Circle(this.xPosition, this.yPosition, 15, Paint.valueOf(colour));
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

    public double getxInitialVel() { return xInitialVel; }

    public double getyInitialVel() { return yInitialVel; }

    public double getMass() {return this.mass;}

    public Circle getBall() { return this.ball; }
}
