package unittests.primitives;

import org.junit.jupiter.api.Test;
import primitives.Double3;
import primitives.Vector;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class VectorTests {

    @Test
    public void testConstructorPoint() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: regular vector of Point - should not throw a note
        assertDoesNotThrow(
                ()-> new Vector(1,1,1),
                "Constructor Point should not throw IllegalArgumentException"
        );
        // =============== Boundary Values Tests ==================
        // TC02: vector cant be zero
        assertThrows(
                IllegalArgumentException.class,
                () -> new Vector(0,0,0),
                "Constructor Point should throw IllegalArgumentException"
        );

    }
    @Test
    // TC01:regular vector of Double3 - should not throw a note
    public void testConstructorDouble3(){
        // TC01: regular vector of Double3 - should not throw a note
        assertDoesNotThrow(
                ()->new Vector(new Double3(1,1,1)),
                "Constructor vector Double3 should not throw IllegalArgumentException"
        );
        // =============== Boundary Values Tests ==================
        //TC02: BVA if the vector is 0 - throw a note
        assertThrows(
                IllegalArgumentException.class,
                ()->new Vector(new Double3(0,0,0)),
                "Constructor vector Double3 should throw IllegalArgumentException"
        );
    }
    @Test
    public void testAdd(){
        // ============ Equivalence Partitions Tests ==============
        // TC01: two vectors that in different direction or different sizes.
        Vector vector1 = new Vector(1,1,1);
        Vector vector2 = new Vector(2,2,2);
        assertEquals(
                new Vector(3,3,3),
                vector1.add(vector2),
                "the result vector should be (3,3,3)"
        );
        // =============== Boundary Values Tests ==================
        // TC02: if the vectors are in the same direction and same sizes
        assertThrows(
                IllegalArgumentException.class,
                ()-> vector1.add(vector1.scale(-1)),
                "the vector should be different direction or different sizes!"
        );

    }
    @Test
    public void testScale(){
        // ============ Equivalence Partitions Tests ==============
        // TC01: if the rhs is not 0
        Vector vector1 = new Vector(1,1,1);
        assertEquals(
                new Vector(2,2,2),
                vector1.scale(2),
                "the scaled vector should be (2,2,2)"
        );
        // =============== Boundary Values Tests ==================
        // TC02: if the vectors are in the same direction and same sizes
        assertThrows(
                IllegalArgumentException.class,
                ()-> vector1.scale(0),
                "the scaled vector is 0 and should throw IllegalArgumentException"
        );

    }

    @Test
    public void TestDotProduct() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: test the dot product between 2 vectors
        assertEquals(
                1,
                new Vector(1, 0, 0).dotProduct(new Vector(1, 0,0)),
                "the result should be 1"
        );

        // =============== Boundary Values Tests ==================
        // TC11: arithmetic vectors
        assertThrows(
                IllegalArgumentException.class,
                () -> new Vector(1, 0,0).dotProduct(new Vector(0, 1, 0)),
                "an exception should be thrown"
        );
    }

    @Test
    void testCrossProduct() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: cross product between 2 vectors
        Vector v1 = new Vector(1, 0, 0);
        Vector v2 = new Vector(0, 1, 0);
        Vector result = v1.crossProduct(v2);

        assertEquals(new Vector(0, 0, 1), result, "Cross product result is incorrect");

        // =============== Boundary Values Tests ==================
        // TC11: parallel vectors
        assertThrows(IllegalArgumentException.class, () -> v1.crossProduct(new Vector(2, 0, 0)),
                "Parallel vectors should throw an exception");
    }

    /**
     * Test length squared computation.
     */
    @Test
    void testLengthSquared() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: test the length squared of a vector
        Vector v = new Vector(1, 2, 2);
        assertEquals(9, v.lengthSquared(), "Length squared calculation is incorrect");
    }

    /**
     * Test length computation.
     */
    @Test
    void testLength() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: test a vector
        Vector v = new Vector(0, 3, 4);
        assertEquals(5, v.length(), "Length calculation is incorrect");
    }

}
