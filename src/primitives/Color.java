package primitives;

/**
 * Wrapper class for {@link java.awt.Color}.
 * <p>
 * This class allows working with RGB color components without the traditional upper bound of 255.
 * It is used primarily for light color computations in rendering, where over-bright values are valid.
 * Provides operations for scaling, adding, and converting colors.
 * </p>
 *
 * @author Dan Zilberstein
 */
public class Color {
    /**
     * The internal RGB representation using {@link Double3}.
     * Each component (red, green, blue) can have any non-negative double value.
     */
    private final Double3 rgb;

    /** Constant representing the black color (0, 0, 0). */
    public static final Color BLACK = new Color();

    /**
     * Private default constructor that initializes the color to black.
     */
    private Color() {
        rgb = Double3.ZERO;
    }

    /**
     * Constructs a color from individual red, green, and blue components.
     * Values must be non-negative.
     *
     * @param r red component
     * @param g green component
     * @param b blue component
     * @throws IllegalArgumentException if any component is negative
     */
    public Color(double r, double g, double b) {
        if (r < 0 || g < 0 || b < 0)
            throw new IllegalArgumentException("Negative color component is illegal");
        rgb = new Double3(r, g, b);
    }

    /**
     * Constructs a color from a {@link Double3} representing RGB components.
     *
     * @param rgb the RGB components
     * @throws IllegalArgumentException if any component is negative
     */
    public Color(Double3 rgb) {
        if (rgb.d1() < 0 || rgb.d2() < 0 || rgb.d3() < 0)
            throw new IllegalArgumentException("Negative color component is illegal");
        this.rgb = rgb;
    }

    /**
     * Constructs a color from a {@link java.awt.Color} object.
     *
     * @param other a java.awt.Color object
     */
    public Color(java.awt.Color other) {
        rgb = new Double3(other.getRed(), other.getGreen(), other.getBlue());
    }

    /**
     * Converts this color to a {@link java.awt.Color} object.
     * Any component value above 255 is clamped to 255.
     *
     * @return a java.awt.Color equivalent of this color
     */
    public java.awt.Color getColor() {
        int ir = (int) rgb.d1();
        int ig = (int) rgb.d2();
        int ib = (int) rgb.d3();
        return new java.awt.Color(ir > 255 ? 255 : ir,
                ig > 255 ? 255 : ig,
                ib > 255 ? 255 : ib);
    }

    /**
     * Adds one or more colors to this color component-wise.
     *
     * @param colors one or more {@link Color} objects to add
     * @return a new {@link Color} representing the result
     */
    public Color add(Color... colors) {
        double rr = rgb.d1();
        double rg = rgb.d2();
        double rb = rgb.d3();
        for (Color c : colors) {
            rr += c.rgb.d1();
            rg += c.rgb.d2();
            rb += c.rgb.d3();
        }
        return new Color(rr, rg, rb);
    }

    /**
     * Multiplies each color component by a corresponding scalar value.
     *
     * @param k a {@link Double3} representing scaling factors for each RGB component
     * @return a new {@link Color} after scaling
     * @throws IllegalArgumentException if any component of k is negative
     */
    public Color scale(Double3 k) {
        if (k.d1() < 0.0 || k.d2() < 0.0 || k.d3() < 0.0)
            throw new IllegalArgumentException("Can't scale a color by a negative number");
        return new Color(rgb.product(k));
    }

    /**
     * Multiplies all color components by the same scalar value.
     *
     * @param k the scalar value
     * @return a new {@link Color} after scaling
     * @throws IllegalArgumentException if k is negative
     */
    public Color scale(double k) {
        if (k < 0.0)
            throw new IllegalArgumentException("Can't scale a color by a negative number");
        return new Color(rgb.scale(k));
    }

    /**
     * Reduces each color component by dividing it with a given factor.
     *
     * @param k the reduction factor (must be ≥ 1)
     * @return a new {@link Color} after reduction
     * @throws IllegalArgumentException if k < 1
     */
    public Color reduce(int k) {
        if (k < 1)
            throw new IllegalArgumentException("Can't scale a color by a number lower than 1");
        return new Color(rgb.reduce(k));
    }

    @Override
    public String toString() {
        return "rgb:" + rgb;
    }
}
