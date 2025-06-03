package primitives;

public class Material {

    public Double3 kA = Double3.ONE;
    public Double3 kS = Double3.ZERO;
    public Double3 kD = Double3.ZERO;
    public int nShininess = 0;

    public Material setKS(Double3 Ks) {
        this.kS = Ks;
        return this;
    }

    public Material setKD(Double3 Kd) {
        this.kD = Kd;
        return this;
    }

    public Material setKS(double Ks) {
        this.kS = new Double3(Ks);
        return this;
    }

    public Material setKD(double Kd) {
        this.kD = new Double3(Kd);
        return this;
    }

    public Material setShininess(int Nsh) {
        this.nShininess = Nsh;
        return this;
    }

    public Material setKa(Double3 KaParam){
        this.kA = KaParam;
        return this;
    }
    public Material setKa(double rgb){
        this.kA = new Double3(rgb);
        return this;
    }


}
