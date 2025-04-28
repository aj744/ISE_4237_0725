package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.List;

/**
 * Represents a triangle in 3D space.
 * <p>
 * A triangle is a special case of a polygon with exactly three vertices.
 * This class extends {@link Polygon} and ensures that a valid triangle is created.
 * </p>
 */
public class Triangle extends Polygon {

    /**
     * Constructs a triangle using a list of three vertices.
     *
     * @param vertices A list containing exactly three points that define the triangle.
     * @throws IllegalArgumentException if the list does not contain exactly three points.
     */
    public Triangle(List<Point> vertices) {
        super(vertices.toArray(new Point[0])); // send to DAD ctor the list converted to array of vertices
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        final var D = - this.getNormal().dotProduct(this.point.subtract(Point.ZERO));
        final var t = (-(this.normal.dotProduct(ray.getPoint().subtract(Point.ZERO)) - D)) / this.normal.dotProduct(ray.getVector());
        return t >= 1 ? null : List.of(ray.getPoint().add(ray.getVector().scale(t)));
    }
}

