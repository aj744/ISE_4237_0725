package lighting;

import primitives.Color;

/**
 * The scene's ambient light
 */
public class AmbientLight {
    final Color Ia;
    public static AmbientLight NONE = new AmbientLight(Color.BLACK);

    /**
     * constructor for ambient light
     * @param ia ambient color
     */
    public AmbientLight(Color ia) {
        Ia = ia;
    }

    /**
     * get the ambient light intensity
     * @return the ambient light color
     */
    public Color getIntensity() {
        return Ia;
    }
}
