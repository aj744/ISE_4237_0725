package geometries;

import java.util.List;
import static primitives.Util.*;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * Polygon class represents a two-dimensional convex polygon in 3D Cartesian coordinate system.
 * The polygon is defined by an ordered list of vertices and lies entirely in a single plane.
 * <p>
 * The constructor enforces convexity and planar conditions.
 * </p>
 * Extends {@link Geometry}, thus inherits emission color, material, and intersection functionality.
 *
 * @author Dan
 */
public class Polygon extends Geometry {

    /**
     * List of the polygon's vertices in order.
     */
    protected final List<Point> vertices;

    /**
     * The plane in which the polygon lies.
     */
    protected final Plane plane;

    /**
     * The number of vertices in the polygon.
     */
    private final int size;

    /**
     * Constructs a convex polygon from an ordered list of vertices.
     * <p>
     * The vertices must be:
     * <ul>
     *     <li>At least 3 in number</li>
     *     <li>Ordered by edge path</li>
     *     <li>In the same plane</li>
     *     <li>Forming a convex shape</li>
     * </ul>
     * </p>
     *
     * @param vertices list of vertices in order
     * @throws IllegalArgumentException if:
     * <ul>
     *     <li>There are fewer than 3 vertices</li>
     *     <li>Two consecutive vertices are the same</li>
     *     <li>The vertices are not in the same plane</li>
     *     <li>The order of vertices does not define a proper convex polygon</li>
     * </ul>
     */
    public Polygon(Point... vertices) {
        if (vertices.length < 3)
            throw new IllegalArgumentException("A polygon can't have less than 3 vertices");

        this.vertices = List.of(vertices);
        size = vertices.length;

        // Define the supporting plane from the first 3 vertices
        plane = new Plane(vertices[0], vertices[1], vertices[2]);

        if (size == 3) return; // A triangle is always convex

        Vector n = plane.getNormal(vertices[0]);

        Vector edge1 = vertices[size - 1].subtract(vertices[size - 2]);
        Vector edge2 = vertices[0].subtract(vertices[size - 1]);

        // Determine initial sign of cross product dot normal (for convexity check)
        boolean positive = edge1.crossProduct(edge2).dotProduct(n) > 0;

        for (var i = 1; i < size; ++i) {
            // Check all vertices are in the same plane
            if (!isZero(vertices[i].subtract(vertices[0]).dotProduct(n)))
                throw new IllegalArgumentException("All vertices of a polygon must lay in the same plane");

            edge1 = edge2;
            edge2 = vertices[i].subtract(vertices[i - 1]);

            // Check that all cross products have the same sign
            if (positive != (edge1.crossProduct(edge2).dotProduct(n) > 0))
                throw new IllegalArgumentException("All vertices must be ordered and the polygon must be convex");
        }
    }

    /**
     * Returns the normal vector to the polygon at the given point.
     * <p>
     * Since the polygon lies entirely in one plane, its normal is constant.
     * </p>
     *
     * @param point a point on the polygon (unused)
     * @return the normal vector of the polygon
     */
    @Override
    public Vector getNormal(Point point) {
        return plane.getNormal(point);
    }

    /**
     * Computes the intersection between the polygon and a ray.
     * <p>
     * Currently unimplemented — returns {@code null}.
     * </p>
     *
     * @param ray the ray to intersect
     * @return {@code null} (no intersection logic implemented)
     */
    @Override
    protected List<Intersection> calculateIntersectionsHelper(Ray ray) {
        return null;
    }
}
