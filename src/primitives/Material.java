package primitives;

public class Material {

    public Double3 Ka = Double3.ZERO;

    public Material setKA(Double3 KaParam){
        this.Ka = KaParam;
        return this;
    }
    public Material setKa(double rgb){
        this.Ka = new Double3(rgb);
        return this;
    }

}
