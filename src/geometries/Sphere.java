package geometries;

import primitives.Ray;
import primitives.Point;
import primitives.Vector;

import java.util.List;
import java.util.ArrayList;


/**
 * Represents a sphere in 3D space, defined by a center point and a radius.
 * <p>
 * This class extends {@link RadialGeometry} since a sphere is a type of radial geometry.
 * </p>
 */
public class Sphere extends RadialGeometry {
    /**
     * the sphere's center point
     */
    private final Point center;

    /**
     * Constructs a Sphere with a given radius and center point.
     *
     * @param radius The radius of the sphere (must be greater than 0).
     * @param center The center point of the sphere.
     */
    public Sphere(double radius, Point center) {
        super(radius); // send to DAD ctor
        this.center = center; // and set the new fileds
    }

    /**
     * Returns the normal vector to the sphere at a given point.
     * <p>
     * This method is currently not implemented and returns {@code null}.
     * </p>
     *
     * @param point The point on the sphere's surface.
     * @return The normal vector at the given point (currently returns {@code null}).
     */
    @Override
    public Vector getNormal(Point point) { return point.subtract(center).normalize(); }

    @Override
    public List<Point> findIntersections(Ray ray) {


        Vector dir = ray.getVector(); // Get the direction of the ray
        Point origin = ray.getHead(); // Get the starting point (head) of the ray

        // If the ray starts at the center of the sphere, we need to handle it differently
        if (origin.equals(center)) {
            // If the ray starts at the center, any direction from the center will intersect the sphere at one point
            // Add the point on the surface of the sphere in the direction of the ray with radius distance
            return List.of(ray.getPoint(radius)); // Intersection point on the surface of the sphere
        }

        // Vector from the ray's origin to the center of the sphere
        Vector L = origin.subtract(center);

        // Calculate the coefficients for the quadratic equation: a*t^2 + b*t + c = 0
        double a = dir.dotProduct(dir); // a = dir·dir (dot product of the ray direction with itself)
        double b = 2 * dir.dotProduct(L); // b = 2 * dir·L (dot product of the ray direction and vector from origin to center)
        double c = L.dotProduct(L) - radius * radius; // c = L·L - radius^2 (dot product of L with itself minus the square of the radius)

        // Calculate the discriminant (b^2 - 4ac)
        double discriminant = b * b - 4 * a * c;

        // If the discriminant is negative, no intersection exists
        if (discriminant < 0) {
            return null; // No intersection
        }

        List<Point> intersections = new ArrayList<>(); // List to hold intersection points

        // Calculate the two possible solutions (t1 and t2)
        double t1 = (-b - Math.sqrt(discriminant)) / (2 * a); // First solution
        double t2 = (-b + Math.sqrt(discriminant)) / (2 * a); // Second solution

        // If t1 is greater than 0, add it to the intersection list
        if (t1 > 0) {
            intersections.add(ray.getPoint(t1)); // Calculate and add intersection point for t1
        }

        // If t2 is greater than 0 and different from t1, add it to the intersection list
        if (t2 > 0 && t2 != t1) {
            intersections.add(ray.getPoint(t2)); // Calculate and add intersection point for t2
        }

        // Return the intersection points, or null if no valid intersections
        return intersections.isEmpty() ? null : intersections;

    }
}

