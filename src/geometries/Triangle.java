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
     * Constructs a triangle using three points.
     *
     * @param p1 The first vertex.
     * @param p2 The second vertex.
     * @param p3 The third vertex.
     */
    public Triangle(Point p1, Point p2, Point p3) {
        super(p1, p2, p3);
    }

    /**
     * Calculates intersection points between the triangle and a given ray.
     *
     * <p>This method first checks if the ray intersects the plane of the triangle.
     * If so, it checks whether the intersection point lies inside the triangle
     * using vector cross product signs.</p>
     *
     * @param ray The ray to intersect with the triangle.
     * @return A list containing the intersection if it exists, or {@code null} if there is no intersection.
     */
    @Override
    public List<Intersection> calculateIntersectionsHelper(Ray ray, double maxDistance) {
        List<Intersection> intersections = plane.calculateIntersectionsHelper(ray, maxDistance);
        if (intersections == null) {
            return null;
        }

        Point p = intersections.getFirst().point;

        // Check if the point is one of the vertices
        if (p.equals(vertices.get(0)) || p.equals(vertices.get(1)) || p.equals(vertices.get(2))) {
            return List.of(new Intersection(this, p));
        }

        // Check if the point is on one of the triangle's edges
        if (vertices.get(0).subtract(vertices.get(1)).normalize().equals(vertices.get(0).subtract(p).normalize())
                || vertices.get(1).subtract(vertices.get(2)).normalize().equals(vertices.get(1).subtract(p).normalize())
                || vertices.get(2).subtract(vertices.get(0)).normalize().equals(vertices.get(2).subtract(p).normalize())) {
            return null;
        }

        // Compute vectors from point p to triangle's vertices
        Vector v1 = vertices.get(0).subtract(p).normalize();
        Vector v2 = vertices.get(1).subtract(p).normalize();
        Vector v3 = vertices.get(2).subtract(p).normalize();

        // Degenerate check
        if (v1.equals(v2) || v1.equals(v3) || v2.equals(v3)) {
            return null;
        }

        // Opposite direction check
        if (v1.equals(v2.scale(-1)) || v1.equals(v3.scale(-1)) || v2.equals(v3.scale(-1))) {
            return List.of(new Intersection(this, p));
        }

        // Use cross products and dot products to determine if point is inside triangle
        double s1 = v1.crossProduct(v2).dotProduct(plane.getNormal());
        double s2 = v2.crossProduct(v3).dotProduct(plane.getNormal());
        double s3 = v3.crossProduct(v1).dotProduct(plane.getNormal());

        if ((s1 > 0 && s2 > 0 && s3 > 0) || (s1 < 0 && s2 < 0 && s3 < 0)) {
            return List.of(new Intersection(this, p));
        } else {
            return null;
        }
    }
}
