package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

public class SpotLight extends PointLight {
    private final Vector direction;
    private Double narrowBeam = 1d;

    public SpotLight(Color intensity, Point position, Vector direction) {
        super(position, intensity);
        this.direction = direction;
    }

    /**
     * set the narrow beam of the light
     * @param narrowBeam the narrow beam of the light
     * @return the light source
     */
    public SpotLight setNarrowBeam(double narrowBeam) {
        this.narrowBeam = narrowBeam;
        return this;
    }

    @Override
    public SpotLight setKc(double kC) {
        return (SpotLight) super.setKc(kC);
    }

    @Override
    public SpotLight setKl(double kL) {
        return (SpotLight) super.setKl(kL);
    }

    @Override
    public SpotLight setKq(double kQ) {
        return (SpotLight) super.setKq(kQ);
    }

    @Override
    public Color getIntensity(Point point) {
        Color oldColor = super.getIntensity(point);
        if(narrowBeam != 1d)
            return oldColor.scale(Math.pow(Math.max(0d, direction.dotProduct(getL(point))),narrowBeam));
        return oldColor.scale(Math.max(0d, direction.dotProduct(getL(point))));
    }
}
