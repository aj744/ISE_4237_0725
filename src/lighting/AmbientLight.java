package lighting;

import primitives.Color;

public class AmbientLight {
    final Color Ia;
    public static AmbientLight NONE = new AmbientLight(Color.BLACK);


    public AmbientLight(Color ia) {
        Ia = ia;
    }
    public Color getIntensity() {
        return Ia;
    }
}
