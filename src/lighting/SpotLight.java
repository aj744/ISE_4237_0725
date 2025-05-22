package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

public class SpotLight extends PointLight {
    private final Vector direction;

    public SpotLight(Color intensity, Vector position, Vector direction) {
        super(intensity, position);
        this.direction = direction;
    }

    @Override
    public PointLight setKC(double kC) {
        return (PointLight) super.setKC(kC);
    }

    @Override
    public PointLight setKL(double kL) {
        return (PointLight) super.setKC(kL);
    }

    @Override
    public PointLight setKQ(double kQ) {
        return (PointLight) super.setKC(kQ);
    }

    @Override
    public Color getIntensity(Point p) {
        Vector dir = p.subtract(position).normalize();
        return super.getIntensity(p).scale(Math.max(0, direction.dotProduct(dir)));
    }
}
