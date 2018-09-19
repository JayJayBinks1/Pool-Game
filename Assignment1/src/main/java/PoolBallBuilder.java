import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class PoolBallBuilder implements BallBuilder {

    private String colour;
    private double xPosition;
    private double yPosition;
    private double xVelocity;
    private double yVelocity;
    private double mass;
    private Circle ball;

    @Override
    public void setColour(String colour) {
        this.colour = colour;
    }

    @Override
    public void setxPosition(double xPosition) {
        this.xPosition = xPosition;
    }

    @Override
    public void setyPosition(double yPosition) {
        this.yPosition = yPosition;
    }

    @Override
    public void setxVelocity(double xVelocity) {
        this.xVelocity = xVelocity;
    }

    @Override
    public void setyVelocity(double yVelocity) {
        this.yVelocity = yVelocity;
    }

    @Override
    public void setMass(double mass) {
        this.mass = mass;
    }

    @Override
    public void drawBall() {
        ball = new Circle(this.xPosition, this.yPosition, 15, Paint.valueOf(colour));
    }

    public PoolBall getBall() {
        return new PoolBall(this.colour, this.xPosition, this.yPosition, this.xVelocity, this.yVelocity, this.mass, this.ball);
    }
}
