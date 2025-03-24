package primitives;

import java.util.Objects;

public class Point {
    protected Double3 xyz;
    public static final Point ZERO = new Point(0, 0, 0);

    public Point(double x, double y, double z) {
        this.xyz = new Double3(x, y, z);
    }

    public Point(Double3 xyz) {
        this.xyz = xyz;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Point point)) return false;
        return Objects.equals(xyz, point.xyz);
    }

    @Override
    public String toString() {
        return "Point{" +
                "xyz=" + xyz +
                '}';
    }

    public Point add(Vector vector) {
        return new Point(this.xyz.add(vector.xyz));
    }

    public Vector subtract(Point point) {
        return new Vector(this.xyz.subtract(point.xyz));
    }

    public double distanceSquared(Point point) {
        if (this.equals(point)) return 0;
        return ((this.xyz.d1() - point.xyz.d1()) * (this.xyz.d1() - point.xyz.d1()) +
                (this.xyz.d2() - point.xyz.d2()) * (this.xyz.d2() - point.xyz.d2())+
                (this.xyz.d3() - point.xyz.d3())*(this.xyz.d3() - point.xyz.d3()));
    }

    public double distance(Point point) {
        return Math.sqrt(distanceSquared(point));
    }
}
