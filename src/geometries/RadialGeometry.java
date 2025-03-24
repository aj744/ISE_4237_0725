package geometries;

public abstract class RadialGeometry extends Geometry {
    protected final double radius ;

    public RadialGeometry(double radius) {
        if (radius <= 0) throw new IllegalArgumentException("Radius must be greater than 0");
        this.radius = radius;
    }

}
