public class BallBuilder implements Builder {

    private String colour;
    private double xPosition;
    private double yPosition;
    private double xVelocity;
    private double yVelocity;
    private double mass;

    @Override
    public void setParameters(String[] contents) {

        this.colour = contents[0];
        this.xPosition = Double.parseDouble(contents[1]);
        this.yPosition = Double.parseDouble(contents[2]);
        this.xVelocity = Double.parseDouble(contents[3]);
        this.yVelocity = Double.parseDouble(contents[4]);
        this.mass = Double.parseDouble(contents[5]);
    }

    public Ball getBall() {
        return new Ball(this.colour, this.xPosition, this.yPosition, this.xVelocity, this.yVelocity, this.mass);
    }
}
