package geometries;
import primitives.Point;
import java.util.List;

public class Triangle extends Polygon {

    public Triangle(List<Point> vertices) {
        super(vertices.toArray(new Point[0]));
    }


}
