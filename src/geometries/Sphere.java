package geometries;

import primitives.Ray;
import primitives.Point;
import primitives.Util;
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
    public List<Intersection> calculateIntersectionsHelper(Ray ray) {

        Point p0 = ray.getHead();
        Vector dir = ray.getVector();

        if (p0.equals(center)) {
            // The ray starts at the center of the sphere
            return List.of(
                    new Intersection(this,
                            p0.add(dir.scale(radius))));
        }
        Vector u = center.subtract(p0); // vector from p0 to the center
        // The distance from p0 to the point that creates right angled triangle with the center, we'ill mark the point as p1
        double tm = dir.dotProduct(u);

        double d = Math.sqrt(u.dotProduct(u) - tm * tm); // The distance from the center to p1

        if (d >= radius) {
            // The ray is outside the sphere
            return null;
        }

        double th = Math.sqrt(radius * radius - d * d); //The distance from p1 to intersections on the line of the ray
        double t1 = Util.alignZero( tm - th); // The distance from the head of the ray to the closer intersection
        double t2 = Util.alignZero(tm + th); // The distance from the head of the ray to the further intersection

        List<Intersection> intersections = new ArrayList<>();
        if (t1 > 0) {
            intersections.add(
                    new Intersection(this,
                            ray.getPoint(t1)));
        }
        if (t2 > 0) {
            intersections.add(
                    new Intersection(this,
                            ray.getPoint(t2)));
        }
        return intersections.isEmpty() ? null : intersections;
    }
}

