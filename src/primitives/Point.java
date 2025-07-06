package primitives;

import java.util.Objects;

/**
 * Represents a point in a 3D space using three coordinates.
 */
public class Point {
    /**
     * The coordinates of the point stored as a Double3 object.
     */
    protected Double3 xyz;

    /**
     * A constant representing the zero point (0,0,0) in 3D space.
     */
    public static final Point ZERO = new Point(0, 0, 0);

    /**
     * Constructs a Point from three double values.
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     */
    public Point(double x, double y, double z) {
        this.xyz = new Double3(x, y, z);
    }

    /**
     * Constructs a Point from a Double3 object.
     * @param xyz A Double3 object representing the coordinates
     */
    public Point(Double3 xyz) {
        this.xyz = xyz;
    }

    /**
     * Checks if this point is equal to another object.
     * @param o The object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Point point)) return false; // Ensures type safety
        return Objects.equals(xyz, point.xyz);
    }

    /**
     * Returns a string representation of the point.
     * @return A string describing the point
     */
    @Override
    public String toString() {
        return "Point{" + "xyz=" + xyz + '}';
    }

    /**
     * Adds a vector to this point, resulting in a new point.
     * @param vector The vector to add
     * @return A new Point that is the result of the addition
     */
    public Point add(Vector vector) {
        return new Point(this.xyz.add(vector.xyz)); // Uses Double3 addition
    }

    /**
     * Subtracts another point from this point, resulting in a vector.
     * @param point The point to subtract
     * @return A Vector representing the difference between the points
     */
    public Vector subtract(Point point) {
        return new Vector(this.xyz.subtract(point.xyz)); // Converts point difference into a vector
    }

    /**
     * Computes the squared distance between this point and another point.
     * @param point The other point
     * @return The squared distance between the two points
     */
    public double distanceSquared(Point point) {
        if (this.equals(point)) return 0; // If points are equal, distance is zero
        return ((this.xyz.d1() - point.xyz.d1()) * (this.xyz.d1() - point.xyz.d1()) +
                (this.xyz.d2() - point.xyz.d2()) * (this.xyz.d2() - point.xyz.d2())+
                (this.xyz.d3() - point.xyz.d3())*(this.xyz.d3() - point.xyz.d3()));
    }

    /**
     * Computes the Euclidean distance between this point and another point.
     * @param point The other point
     * @return The Euclidean distance between the two points
     */
    public double distance(Point point) {
        return Math.sqrt(distanceSquared(point)); // Distance is the square root of the squared distance
    }

    public double getX() {
        return this.xyz.d1();
    }

    public double getY() {
        return this.xyz.d2();
    }

    public double getZ() {
        return this.xyz.d3();
    }

    public double getComponent(int i) {
        return switch (i) {
            case 0 -> getX();
            case 1 -> getY();
            case 2 -> getZ();
            default -> throw new IllegalArgumentException("parameter must be 0/1/2");
        };
    }
}
