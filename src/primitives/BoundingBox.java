package primitives;

/**
 * Represents an axis-aligned bounding box (AABB) used for spatial acceleration
 */
public class BoundingBox {
    private final Point min;
    private final Point max;

    /**
     * Constructor for BoundingBox
     * @param min minimum point of the box
     * @param max maximum point of the box
     */
    public BoundingBox(Point min, Point max) {
        this.min = min;
        this.max = max;
    }

    /**
     * Gets the minimum point of the bounding box
     * @return minimum point
     */
    public Point getMin() {
        return min;
    }

    /**
     * Gets the maximum point of the bounding box
     * @return maximum point
     */
    public Point getMax() {
        return max;
    }

    /**
     * Creates a bounding box that encompasses both this box and another
     * @param other the other bounding box
     * @return merged bounding box
     */
    public BoundingBox merge(BoundingBox other) {
        double minX = Math.min(this.min.getX(), other.min.getX());
        double minY = Math.min(this.min.getY(), other.min.getY());
        double minZ = Math.min(this.min.getZ(), other.min.getZ());

        double maxX = Math.max(this.max.getX(), other.max.getX());
        double maxY = Math.max(this.max.getY(), other.max.getY());
        double maxZ = Math.max(this.max.getZ(), other.max.getZ());

        return new BoundingBox(
                new Point(minX, minY, minZ),
                new Point(maxX, maxY, maxZ)
        );
    }

    /**
     * Tests if a ray intersects with this bounding box
     * Uses the slab method for efficient AABB-ray intersection
     * @param ray the ray to test
     * @return true if the ray intersects the box
     */
    public boolean intersects(Ray ray) {
        return intersects(ray, Double.MAX_VALUE);
    }

    /**
     * Tests if a ray intersects with this bounding box within a maximum distance
     * @param ray the ray to test
     * @param maxDistance maximum distance to consider
     * @return true if the ray intersects the box within the distance
     */
    public boolean intersects(Ray ray, double maxDistance) {
        Point rayStart = ray.getHead();
        Vector rayDir = ray.getDirection();

        double tMin = 0;
        double tMax = maxDistance;

        // Check intersection with each axis slab
        for (int i = 0; i < 3; i++) {
            double rayDirComponent = rayDir.getComponent(i);
            double rayStartComponent = rayStart.getComponent(i);
            double minComponent = min.getComponent(i);
            double maxComponent = max.getComponent(i);

            if (Math.abs(rayDirComponent) < 1e-10) {
                // Ray is parallel to the slab
                if (rayStartComponent < minComponent || rayStartComponent > maxComponent) {
                    return false;
                }
            } else {
                // Calculate intersection distances
                double t1 = (minComponent - rayStartComponent) / rayDirComponent;
                double t2 = (maxComponent - rayStartComponent) / rayDirComponent;

                if (t1 > t2) {
                    double temp = t1;
                    t1 = t2;
                    t2 = temp;
                }

                tMin = Math.max(tMin, t1);
                tMax = Math.min(tMax, t2);

                if (tMin > tMax) {
                    return false;
                }
            }
        }

        return tMin <= tMax && tMax >= 0;
    }

    /**
     * Gets the surface area of the bounding box
     * @return surface area
     */
    public double getSurfaceArea() {
        double dx = max.getX() - min.getX();
        double dy = max.getY() - min.getY();
        double dz = max.getZ() - min.getZ();
        return 2 * (dx * dy + dy * dz + dz * dx);
    }

    /**
     * Gets the centroid of the bounding box
     * @return centroid point
     */
    public Point getCentroid() {
        return new Point(
                (min.getX() + max.getX()) / 2,
                (min.getY() + max.getY()) / 2,
                (min.getZ() + max.getZ()) / 2
        );
    }

    /**
     * Gets the extent of the bounding box along a specific axis
     * @param axis 0 for X, 1 for Y, 2 for Z
     * @return extent along the axis
     */
    public double getExtent(int axis) {
        return max.getComponent(axis) - min.getComponent(axis);
    }

    /**
     * Returns the center coordinate along a given axis.
     * @param axis 0 for X, 1 for Y, 2 for Z
     * @return the center value along the selected axis
     */
    public double getCenter(int axis) {
        // Must be 0, 1, or 2
        return switch (axis) {
            case 0 -> (min.getX() + max.getX()) / 2.0;// X-axis
            case 1 -> (min.getY() + max.getY()) / 2.0;// Y-axis
            case 2 -> (min.getX() + max.getZ()) / 2.0;// Z-axis
            default -> throw new IllegalArgumentException("Invalid axis: " + axis);// Must be 0, 1, or 2
        };
    }

    /**
     * Gets the longest axis of the bounding box
     * @return 0 for X, 1 for Y, 2 for Z
     */
    public int getLongestAxis() {
        double dx = getExtent(0);
        double dy = getExtent(1);
        double dz = getExtent(2);

        if (dx >= dy && dx >= dz) return 0;
        if (dy >= dz) return 1;
        return 2;
    }
}