package primitives;

import geometries.Intersectable.Intersection;

import java.util.List;
import java.util.Objects;

import static primitives.Util.isZero;

/**
 * Represents a ray in 3D space, defined by a starting point and a direction vector.
 */
public class Ray {
    /**
     * The direction vector of the ray. It is always normalized.
     */
    private Vector vector;

    /**
     * The starting point of the ray.
     */
    private Point head;

    /**
     * get method for direction
     * @return the direction
     */
    public Vector getVector() { return vector; }

    /**
     * get method for point
     * @return the base point
     */
    public Point getHead() { return head; }

    /**
     *
     * @param t
     * @return
     */
    public Point getPoint(double t) {
        if (Util.isZero(t)) return head;
        return head.add(vector.scale(t));
    }

    /**
     * Constructs a Ray with a given direction vector and starting point.
     * The direction vector is automatically normalized.
     *
     * @param point  The starting point of the ray
     * @param vector The direction vector of the ray
     */
    public Ray(Point point, Vector vector) {
        this.vector = vector.normalize(); // Ensure the direction vector is normalized
        this.head = point;
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

    public Point findClosestPoint(List<Point> intersections) {
        return intersections == null ? null
                : findClosestIntersection(
                intersections.stream()
                        .map(p -> new Intersection(null, p))
                        .toList()
        ).point;
    }


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
