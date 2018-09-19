import javafx.scene.shape.Circle;

public class PoolBall extends Ball {

    public PoolBall(String colour, double xPosition, double yPosition, double xVelocity, double yVelocity, double mass, Circle ball) {
        super(colour, xPosition, yPosition, xVelocity, yVelocity, mass, ball);
    }

    @Override
    public String toString() {
        return "Pool Ball";
    }
}
