package geometries;

import primitives.BoundingBox;
import primitives.Ray;
import java.util.List;

/**
 * Represents a node in the Bounding Volume Hierarchy
 */
public class BVHNode {
    /** The bounding box*/
    private final BoundingBox boundingBox;
    /** The node's left child */
    private final BVHNode leftChild;
    /** The node's right child */
    private final BVHNode rightChild;
    /** The node's geometries */
    private final List<Intersectable> geometries;
    /** Is the node a leaf */
    private final boolean isLeaf;

    /**
     * Constructor for leaf node
     * @param boundingBox bounding box of the node
     * @param geometries list of geometries in this leaf
     */
    public BVHNode(BoundingBox boundingBox, List<Intersectable> geometries) {
        this.boundingBox = boundingBox;
        this.geometries = geometries;
        this.isLeaf = true;
        this.leftChild = null;
        this.rightChild = null;
    }

    /**
     * Constructor for internal node
     * @param boundingBox bounding box of the node
     * @param leftChild left child node
     * @param rightChild right child node
     */
    public BVHNode(BoundingBox boundingBox, BVHNode leftChild, BVHNode rightChild) {
        this.boundingBox = boundingBox;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
        this.geometries = null;
        this.isLeaf = false;
    }

    /**
     * Gets the geometries in this leaf node
     * @return list of geometries
     */
    public List<Intersectable> getGeometries() {
        return geometries;
    }

    /**
     * Traverses the BVH and finds all intersections with a ray
     * @param ray the ray to test
     * @param intersections list to collect intersections
     */
    public void findIntersections(Ray ray, List<Intersectable.Intersection> intersections) {
        // Early exit if ray doesn't intersect bounding box
        if (!boundingBox.intersects(ray)) {
            return;
        }

        if (isLeaf) {
            // Test intersection with all geometries in this leaf
            for (Intersectable geometry : geometries) {
                List<Intersectable.Intersection> geoIntersections = geometry.calculateIntersections(ray);
                if (geoIntersections != null) {
                    intersections.addAll(geoIntersections);
                }
            }
        } else {
            // Recursively test children
            if (leftChild != null) {
                leftChild.findIntersections(ray, intersections);
            }
            if (rightChild != null) {
                rightChild.findIntersections(ray, intersections);
            }
        }
    }

    /**
     * Traverses the BVH and finds intersections within a maximum distance
     * @param ray the ray to test
     * @param maxDistance maximum distance to consider
     * @param intersections list to collect intersections
     */
    public void findIntersections(Ray ray, double maxDistance, List<Intersectable.Intersection> intersections) {
        // Early exit if ray doesn't intersect bounding box within distance
        if (!boundingBox.intersects(ray, maxDistance)) {
            return;
        }

        if (isLeaf) {
            // Test intersection with all geometries in this leaf
            for (Intersectable geometry : geometries) {
                List<Intersectable.Intersection> geoIntersections = geometry.calculateIntersections(ray, maxDistance);
                if (geoIntersections != null) {
                    intersections.addAll(geoIntersections);
                }
            }
        } else {
            // Recursively test children
            if (leftChild != null) {
                leftChild.findIntersections(ray, maxDistance, intersections);
            }
            if (rightChild != null) {
                rightChild.findIntersections(ray, maxDistance, intersections);
            }
        }
    }
}