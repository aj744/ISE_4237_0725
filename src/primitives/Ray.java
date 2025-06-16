package primitives;

import geometries.Intersectable.Intersection;

import java.util.List;
import java.util.Objects;
import primitives.Util.*;

import static primitives.Util.isZero;

/**
 * Represents a ray in 3D space, defined by a starting point and a direction vector.
 * The direction vector is always normalized.
 */
public class Ray {
    /**
     * distance for moving the head point
     */
    private static final double DELTA = 0.1;
    /**
     * The direction vector of the ray. It is always normalized.
     */
    private Vector vector;

    /**
     * The starting point (origin) of the ray.
     */
    private Point head;

    /**
     * Returns the direction vector of the ray.
     *
     * @return the direction vector
     */
    public Vector getDirection() { return vector; }

    /**
     * Returns the starting point (origin) of the ray.
     *
     * @return the starting point
     */
    public Point getHead() { return head; }

    /**
     * Calculates a point along the ray at a given distance 't' from the origin.
     *
     * @param t the distance along the ray (scalar)
     * @return the point at distance 't' from the origin
     */
    public Point getPoint(double t) {
        if (isZero(t)) return head;
        return head.add(vector.scale(t));
    }

    /**
     * Constructs a Ray with a given starting point and direction vector.
     * The direction vector is automatically normalized.
     *
     * @param point  The starting point of the ray
     * @param vector The direction vector of the ray
     */
    public Ray(Point point, Vector vector) {
        this.vector = vector.normalize(); // Ensure the direction vector is normalized
        this.head = point;
    }

    public Ray(Point point, Vector direction, Vector normal) {
        this.vector = direction;
        double vn = direction.dotProduct(normal);
        if (isZero(vn)) {
            this.head = point;
        }
        else if (vn > 0) {
            this.head = point.add(normal.scale(DELTA));
        }
        else {
            this.head = point.add(normal.scale(-DELTA));
        }
    }

    @Override
    public String toString() {
        return head.toString() + "+" + vector.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Ray other)
                && this.head.equals(other.head)
                && this.vector.equals(other.vector);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vector, head);
    }

    /**
     * Finds the closest point to the ray's origin from a list of intersection points.
     *
     * @param intersections list of intersection points
     * @return the closest point to the ray's origin, or null if the list is null or empty
     */
    public Point findClosestPoint(List<Point> intersections) {
        return intersections == null ? null
                : findClosestIntersection(
                intersections.stream()
                        .map(p -> new Intersection(null, p))
                        .toList()
        ).point;
    }

    /**
     * Finds the closest intersection to the ray's origin from a list of intersections.
     *
     * @param intersections list of intersections
     * @return the closest intersection to the ray's origin, or null if the list is null or empty
     */
    public Intersection findClosestIntersection(List<Intersection> intersections) {
        if(intersections == null || intersections.isEmpty()) {
            return null;
        }
        Intersection closest = intersections.getFirst();
        for (Intersection intersection : intersections) {
            if(intersection.point.distanceSquared(head) < closest.point.distanceSquared(head)) {
                closest = intersection;
            }
        }
        return closest;
    }
}
