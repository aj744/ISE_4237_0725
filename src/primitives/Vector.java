package primitives;

public class Vector extends Point {

    private void checkIfZero(Double3 xyz){
        if(xyz.equals(Double3.ZERO))
            throw new IllegalArgumentException("Vector can not be ZERO");
    }
    public Vector(double x, double y, double z) {
        super(x, y, z);
        checkIfZero(new Double3(x, y, z));

    }
    public Vector(Double3 xyz) {
        super(xyz);
        checkIfZero(xyz);
    }
    public Vector add(Vector vector) {
        if(!vector.equals(vector.scale(-1)))
            return new Vector(this.xyz.add(vector.xyz));
        else
            throw new IllegalArgumentException("Vectors can not be added");
    }
    public Vector scale(double rhs) {
        if(rhs != 0)
            return new Vector(this.xyz.scale(rhs));
        else
            throw new IllegalArgumentException("Scalar cant be ZERO");
    }
    public double dotProduct(Vector vector) {
        return this.xyz.product(vector.xyz).d1() + this.xyz.product(vector.xyz).d2() + this.xyz.product(vector.xyz).d3();
    }
    public Vector crossProduct(Vector vector) {
        return new Vector(
                this.xyz.d2() * vector.xyz.d3() - this.xyz.d3() * vector.xyz.d2(),
                this.xyz.d3() * vector.xyz.d1() - this.xyz.d1() * vector.xyz.d3(),
                this.xyz.d1() * vector.xyz.d2() - this.xyz.d2() * vector.xyz.d1()
        );
    }
    public double lengthSquared() {
        return this.dotProduct(this);
    }
    public double length() {
        return Math.sqrt(this.lengthSquared());
    }
    public Vector normalize() {
        return this.scale(1/this.length());
    }

}
