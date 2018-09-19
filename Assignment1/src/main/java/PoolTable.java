import javafx.scene.shape.Rectangle;

public class PoolTable extends Table {

    public PoolTable(String colour, double width, double height, double friction, Rectangle table) {
        super(colour, width, height, friction, table);
    }

    @Override
    public String toString() {
        return "Pool Table";
    }
}
