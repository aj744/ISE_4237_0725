package geometries;

import primitives.BoundingBox;
import primitives.Point;
import primitives.Ray;
import java.util.*;

/**
 * Bounding Volume Hierarchy implementation for spatial acceleration
 */
public class BVHBuilder {
    private BVHNode root;
    private static final int MAX_PRIMITIVES_PER_LEAF = 4;
    private static final int MAX_DEPTH = 20;

    /**
     * Builds a BVH tree from a list of geometries
     * @param geometries list of geometries to organize
     */
    public void build(List<Intersectable> geometries) {
        if (geometries == null || geometries.isEmpty()) {
            root = null;
            return;
        }

        // Filter out geometries without valid bounding boxes
        List<Intersectable> validGeometries = new ArrayList<>();
        for (Intersectable geometry : geometries) {
            BoundingBox bbox = geometry.getBoundingBox();
            if (bbox != null) {
                validGeometries.add(geometry);
            }
        }

        if (validGeometries.isEmpty()) {
            root = null;
            return;
        }

        root = buildRecursive(validGeometries, 0);
    }

    /**
     * Recursively builds the BVH tree
     * @param geometries list of geometries with valid bounding boxes
     * @param depth current depth in the tree
     * @return root node of the subtree
     */
    private BVHNode buildRecursive(List<Intersectable> geometries, int depth) {
        if (geometries == null || geometries.isEmpty()) {
            return null;
        }

        // Calculate bounding box for all geometries
        BoundingBox nodeBounds = calculateBoundingBox(geometries);
        if (nodeBounds == null) {
            return null;
        }

        // Create leaf if we have few geometries or reached max depth
        if (geometries.size() <= MAX_PRIMITIVES_PER_LEAF || depth >= MAX_DEPTH) {
            return new BVHNode(nodeBounds, new ArrayList<>(geometries));
        }

        // Find the best axis to split on
        int bestAxis = findBestSplitAxis(geometries, nodeBounds);

        // Sort geometries by centroid position along the best axis
        final int splitAxis = bestAxis;
        geometries.sort((a, b) -> {
            Point centroidA = a.getBoundingBox().getCentroid();
            Point centroidB = b.getBoundingBox().getCentroid();
            return Double.compare(centroidA.getComponent(splitAxis), centroidB.getComponent(splitAxis));
        });

        // Split at the median
        int mid = geometries.size() / 2;
        List<Intersectable> leftGeometries = new ArrayList<>(geometries.subList(0, mid));
        List<Intersectable> rightGeometries = new ArrayList<>(geometries.subList(mid, geometries.size()));

        // Recursively build children
        BVHNode leftChild = buildRecursive(leftGeometries, depth + 1);
        BVHNode rightChild = buildRecursive(rightGeometries, depth + 1);

        // If one child is null, return the other
        if (leftChild == null && rightChild == null) {
            return new BVHNode(nodeBounds, new ArrayList<>(geometries));
        }
        if (leftChild == null) {
            return rightChild;
        }
        if (rightChild == null) {
            return leftChild;
        }

        return new BVHNode(nodeBounds, leftChild, rightChild);
    }

    /**
     * Calculates the bounding box that encompasses all geometries
     * @param geometries list of geometries with valid bounding boxes
     * @return combined bounding box, or null if no valid geometries
     */
    private BoundingBox calculateBoundingBox(List<Intersectable> geometries) {
        if (geometries == null || geometries.isEmpty()) {
            return null;
        }

        BoundingBox result = null;

        // Find first valid bounding box
        for (Intersectable geometry : geometries) {
            BoundingBox bbox = geometry.getBoundingBox();
            if (bbox != null) {
                result = bbox;
                break;
            }
        }

        if (result == null) {
            return null;
        }

        // Merge all valid bounding boxes
        for (Intersectable geometry : geometries) {
            BoundingBox bbox = geometry.getBoundingBox();
            if (bbox != null) {
                result = result.merge(bbox);
            }
        }

        return result;
    }

    /**
     * Finds the best axis to split on based on the largest extent
     * @param geometries list of geometries
     * @param nodeBounds bounding box of the node
     * @return best axis (0=X, 1=Y, 2=Z)
     */
    private int findBestSplitAxis(List<Intersectable> geometries, BoundingBox nodeBounds) {
        // Simple heuristic: split along the axis with the largest extent
        Point min = nodeBounds.getMin();
        Point max = nodeBounds.getMax();

        double extentX = max.getX() - min.getX();
        double extentY = max.getY() - min.getY();
        double extentZ = max.getZ() - min.getZ();

        if (extentX >= extentY && extentX >= extentZ) {
            return 0; // X axis
        } else if (extentY >= extentZ) {
            return 1; // Y axis
        } else {
            return 2; // Z axis
        }
    }

    /**
     * Finds all intersections with a ray
     * @param ray the ray to test
     * @return list of intersections, or null if none found
     */
    public List<Intersectable.Intersection> findIntersections(Ray ray) {
        if (root == null) {
            return null;
        }

        List<Intersectable.Intersection> intersections = new ArrayList<>();
        root.findIntersections(ray, intersections);
        return intersections.isEmpty() ? null : intersections;
    }

    /**
     * Finds intersections within a maximum distance
     * @param ray the ray to test
     * @param maxDistance maximum distance to consider
     * @return list of intersections, or null if none found
     */
    public List<Intersectable.Intersection> findIntersections(Ray ray, double maxDistance) {
        if (root == null) {
            return null;
        }

        List<Intersectable.Intersection> intersections = new ArrayList<>();
        root.findIntersections(ray, maxDistance, intersections);
        return intersections.isEmpty() ? null : intersections;
    }

    /**
     * Returns the root node of the BVH (for debugging/visualization)
     * @return root BVHNode
     */
    public BVHNode getRoot() {
        return root;
    }

    /**
     * Checks if the BVH is built and ready
     * @return true if BVH is built
     */
    public boolean isBuilt() {
        return root != null;
    }
}