package geometries;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

public class Cylinder extends Tube{
    private final double height;

    public Cylinder(Ray axis, double radius, double height) {
        super(radius, axis);
        if( height < 0) throw new IllegalArgumentException("Hight must be positive");
        this.height = height;
    }

    @Override
    public Vector getNormal(Point p){
        return null ;
    }

}
