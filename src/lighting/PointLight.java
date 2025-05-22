package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

public class PointLight implements LightSource extends Light{
    protected final Vector position;
    private double kC = 1;
    private double kL = 0;
    private double kQ = 0;

    public PointLight(Color intensity, Vector position) {
        super(intensity);
        this.position = position;
    }

    public PointLight setKC(double kC) {
        this.kC = kC;
        return this;
    }

    public PointLight setKL(double kL) {
        this.kL= kL;
        return this;
    }

    public PointLight setKQ(double kQ) {
        this.kQ = kQ;
        return this;
    }

    @Override
    public Color getIntensity(Point p) {
        double d = getL(p).length();
        return super.getIntensity().scale(1.0f / (kC + kL * d + kQ * d * d));
    }

    @Override
    public Vector getL(Point p) {
        return p.subtract(position);
    }
}
