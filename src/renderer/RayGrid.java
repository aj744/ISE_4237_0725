package renderer;

import primitives.Ray;
import renderer.Camera;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RayGrid {
    private static class GridPoint {
        public double x;
        public double y;

        public GridPoint(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    private final List<GridPoint> points = new ArrayList<>();
    private final Random random = new Random();

    public RayGrid(int numOfPoints) {
        // Find the closest perfect square to numOfPoints
        int sideLength = (int) Math.sqrt(numOfPoints);

        // If numOfPoints is not a perfect square, adjust
        if (sideLength * sideLength < numOfPoints) {
            sideLength++;
        }

        // Generate evenly spaced coordinates within (-0.5, 0.5) range
        // This represents offsets within a pixel
        double[] coords;
        if (sideLength == 1) {
            coords = new double[]{0.0}; // Single point at center
        } else {
            coords = new double[sideLength];
            for (int i = 0; i < sideLength; i++) {
                coords[i] = -0.5 + (1.0 * i) / (sideLength - 1);
            }
        }

        int count = 0;
        // Create grid points
        for (int row = 0; row < sideLength; row++) {
            for (int col = 0; col < sideLength; col++) {
                if (count < numOfPoints) {
                    points.add(new GridPoint(coords[col], coords[row]));
                    count++;
                } else {
                    break;
                }
            }
            if (count >= numOfPoints) {
                break;
            }
        }
    }

    /**
     * Creates a beam of rays through sub-pixel positions for anti-aliasing
     * @param camera Camera instance for ray construction
     * @param nX Total pixels in X direction
     * @param nY Total pixels in Y direction
     * @param j Pixel column index
     * @param i Pixel row index
     * @return List of rays through sub-pixel positions
     */
    public List<Ray> createPixelBeam(Camera camera, int nX, int nY, int j, int i) {
        List<Ray> rays = new ArrayList<>();

        for (GridPoint point : points) {
            // Add small random jitter to avoid aliasing patterns
            double jitterX = (random.nextDouble() - 0.5) * 0.1;
            double jitterY = (random.nextDouble() - 0.5) * 0.1;

            // Create ray with sub-pixel offset
            Ray ray = camera.constructRay(nX, nY, j, i,
                    point.x + jitterX,
                    point.y + jitterY);
            rays.add(ray);
        }

        return rays;
    }
}