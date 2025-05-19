package renderer;

import primitives.Color;
import primitives.Ray;
import scene.Scene;

/**
 * An abstract base class for ray tracing implementations.
 * Provides the basic framework for tracing rays in a scene.
 */
public abstract class RayTracerBase {

    /**
     * The scene that will be used for ray tracing.
     */
    protected final Scene scene;

    /**
     * Constructs a ray tracer with the given scene.
     *
     * @param scene the scene to be rendered
     */
    protected RayTracerBase(Scene scene) {
        this.scene = scene;
    }

    /**
     * Traces a single ray and returns the resulting color.
     * This method must be implemented by subclasses.
     *
     * @param ray the ray to be traced
     * @return the color calculated for the ray
     */
    public abstract Color traceRay(Ray ray);
}
