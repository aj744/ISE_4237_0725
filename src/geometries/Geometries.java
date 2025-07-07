package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * The {@code Geometries} class represents a collection of geometric objects
 * (that extend {@link Intersectable}) and provides functionality to manage and
 * find intersections between a {@link Ray} and the entire collection.
 * This implementation uses a Bounding Volume Hierarchy (BVH) for efficient intersection testing.
 */
public class Geometries extends Intersectable {
    /**
     * A list containing all geometric objects in this collection.
     */
    private final List<Intersectable> geometries = new LinkedList<>();

    /**
     * The BVH builder for efficient intersection testing.
     */
    private BVHBuilder bvhBuilder;

    /**
     * Flag to track if the BVH needs to be rebuilt.
     */
    private boolean bvhNeedsUpdate = true;

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
        bvhNeedsUpdate = true; // Mark BVH for rebuild
    }

    /**
     * Builds or rebuilds the BVH tree from the current geometries.
     */
    private void buildBVH() {
        if (geometries.isEmpty()) {
            bvhBuilder = null;
            bvhNeedsUpdate = false;
            return;
        }

        bvhBuilder = new BVHBuilder();
        bvhBuilder.build(geometries);
        bvhNeedsUpdate = false;
    }

    /**
     * Helper method that calculates all intersections between the given {@code ray}
     * and the geometries in the collection using BVH acceleration.
     *
     * @param ray the ray to intersect with the geometries
     * @param maxDistance maximum distance to consider for intersections
     * @return a list of {@link Intersection} points, or {@code null} if there are no intersections
     */
    @Override
    protected List<Intersection> calculateIntersectionsHelper(Ray ray, double maxDistance) {
        // Build BVH if needed
        if (bvhNeedsUpdate) {
            buildBVH();
        }

        List<Intersection> intersections = null;
        // Use BVH for intersection testing if available
        if (bvhBuilder != null) {
            for (Intersectable geometry : geometries) {
                if (geometry instanceof Plane) {
                    List<Intersection> planeIntersections = geometry.calculateIntersections(ray, maxDistance);
                    if (planeIntersections != null){
                        if (intersections == null) {
                            intersections = new ArrayList<>();
                        }
                        intersections.addAll(planeIntersections);
                    }
                }
            }

            List<Intersection> total = bvhBuilder.findIntersections(ray, maxDistance);
            if (total != null && intersections != null) {
                total.addAll(intersections);
            }
            else if (total == null) {
                return intersections;
            }
            return total;
        }

        // Fallback to linear search if no BVH
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

    /**
     * Returns the number of geometries in this collection.
     *
     * @return the number of geometries
     */
    public int size() {
        return geometries.size();
    }

    /**
     * Checks if this collection is empty.
     *
     * @return true if the collection contains no geometries
     */
    public boolean isEmpty() {
        return geometries.isEmpty();
    }

    /**
     * Forces a rebuild of the BVH tree.
     * This can be useful if geometries have been modified externally.
     */
    public void rebuildBVH() {
        bvhNeedsUpdate = true;
    }
}