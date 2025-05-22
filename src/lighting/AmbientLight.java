package lighting;

import primitives.Color;

/**
 * The scene's ambient light
 */
public class AmbientLight extends Light {

    public static AmbientLight NONE = new AmbientLight(Color.BLACK);

    /**
     * constructor for ambient light
     * @param ia ambient color
     */
    public AmbientLight(Color ia) {
        super(ia);
    }

    /**
     * get the ambient light intensity
     * @return the ambient light color
     */
}
