package geometries;
import primitives.Util;
import primitives.Vector;
import primitives.Point;
import primitives.Ray;

import java.util.ArrayList;
import java.util.List;

import static primitives.Util.alignZero;

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
    public Triangle(Point p1, Point p2, Point p3) {
        super((Point) List.of(p1,p2,p3)); // send to DAD ctor the list converted to array of vertices
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        List<Point> intersections = this.plane.findIntersections(ray);
        if (intersections == null) return null;

        Point a = this.vertices.get(0);
        Point b = this.vertices.get(1);
        Point c = this.vertices.get(2);
        Point p = intersections.getFirst();
        Point rayPoint = ray.getHead();
        Vector rayVector = ray.getVector();


        Vector v0 = a.subtract(rayPoint);
        Vector v1 = b.subtract(rayPoint);
        Vector v2 = c.subtract(rayPoint);

        Vector normal1 = v0.crossProduct(v1).normalize();
        Vector normal2 = v1.crossProduct(v2).normalize();
        Vector normal3 = v2.crossProduct(v0).normalize();

        final double res1 = alignZero(rayVector.dotProduct(normal1));
        final double res2 = alignZero(rayVector.dotProduct(normal2));
        final double res3 = alignZero(rayVector.dotProduct(normal3));

        if (res1 > 0 && res2 > 0 && res3 > 0 || res1 < 0 && res2 < 0 && res3 < 0) {
            return intersections;
        } else {
            return null;
        }

    }
}

