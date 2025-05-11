package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import scene.Scene;

import java.util.MissingResourceException;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * The {@code Camera} class represents a virtual camera in 3D space, responsible for generating rays
 * through pixels on a view plane.
 * <p>
 * It supports construction using the Builder pattern.
 */
public class Camera implements Cloneable {
    private Point location;
    private Vector right;
    private Vector up;
    private Vector to;
    private double height;
    private double width;
    private double distance;
    private ImageWriter imageWriter;
    private RayTracerBase rayTracer;
    private int nX = 1;
    private int nY = 1;
    /**
     * Builder class for constructing a {@link Camera} instance in a flexible and readable way.
     */
    private Camera() {}

    public static class Builder {
        final Camera camera = new Camera();

        /**
         * Sets the camera's location in space.
         * @param location The 3D point representing the camera location.
         * @return this Builder instance (for method chaining).
         */
        public Builder setLocation(Point location) {
            camera.location = location;
            return this;
        }

        /**
         * Sets the viewing direction using "to" and "up" vectors.
         * @param to Vector pointing in the viewing direction.
         * @param up Vector pointing upward.
         * @return this Builder instance.
         * @throws IllegalArgumentException if the vectors are not orthogonal.
         */
        public Builder setDirection(Vector to, Vector up){
            if(checkOrthogonal(up, to)){
                throw new IllegalArgumentException("Up and To must be orthogonal");
            }
            camera.up = up.normalize();
            camera.to = to.normalize();
            camera.right = camera.to.crossProduct(camera.up).normalize();
            return this;
        }

        /**
         * Sets direction using a target point and an "up" vector.
         * Calculates the "to" vector as the direction from camera location to target.
         * @param target The point to look at.
         * @param up The "up" vector for orientation.
         * @return this Builder instance.
         */
        public Builder setDirection(Point target, Vector up){
            Vector to = target.subtract(camera.location);
            // Not checking orthogonality here to allow auto-adjustment
            camera.to = to.normalize();
            camera.right = to.crossProduct(up).normalize();
            camera.up = camera.right.crossProduct(camera.to).normalize();

            return this;
        }

        /**
         * Sets direction using only a target point.
         * Assumes global Y-axis (AXIS_Y) as "up".
         * @param target The point to look at.
         * @return this Builder instance.
         */
        public Builder setDirection(Point target){
            camera.to = target.subtract(camera.location).normalize();
            camera.up = Vector.AXIS_Y;
            camera.right = camera.to.crossProduct(camera.up).normalize();
            camera.up = camera.right.crossProduct(camera.to).normalize();
            return this;
        }

        /**
         * Sets the size of the view plane.
         * @param height The height of the view plane.
         * @param width The width of the view plane.
         * @return this Builder instance.
         * @throws IllegalArgumentException if height or width are not positive.
         */
        public Builder setVpSize(double height, double width) {
            if (checkGreaterThanZero(height))
                throw new IllegalArgumentException("height must be grater than zero");
            if (checkGreaterThanZero(width))
                throw new IllegalArgumentException("width must be grater than zero");
            camera.height = height;
            camera.width = width;
            return this;
        }

        /**
         * Sets the distance from the camera to the view plane.
         * @param distance Distance to the view plane.
         * @return this Builder instance.
         * @throws IllegalArgumentException if distance is not positive.
         */
        public Builder setVpDistance(double distance) {
            if (checkGreaterThanZero(distance))
                throw new IllegalArgumentException("distance must be grater than zero");
            camera.distance = distance;

            return this;
        }

        /**
         * Placeholder for resolution setting. Currently unused.
         * @param nX Number of columns (pixels in width).
         * @param nY Number of rows (pixels in height).
         * @return this Builder instance.
         */
        public Builder setResolution(int nX, int nY) {
            camera.nX = nX;
            camera.nY = nY;
            return this;
        }

        /**
         * Builds and returns the configured {@link Camera} instance.
         * @return A cloned Camera object.
         * @throws IllegalArgumentException if any required field is invalid or unset.
         */
        public Camera build() {
            final String className = "Camera";
            final String description = "missing render data";

            if(camera.location == null)
                throw new MissingResourceException(description, className, "location");
            if(camera.up == null)
                throw new MissingResourceException(description, className, "up");
            if(camera.to == null)
                throw new MissingResourceException(description, className, "to");
            if(camera.width == 0d)
                throw new MissingResourceException(description, className, "width");
            if(camera.height == 0d)
                throw new MissingResourceException(description, className, "height");
            if(camera.distance == 0d)
                throw new MissingResourceException(description, className, "distance");

            camera.right = camera.to.crossProduct(camera.up).normalize();

            if (!isZero(camera.to.dotProduct(camera.right)) ||
                    !isZero(camera.to.dotProduct(camera.up)) ||
                    !isZero(camera.right.dotProduct(camera.up)))
                throw new IllegalArgumentException("to, up and right must be orthogonal");

            if ((alignZero(camera.to.length() - 1)) != 0
                    || (alignZero(camera.up.length() - 1)) != 0
                    || (alignZero(camera.right.length() - 1)) != 0)
                throw new IllegalArgumentException("to, up and right must be normalized");

            if (camera.width <= 0 || camera.height <= 0)
                throw new IllegalArgumentException("width and height must be positive");

            if (camera.distance <= 0)
                throw new IllegalArgumentException("distance from camera to view must be positive");

            if(camera.nX <= 0 || camera.nY <= 0)
                throw new IllegalArgumentException("nX and nY must be positive");
            else
                camera.imageWriter = new ImageWriter(camera.nX, camera.nY);

            if (camera.rayTracer == null)
                camera.rayTracer = new SimpleRayTracer(null);


            try {
                return (Camera) camera.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * Checks if two vectors are not orthogonal (dot product not zero).
         */
        public boolean checkOrthogonal(Vector a, Vector b) {
            return a.dotProduct(b) != 0;
        }

        /**
         * Checks if a value is less than or equal to zero after alignment.
         */
        public boolean checkGreaterThanZero(double a) {
            return alignZero(a) <= 0;
        }

        public Builder setRayTracer(Scene scene , RayTracerType rayTracerType){
            if(rayTracerType == RayTracerType.SIMPLE){
                camera.rayTracer = new SimpleRayTracer(scene);
            }
            else {
                camera.rayTracer = null;
            }
            return this;
        }
    }

    /**
     * Returns a new {@link Builder} instance to configure a Camera.
     */
    public static Builder getBuilder() {
        return new Builder();
    }

    /**
     * Constructs a ray from the camera through the center of a specified pixel (i,j).
     * @param nX Total number of columns (pixels) in view plane.
     * @param nY Total number of rows (pixels) in view plane.
     * @param j Column index of the pixel.
     * @param i Row index of the pixel.
     * @return Ray from camera through pixel center.
     */
    public Ray constructRay(int nX, int nY, int j, int i) {
        Point Pc = location.add(to.scale(distance)); // center of view plane
        double xj = (j - (nX - 1) / 2.0) * (width / nX); // pixel horizontal offset
        double yi = -(i - (nY - 1) / 2.0) * (height / nY); // pixel vertical offset (negative due to coordinate system)

        Point Pij = Pc;
        if (!isZero(xj)) Pij = Pij.add(right.scale(xj));
        if (!isZero(yi)) Pij = Pij.add(up.scale(yi));
        return new Ray(location, Pij.subtract(location));
    }

    public Camera renderImage() {
        for (int i=0; i<this.nX; i++) {
            for (int j=0; j<this.nY; j++) {
                castRay(j, i);
            }
        }
        return this;
    }

    public Camera printGrid(int interval , Color color){
        for (int i = 0; i < imageWriter.nY(); i++) {
            for (int j = 0; j < imageWriter.nX(); j++) {
                if (i % interval == 0 || j % interval == 0) {
                    imageWriter.writePixel(j, i, color);
                }
            }
        }
        return this;
    }

    public void writeToImage(String fileName) {
         imageWriter.writeToImage(fileName);
    }

    public void castRay(int j, int i) {
        Ray ray = constructRay(nX, nY, j, i);
        imageWriter.writePixel(j, i, rayTracer.traceRay(ray));
    }
}
