package lighting;

import primitives.Color;

/**
 * Represents Light
 */
abstract class Light {
    /**
     * the light's intensity
     */
    protected final Color intensity;

    protected Light(Color intensity) {
        this.intensity = intensity;
    }

    /**
     * getter for the light's intensity
     * @return the intensity
     */
    public Color getIntensity() {
        return intensity;
    }
}
