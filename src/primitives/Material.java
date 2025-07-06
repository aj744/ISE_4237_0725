package primitives;

/**
 * This class represents the material properties of a surface used in rendering.
 * <p>
 * These properties determine how the surface interacts with light, including
 * ambient, diffuse, and specular reflections, as well as shininess for highlights.
 * </p>
 * The values are used in shading calculations for realistic lighting effects.
 */
public class Material {

    /**
     * Ambient reflection coefficient (Ka).
     * Represents the base color of the material when lit by ambient light.
     */
    public Double3 kA = Double3.ONE;

    /**
     * Specular reflection coefficient (Ks).
     * Determines the strength of specular highlights.
     */
    public Double3 kS = Double3.ZERO;

    /**
     * Diffuse reflection coefficient (Kd).
     * Determines how much light is scattered from the surface.
     */
    public Double3 kD = Double3.ZERO;

    /**
     * Shininess coefficient (nShininess).
     * Controls the size and intensity of specular highlights.
     * Higher values create smaller, sharper highlights.
     */
    public int nShininess = 0;

    /**
     * transparency coefficient
     * controls the transparency of a material
     */
    public Double3 kT = Double3.ZERO;

    /**
     * reflection coefficient
     * controls the transparency pf a material
     */
    public Double3 kR = Double3.ZERO;

    // Add these fields to your existing Material class
    public double glossiness = 0.0;  // 0 = perfect mirror, 1 = completely rough
    public double blurriness = 0.0;  // 0 = perfect glass, 1 = completely blurred

    // Add these methods to your Material class
    public Material setGlossiness(double glossiness) {
        this.glossiness = glossiness;
        return this;
    }

    public Material setBlurriness(double blurriness) {
        this.blurriness = blurriness;
        return this;
    }

    public boolean isGlossy() {
        return glossiness > 0;
    }

    public boolean isBlurry() {
        return blurriness > 0;
    }

    public Material setKT(Double3 kT) {
        this.kT = kT;
        return this;
    }

    public Material setKR(Double3 kR) {
        this.kR = kR;
        return this;
    }

    public Material setKT(double kT) {
        this.kT = new Double3(kT);
        return this;
    }

    public Material setKR(double kR) {
        this.kR = new Double3(kR);
        return this;
    }

    /**
     * Sets the specular reflection coefficient.
     *
     * @param Ks a {@link Double3} representing the specular reflection coefficient
     * @return this material (for chaining)
     */
    public Material setKS(Double3 Ks) {
        this.kS = Ks;
        return this;
    }

    /**
     * Sets the diffuse reflection coefficient.
     *
     * @param Kd a {@link Double3} representing the diffuse reflection coefficient
     * @return this material (for chaining)
     */
    public Material setKD(Double3 Kd) {
        this.kD = Kd;
        return this;
    }

    /**
     * Sets the specular reflection coefficient using a scalar.
     *
     * @param Ks a double value for all RGB components of specular reflection
     * @return this material (for chaining)
     */
    public Material setKS(double Ks) {
        this.kS = new Double3(Ks);
        return this;
    }

    /**
     * Sets the diffuse reflection coefficient using a scalar.
     *
     * @param Kd a double value for all RGB components of diffuse reflection
     * @return this material (for chaining)
     */
    public Material setKD(double Kd) {
        this.kD = new Double3(Kd);
        return this;
    }

    /**
     * Sets the shininess factor of the material.
     *
     * @param Nsh shininess coefficient
     * @return this material (for chaining)
     */
    public Material setShininess(int Nsh) {
        this.nShininess = Nsh;
        return this;
    }

    /**
     * Sets the ambient reflection coefficient.
     *
     * @param KaParam a {@link Double3} representing the ambient reflection
     * @return this material (for chaining)
     */
    public Material setKa(Double3 KaParam) {
        this.kA = KaParam;
        return this;
    }

    /**
     * Sets the ambient reflection coefficient using a scalar.
     *
     * @param rgb a double value for all RGB components of ambient reflection
     * @return this material (for chaining)
     */
    public Material setKa(double rgb) {
        this.kA = new Double3(rgb);
        return this;
    }
}
