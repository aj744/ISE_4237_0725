package geometries;

import primitives.Color;
import primitives.Material;
import primitives.Vector;
import primitives.Point;

/**
 * Represents a geometric shape in 3D space.
 * This is an abstract class that defines the basic behavior for all geometries.
 */

public abstract class Geometry extends Intersectable {

    private Material material = new Material();
    protected  Color emission = new Color(java.awt.Color.BLACK);

    public Color getEmission() {
        return emission;
    }

    public Material getMaterial() {
        return material;
    }

    public Geometry setMaterial(Material material) {
        this.material = material;
        return this;
    }

    public Geometry setEmission(Color emission) {
        this.emission = emission;
        return this;
    }


    /**
     * Calculates and returns the normal vector to the geometry at a given point.
     * <p>
     * Since this class is abstract, specific geometries must implement this method.
     * </p>
     *
     * @param point The point on the surface of the geometry.
     * @return The normal vector at the given point.
     */
    public abstract Vector getNormal(Point point);
}
