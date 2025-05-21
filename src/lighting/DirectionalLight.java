package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

public class DirectionalLight implements LightSource extends Light{
    private final Vector direction;

    public DirectionalLight(Color intensity, Vector direction) {
        super(intensity);
        this.direction = direction;
    }

    @Override
    public Color getIntensity(Point p) {
        return null;
    }

    @Override
    public Vector getL(Point p) {
        return null;
    }
}
