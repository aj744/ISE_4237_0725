package primitives;

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
    private Point point;

    /**
     * get method for direction
     * @return the direction
     */
    public Vector getVector() { return vector; }

    /**
     * get method for point
     * @return the base point
     */
    public Point getPoint() { return point; }

    /**
     * Constructs a Ray with a given direction vector and starting point.
     * The direction vector is automatically normalized.
     *
     * @param vector The direction vector of the ray
     * @param point The starting point of the ray
     */
    public Ray(Vector vector, Point point) {
        this.vector = vector.normalize(); // Ensure the direction vector is normalized
        this.point = point;
    }
}
