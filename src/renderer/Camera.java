package renderer;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import primitives.Util;

import java.util.MissingResourceException;

public class Camera implements Cloneable{
    public static class Builder {
        final Camera camera = new Camera();

        public Builder setLocation(Point location) {
            camera.location = location;
            return this;
        }

        public Builder setDirection(Vector up, Vector to){
            if (up.dotProduct(to) != 0) throw new IllegalArgumentException("Vectors up and to are not orthogonal");

            camera.up = up.normalize();
            camera.to = to.normalize();
            camera.right = camera.up.crossProduct(camera.to).normalize();

            return this;
        }

        public Builder setDirection(Point target, Vector up){
            Vector to = target.subtract(camera.location);
            if (up.dotProduct(to) != 0) throw new IllegalArgumentException("Vectors up and to are not orthogonal");

            camera.to = to.normalize();
            camera.right = to.crossProduct(up).normalize();

            return this;
        }

        public Builder setDirection(Point target){
            camera.to = target.subtract(camera.location).normalize();
            camera.up = new Vector(0, 1, 0);
            camera.right = camera.to.crossProduct(camera.up).normalize();

            return this;
        }

        public Builder setVpSize(double height, double width) {
            if (height  <= 0) throw new IllegalArgumentException("height must be grater than zero");
            if (width <= 0) throw new IllegalArgumentException("width must be grater than zero");
            camera.height = height;
            camera.width = width;

            return this;
        }

        public Builder setVpDistance(double distance) {
            if (distance <= 0) throw new IllegalArgumentException("distance must be grater than zero");
            camera.distance = distance;

            return this;
        }

        public Builder setResolution(double nX, double nY) {
            return this;
        }

        public Camera build() {
            if (camera.location.equals(Point.ZERO)) throw new MissingResourceException("the camera location is not set");


            return (Camera) this.camera.clone();
        }
    }

    private Point location;
    private Vector right;
    private Vector up;
    private Vector to;
    private double height;
    private double width;
    private double distance;

    private Camera() {
        location = new Point(0, 0, 0);
        right = new Vector(0, 0, 0);
        up = new Vector(0, 0, 0);
        to = new Vector(0, 0, 0);
    }

    public static void getBuilder() {}

    public Ray constructRay(int nX, int nY, int j, int i) {
        return null;
    }
}
