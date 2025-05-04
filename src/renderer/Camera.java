package renderer;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import primitives.Util;

import java.util.MissingResourceException;

import static primitives.Util.alignZero;

public class Camera implements Cloneable{
    public static class Builder {
        final Camera camera = new Camera();

        public Builder setLocation(Point location) {
            camera.location = location;
            return this;
        }

        public Builder setDirection(Vector up, Vector to){
            if(checkOrthogonal(up,to)){
                throw new IllegalArgumentException("Up and To must be orthogonal");
            }
            camera.up = up.normalize();
            camera.to = to.normalize();
            camera.right = camera.up.crossProduct(camera.to).normalize();
            return this;
        }

        public Builder setDirection(Point target, Vector up){
            Vector to = target.subtract(camera.location);
            if (checkOrthogonal(up,to)){
                throw new IllegalArgumentException("Up and To must be orthogonal");
            }
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
            if (checkGreaterThanZero(height))
                throw new IllegalArgumentException("height must be grater than zero");
            if (checkGreaterThanZero(width))
                throw new IllegalArgumentException("width must be grater than zero");
            camera.height = height;
            camera.width = width;
            return this;
        }

        public Builder setVpDistance(double distance) {
            if (checkGreaterThanZero(distance))
                throw new IllegalArgumentException("distance must be grater than zero");
            camera.distance = distance;

            return this;
        }

        public Builder setResolution(double nX, double nY) {
            return this;
        }

        public Camera build() {
            if (checkGreaterThanZero(camera.distance))
                throw new IllegalArgumentException("distance must be grater than zero");
            if (checkGreaterThanZero(camera.height))
                throw new IllegalArgumentException("height must be grater than zero");
            if (checkGreaterThanZero(camera.width))
                throw new IllegalArgumentException("width must be grater than zero");
            if(checkOrthogonal(camera.up,camera.to))
                throw new IllegalArgumentException("Vectors up and to are not orthogonal");

            camera.right = camera.up.crossProduct(camera.to).normalize();

            return (Camera) this.camera;
        }

        public boolean checkOrthogonal(Vector a, Vector b) {return a.dotProduct(b) != 0 ;}
        public boolean checkGreaterThanZero(double a) {return alignZero(a) <= 0;}
    }

    private Point location;
    private Vector right;
    private Vector up;
    private Vector to;
    private double height;
    private double width;
    private double distance;

    public static Builder getBuilder() {return new Builder();}

    public Ray constructRay(int nX, int nY, int j, int i) {
        Ray ray = new Ray(location, to);
        Point Pc = ray.getPoint(distance);
        Point Pij = Pc.subtract(Point.ZERO)
                .add(right.scale((j - (nX - 1) / 2.0) * (width/nX)))
                .add(up.scale((i - (nY - 1) / 2.0) * (height / nY)).scale(-1));
        return new Ray(Pij, Pij.subtract(location));
    }
}
