package geometries;
import primitives.*;

public class Plane extends Geometry {
    private final Point point;
    private final Vector normal;

    public Plane(Point point, Vector normal) {
        this.point = point;
        this.normal = normal;
    }
    public Plane(Point p1,Point p2,Point p3) {
        this.normal = p2.subtract(p1).crossProduct(p2.subtract(p3));
        this.point = p2;
    }

    @Override
    public Vector getNormal(Point point) {
        return normal;
    }
}
