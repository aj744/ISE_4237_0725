package primitives;

/**
 * Represents a 3D vector, which is a directed quantity in space.
 * This class extends {@link Point} but ensures that a vector cannot be the zero vector.
 */
public class Vector extends Point {

    /**
     * Checks if the given {@link Double3} value is zero and throws an exception if it is.
     * @param xyz The coordinates to check
     * @throws IllegalArgumentException if the vector is the zero vector
     */
    private void checkIfZero(Double3 xyz) {
        if (xyz.equals(Double3.ZERO))
            throw new IllegalArgumentException("Vector can not be ZERO");
    }

    /**
     * Constructs a Vector with the given x, y, and z components.
     * @param x X component
     * @param y Y component
     * @param z Z component
     * @throws IllegalArgumentException if the vector is the zero vector
     */
    public Vector(double x, double y, double z) {
        super(x, y, z);
        checkIfZero(new Double3(x, y, z));
    }

    /**
     * Constructs a Vector from a {@link Double3} object.
     * @param xyz A {@link Double3} object representing the vector's components
     * @throws IllegalArgumentException if the vector is the zero vector
     */
    public Vector(Double3 xyz) {
        super(xyz);
        checkIfZero(xyz);
    }

    /**
     * Adds another vector to this vector and returns the resulting vector.
     * @param vector The vector to add
     * @return A new {@link Vector} representing the sum
     * @throws IllegalArgumentException if the vectors cancel each other out
     */
    public Vector add(Vector vector) {
        if (!vector.equals(vector.scale(-1))) // Ensures vectors do not cancel each other out
            return new Vector(this.xyz.add(vector.xyz));
        else
            throw new IllegalArgumentException("Vectors can not be added");
    }

    /**
     * Scales this vector by a given scalar value.
     * @param rhs The scalar value to multiply by
     * @return A new {@link Vector} representing the scaled vector
     * @throws IllegalArgumentException if the scalar is zero
     */
    public Vector scale(double rhs) {
        return new Vector(this.xyz.scale(rhs));
    }

    /**
     * Computes the dot product of this vector with another vector.
     * @param vector The other vector
     * @return The dot product result as a double
     */
    public double dotProduct(Vector vector) {
        return this.xyz.product(vector.xyz).d1() +
                this.xyz.product(vector.xyz).d2() +
                this.xyz.product(vector.xyz).d3();
    }

    /**
     * Computes the cross product of this vector with another vector.
     * @param vector The other vector
     * @return A new {@link Vector} representing the cross product result
     */
    public Vector crossProduct(Vector vector) {
        return new Vector(
                this.xyz.d2() * vector.xyz.d3() - this.xyz.d3() * vector.xyz.d2(),
                this.xyz.d3() * vector.xyz.d1() - this.xyz.d1() * vector.xyz.d3(),
                this.xyz.d1() * vector.xyz.d2() - this.xyz.d2() * vector.xyz.d1()
        );
    }

    /**
     * Computes the squared length (magnitude) of the vector.
     * @return The squared length of the vector
     */
    public double lengthSquared() {
        return this.dotProduct(this);
    }

    /**
     * Computes the length (magnitude) of the vector.
     * @return The length of the vector
     */
    public double length() {
        return Math.sqrt(this.lengthSquared());
    }

    /**
     * Normalizes this vector to have a length of 1.
     * @return A new {@link Vector} representing the normalized vector
     */
    public Vector normalize() {
        return this.scale(1 / this.length());
    }
}