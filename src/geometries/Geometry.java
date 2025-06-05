package geometries;

import primitives.Color;
import primitives.Material;
import primitives.Vector;
import primitives.Point;

import static java.awt.Color.BLACK;

/**
 * Represents a geometric shape in 3D space.
 * <p>
 * This is an abstract class that defines the base behavior for all concrete geometry classes.
 * Each geometry has an emission color and a material that define its appearance and interaction
 * with light in a ray-tracing context.
 * </p>
 * <p>
 * Extends {@link Intersectable}, meaning each geometry can be intersected by rays.
 * </p>
 */
public abstract class Geometry extends Intersectable {

    /**
     * The material properties of the geometry (e.g. shininess, transparency, etc.).
     */
    private Material material = new Material();

    /**
     * The emission color of the geometry — the intrinsic color it emits.
     * Default is black (no emission).
     */
    protected Color emission = new Color(BLACK);

    /**
     * Gets the emission color of the geometry.
     *
     * @return the emission color
     */
    public Color getEmission() {
        return emission;
    }

    /**
     * Gets the material properties of the geometry.
     *
     * @return the material of the geometry
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * Sets the material of the geometry.
     *
     * @param material the material to set
     * @return this geometry object for method chaining
     */
    public Geometry setMaterial(Material material) {
        this.material = material;
        return this;
    }

    /**
     * Sets the emission color of the geometry.
     *
     * @param emission the emission color to set
     * @return this geometry object for method chaining
     */
    public Geometry setEmission(Color emission) {
        this.emission = emission;
        return this;
    }

    /**
     * Calculates and returns the normal vector to the geometry at a given point.
     * <p>
     * This method must be implemented by any concrete subclass to define the geometry's surface behavior.
     * </p>
     *
     * @param point the point on the surface of the geometry
     * @return the unit normal vector at the given point
     */
    public abstract Vector getNormal(Point point);
}
