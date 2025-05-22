package primitives;

public class Material {

    public Double3 Ka = Double3.ZERO;
    public Double3 Ks = Double3.ZERO;
    public Double3 Kd = Double3.ZERO;
    public double Nsh = 0;

    public Material setKs(Double3 Ks) {
        this.Ks = Ks;
        return this;
    }

    public Material setKd(Double3 Kd) {
        this.Kd = Kd;
        return this;
    }

    public Material setKs(double Ks) {
        this.Ks = new Double3(Ks);
        return this;
    }

    public Material setKd(double Kd) {
        this.Kd = new Double3(Kd);
        return this;
    }

    public Material setNsh(double Nsh) {
        this.Nsh = Nsh;
        return this;
    }

    public Material setKa(Double3 KaParam){
        this.Ka = KaParam;
        return this;
    }
    public Material setKa(double rgb){
        this.Ka = new Double3(rgb);
        return this;
    }


}
