package primitives;

public class Ray {
    private Vector vector;
    private Point point;

    public Ray(Vector vector, Point point) {
        this.vector = vector.normalize();
        this.point = point;
    }
}
