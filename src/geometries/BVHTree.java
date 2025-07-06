package geometries;

import primitives.BoundingBox;
import primitives.Point;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * BVHBuilder is responsible for constructing a Bounding Volume Hierarchy (BVH)
 * from a list of geometries. It recursively splits the geometry list into
 * smaller groups and builds a tree of BVHNodes, each with a bounding box.
 */
public class BVHBuilder {
    /**
     * The maximum number of geometries allowed in a leaf node.
     * If the number of geometries exceeds this, the node will be split further.
     */
    private static final int MAX_GEOMETRIES_IN_LEAF = 2;
    /**
     * Builds the root of the BVH tree from a list of geometries.
     * @param geometries The list of geometries to include in the BVH.
     * @return The root BVHNode of the constructed BVH tree.
     */
    public BVHNode build(List<Geometry> geometries) {
        return buildRecursive(geometries, 0);
    }
    /**
     * Recursively constructs a BVHNode from a list of geometries.
     * @param geometries The geometries to include at this subtree level.
     * @param depth      The current recursion depth (used to choose split axis).
     * @return A BVHNode representing this subtree.
     */
    private BVHNode buildRecursive(List<Geometry> geometries, int depth) {
        if (geometries == null) return null;
        // Base case: if the list is small, create a leaf node
        if (geometries.size() <= MAX_GEOMETRIES_IN_LEAF) {
            BoundingBox box = computeBoundingBox(geometries);
            return new BVHNode(box, geometries);
        }

        // Compute the bounding box that encloses all geometries
        BoundingBox enclosingBox = computeBoundingBox(geometries);

        // Choose axis to split on: 0 = X, 1 = Y, 2 = Z
        int axis = depth % 3;

        // Sort geometries by center point along the chosen axis
        geometries.sort(Comparator.comparingDouble(g -> g.getBoundingBox().getExtent(axis)));

        // Divide geometries into two halves
        int mid = geometries.size() / 2;
        List<Geometry> leftList = geometries.subList(0, mid);
        List<Geometry> rightList = geometries.subList(mid, geometries.size());

        // Recursively build left and right children
        BVHNode left = buildRecursive(new ArrayList<>(leftList), depth + 1);
        BVHNode right = buildRecursive(new ArrayList<>(rightList), depth + 1);

        return new BVHNode(enclosingBox, left, right);
    }

    /**
     * Computes the bounding box that encloses a list of geometries.
     *
     * @param geometries The geometries to enclose.
     * @return A BoundingBox that contains all input geometries.
     */
    private BoundingBox computeBoundingBox(List<Geometry> geometries) {
        BoundingBox box = geometries.get(0).getBoundingBox();
        for (int i = 1; i < geometries.size(); i++) {
            box = box.merge(geometries.get(i).getBoundingBox());
        }
        return box;
    }
}