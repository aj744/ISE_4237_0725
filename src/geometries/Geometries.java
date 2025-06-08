package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * The {@code Geometries} class represents a collection of geometric objects
 * (that extend {@link Intersectable}) and provides functionality to manage and
 * find intersections between a {@link Ray} and the entire collection.
 */
public class Geometries extends Intersectable {
    /**
     * A list containing all geometric objects in this collection.
     */
    private final List<Intersectable> geometries = new LinkedList<>();

    /**
     * Constructs a new {@code Geometries} object initialized with the given geometries.
     *
     * @param geometries one or more geometric objects to add to the collection
     */
    public Geometries(Intersectable... geometries) {
        add(geometries);
    }

    /**
     * Adds one or more geometric objects to the collection.
     *
     * @param geometries the geometric objects to add
     */
    public void add(Intersectable... geometries) {
        this.geometries.addAll(Arrays.asList(geometries));
    }


    /**
     * Helper method that calculates all intersections between the given {@code ray}
     * and the geometries in the collection.
     *
     * @param ray the ray to intersect with the geometries
     * @return a list of {@link Intersection} points, or {@code null} if there are no intersections
     */
    @Override
    protected List<Intersection> calculateIntersectionsHelper(Ray ray, double maxDistance) {
        List<Intersection> intersections = null;
        for (Intersectable geometry : this.geometries) {
            List<Intersection> geometryIntersections = geometry.calculateIntersectionsHelper(ray, maxDistance);
            if (geometryIntersections != null) {
                if (intersections == null) {
                    intersections = new LinkedList<>();
                }
                intersections.addAll(geometryIntersections);
            }
        }
        return intersections;
    }
}
