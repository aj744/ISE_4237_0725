package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import scene.Scene;

import java.util.LinkedList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.stream.IntStream;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * The {@code Camera} class represents a virtual camera in 3D space, responsible for generating rays
 * through pixels on a view plane. It supports construction using the Builder pattern and
 * handles image rendering with ray tracing.
 */
public class Camera implements Cloneable {
    /**
     * The camera's location in 3D space.
     */
    private Point location;

    /**
     * The right direction vector of the camera (X axis).
     */
    private Vector right;

    /**
     * The up direction vector of the camera (Y axis).
     */
    private Vector up;

    /**
     * The forward (to) direction vector of the camera (Z axis).
     */
    private Vector to;

    /**
     * The height of the view plane.
     */
    private double height;

    /**
     * The width of the view plane.
     */
    private double width;

    /**
     * The distance from the camera to the view plane.
     */
    private double distance;

    /**
     * Utility for writing image pixels to a file.
     */
    private ImageWriter imageWriter;

    /**
     * The ray tracer used to determine pixel color.
     */
    private RayTracerBase rayTracer;

    /**
     * Number of pixels in width.
     */
    private int nX = 1;

    /**
     * Number of pixels in height.
     */
    private int nY = 1;

    private final int numOfRays = 289;
    private final RayGrid rayGrid = new RayGrid(numOfRays);

    private int threadsCount = 0; // -2 auto, -1 range/stream, 0 no threads, 1+ number of threadsprivate
    static final int SPARE_THREADS = 2; // Spare threads if trying to use all the coresprivate
    double printInterval = 0; // printing progress percentage interval (0 – no printing)
    private PixelManager pixelManager; // pixel manager object

    /**
     * Private constructor to enforce use of Builder.
     */
    private Camera() {}

    /**
     * Builder class for constructing a {@link Camera} instance in a flexible and readable way.
     */
    public static class Builder {
        final Camera camera = new Camera();

        /**
         * Point that the camera is currently targeting/looking at.
         * Used to maintain focus when moving the camera.
         */
        private Point targetPoint;

        /**
         * Moves the camera by the specified displacement vector while keeping it focused
         * on the same target point it was previously looking at.
         *
         * @param displacement Vector specifying how much to move the camera location
         * @return this Builder instance (for method chaining)
         * @throws IllegalStateException if camera location or target point is not set
         */
        public Builder moveCamera(Vector displacement) {
            if (camera.location == null) {
                throw new IllegalStateException("Camera location must be set before moving");
            }
            if (targetPoint == null) {
                throw new IllegalStateException("Target point must be set before moving camera");
            }

            // Move the camera location
            camera.location = camera.location.add(displacement);

            // Recalculate direction vectors to keep looking at the same target point
            camera.to = targetPoint.subtract(camera.location).normalize();

            // Preserve the current up vector if it exists, otherwise use global Y-axis
            Vector currentUp = (camera.up != null) ? camera.up : Vector.AXIS_Y;

            // Recalculate right and up vectors to maintain proper orthogonal orientation
            camera.right = camera.to.crossProduct(currentUp).normalize();
            camera.up = camera.right.crossProduct(camera.to).normalize();

            return this;
        }

        /**
         * Sets the target point that the camera should look at.
         * This point will be preserved when moving the camera.
         *
         * @param target The point the camera should focus on
         * @return this Builder instance (for method chaining)
         */
        public Builder setTargetPoint(Point target) {
            this.targetPoint = target;
            return this;
        }

        /**
         * Gets the current target point.
         *
         * @return the target point, or null if not set
         */
        public Point getTargetPoint() {
            return targetPoint;
        }

        /**
         * rotates the camera
         * @param degrees the angle of rotation
         * @return the object for method stacking
         */
        public Builder rotateCamera(int degrees) {
            if (camera.to == null) {
                throw new IllegalStateException("Camera 'to' vector must be set before rotating");
            }
            if (camera.up == null) {
                throw new IllegalStateException("Camera 'up' vector must be set before rotating");
            }
            if (camera.right == null) {
                throw new IllegalStateException("Camera 'right' vector must be set before rotating");
            }

            // Convert degrees to radians
            double radians = Math.toRadians(degrees);
            double cosTheta = Math.cos(radians);
            double sinTheta = Math.sin(radians);

            // Store current up and right vectors
            Vector currentUp = camera.up;
            Vector currentRight = camera.right;

            // Simple 2D rotation in the plane perpendicular to "to" vector
            // Since up and right are orthogonal and both perpendicular to "to",
            // we can treat this as a 2D rotation in the up-right plane

            // new_up = up*cos(θ) - right*sin(θ)
            // new_right = up*sin(θ) + right*cos(θ)

            Vector newUp = currentUp.scale(cosTheta).add(currentRight.scale(-sinTheta));
            Vector newRight = currentUp.scale(sinTheta).add(currentRight.scale(cosTheta));

            // Normalize to ensure unit vectors (should already be unit, but ensure precision)
            camera.up = newUp.normalize();
            camera.right = newRight.normalize();

            return this;
        }

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
            camera.to = to.normalize();
            camera.right = to.crossProduct(up).normalize();
            camera.up = camera.right.crossProduct(camera.to).normalize();

            targetPoint = target;
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

            targetPoint = target;
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
                throw new IllegalArgumentException("height must be greater than zero");
            if (checkGreaterThanZero(width))
                throw new IllegalArgumentException("width must be greater than zero");
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
                throw new IllegalArgumentException("distance must be greater than zero");
            camera.distance = distance;
            return this;
        }

        /**
         * Sets the resolution of the image to render.
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
         * Sets the ray tracer to be used for rendering.
         * @param scene The scene to render.
         * @param rayTracerType The type of ray tracer to use.
         * @return this Builder instance.
         */
        public Builder setRayTracer(Scene scene , RayTracerType rayTracerType){
            if(rayTracerType == RayTracerType.SIMPLE){
                camera.rayTracer = new SimpleRayTracer(scene);
            } else {
                camera.rayTracer = null;
            }
            return this;
        }

        public Builder setMultithreading(int threads) {
            if (threads < -2) throw new IllegalArgumentException("Multithreading must be -2 or higher");
            if (threads >= -1) camera.threadsCount = threads;
            else { // == -2
                int cores = Runtime.getRuntime().availableProcessors() - SPARE_THREADS;
                camera.threadsCount = cores <= 2 ? 1 : cores;
            }
            return this;
        }
        public Builder setDebugPrint(double interval) {
            if (interval < 0) throw new IllegalArgumentException("Interval value must be non-negative");
            camera.printInterval = interval;
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
         * @param a first vector
         * @param b second vector
         * @return true if not orthogonal
         */
        public boolean checkOrthogonal(Vector a, Vector b) {
            return a.dotProduct(b) != 0;
        }

        /**
         * Checks if a value is less than or equal to zero after alignment.
         * @param a the value to check
         * @return true if a <= 0
         */
        public boolean checkGreaterThanZero(double a) {
            return alignZero(a) <= 0;
        }
    }

    /**
     * Returns a new {@link Builder} instance to configure a Camera.
     * @return a new Camera.Builder
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
        Point Pc = location.add(to.scale(distance));
        double xj = (j - (nX - 1) / 2.0) * (width / nX);
        double yi = -(i - (nY - 1) / 2.0) * (height / nY);
        Point Pij = Pc;
        if (!isZero(xj)) Pij = Pij.add(right.scale(xj));
        if (!isZero(yi)) Pij = Pij.add(up.scale(yi));
        return new Ray(location, Pij.subtract(location));
    }

    /**
     * Renders the image by casting rays through each pixel.
     * @return this Camera instance
     */
    public Camera renderImageNoThreads() {
        for (int i=0; i<this.nX; i++) {
            for (int j=0; j<this.nY; j++) {
                castRay(j, i);
            }
        }
        return this;
    }

    /** ...
     */
    private void castRay() {
// your existing code...
        pixelManager.pixelDone();
    }
    /** ...
     */
    public Camera renderImage() {
        pixelManager = new PixelManager(nY, nX, printInterval);
        return switch (threadsCount) {
            case 0 -> renderImageNoThreads();
            case -1 -> renderImageStream();
            default -> renderImageRawThreads();
        };
    }

    /**
     * Render image using multi-threading by creating and running raw threads* @return the camera object itself
     */
    private Camera renderImageRawThreads() {
        var threads = new LinkedList<Thread>();
        while (threadsCount-- > 0)
            threads.add(new Thread(() -> {
                PixelManager.Pixel pixel;
                while ((pixel = pixelManager.nextPixel()) != null)
                    castRay(pixel.col(), pixel.row());
            }));
        for (var thread : threads) thread.start();
        try {
            for (var thread : threads) thread.join();
        } catch (InterruptedException ignore) {}
        return this;
    }
    /**
     * Render image using multi-threading by creating and running raw threads* @return the camera object itself
     */
    public Camera renderImageStream() {
        IntStream.range(0, nY).parallel() //
                .forEach(i -> IntStream.range(0, nX).parallel() //
                        .forEach(j -> castRay(j, i)));
        return this;
    }

    /**
     * Draws a grid on the image using the given interval and color.
     * @param interval spacing between grid lines
     * @param color color of the grid lines
     * @return this Camera instance
     */
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

    /**
     * Writes the rendered image to a file.
     * @param fileName the name of the output file
     */
    public void writeToImage(String fileName) {
        imageWriter.writeToImage(fileName);
    }

    /**
     * Casts a single ray through a specific pixel and writes its color.
     * @param j column index
     * @param i row index
     */
    public void castRay(int j, int i) {
        List<Ray> rays = rayGrid.createPixelBeam(this, nX, nY, j, i);

        Color totalColor = Color.BLACK;

        // Trace each ray and accumulate colors
        for (Ray ray : rays) {
            Color rayColor = rayTracer.traceRay(ray);
            totalColor = totalColor.add(rayColor);
        }

        // Average the colors
        Color finalColor = totalColor.reduce(rays.size());
        imageWriter.writePixel(j, i, finalColor);
    }

    // Modified Camera class methods (add these to your Camera class)
    /**
     * Constructs a ray from the camera through a specific sub-pixel position
     * @param nX Total number of columns (pixels) in view plane
     * @param nY Total number of rows (pixels) in view plane
     * @param j Column index of the pixel
     * @param i Row index of the pixel
     * @param offsetX Sub-pixel offset in X direction (-0.5 to 0.5)
     * @param offsetY Sub-pixel offset in Y direction (-0.5 to 0.5)
     * @return Ray from camera through the specified sub-pixel position
     */
    public Ray constructRay(int nX, int nY, int j, int i, double offsetX, double offsetY) {
        // Center of the view plane
        Point Pc = location.add(to.scale(distance));

        // Calculate pixel dimensions
        double pixelWidth = width / nX;
        double pixelHeight = height / nY;

        // Calculate base pixel position
        double xj = (j - (nX - 1) / 2.0) * pixelWidth;
        double yi = -(i - (nY - 1) / 2.0) * pixelHeight;

        // Add sub-pixel offset
        xj += offsetX * pixelWidth;
        yi += offsetY * pixelHeight;

        // Calculate the point on the view plane
        Point Pij = Pc;
        if (!isZero(xj)) Pij = Pij.add(right.scale(xj));
        if (!isZero(yi)) Pij = Pij.add(up.scale(yi));

        return new Ray(location, Pij.subtract(location));
    }
}