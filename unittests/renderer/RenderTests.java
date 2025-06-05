package renderer;

import static java.awt.Color.*;

import org.junit.jupiter.api.Test;

import geometries.Sphere;
import geometries.Triangle;
import lighting.AmbientLight;
import primitives.*;
import scene.Scene;

/**
 * Unit tests for rendering basic scenes using the rendering engine.
 * <p>
 * Includes simple tests for rendering scenes with spheres and triangles,
 * with and without materials, colors and ambient lighting.
 * </p>
 *
 * Author: Dan
 */
class RenderTests {
    /**
     * Default constructor to satisfy JavaDoc generator.
     */
    RenderTests() { /* to satisfy JavaDoc generator */ }

    /**
     * Camera builder used for the test scenes.
     * Initialized with:
     * - location at origin
     * - direction toward -Z axis
     * - up direction as Y axis
     * - view plane size: 500x500
     * - view plane distance: 100
     */
    private final Camera.Builder camera = Camera.getBuilder() //
            .setLocation(Point.ZERO).setDirection(new Point(0, 0, -1), Vector.AXIS_Y) //
            .setVpDistance(100) //
            .setVpSize(500, 500);

    /**
     * Test rendering a basic scene with:
     * - greenish background
     * - a central sphere
     * - three triangles placed around it (up-left, down-left, down-right)
     * The image is saved as "Two color render test" and includes a yellow grid.
     */
    @Test
    void renderTwoColorTest() {
        Scene scene = new Scene("Two color").setBackground(new Color(75, 127, 90))
                .setAmbientLight(new AmbientLight(new Color(255, 191, 191)));
        scene.geometries //
                .add(// center
                        new Sphere(new Point(0, 0, -100), 50d),
                        // up left
                        new Triangle(new Point(-100, 0, -100), new Point(0, 100, -100), new Point(-100, 100, -100)),
                        // down left
                        new Triangle(new Point(-100, 0, -100), new Point(0, -100, -100), new Point(-100, -100, -100)),
                        // down right
                        new Triangle(new Point(100, 0, -100), new Point(0, -100, -100), new Point(100, -100, -100)));

        camera //
                .setRayTracer(scene, RayTracerType.SIMPLE) //
                .setResolution(1000, 1000) //
                .build() //
                .renderImage() //
                .printGrid(100, new Color(YELLOW)) //
                .writeToImage("Two color render test");
    }

    /**
     * Test rendering a scene with colored materials on the geometries instead of using emission.
     * - Each triangle has a different material color (R, G, B)
     * - Sphere has a diffuse grayish material
     * - Scene includes white ambient light
     * The image is saved as "color render test" and includes a white grid.
     *
     * Note: This test is relevant for stage 6 (lighting and materials),
     *       not for stage 5.
     */
    @Test
    void renderMultiColorTest() {
        Scene scene = new Scene("Multi color")
                .setAmbientLight(new AmbientLight(new Color(WHITE)));
        scene.geometries //
                .add(// center
                        new Sphere(new Point(0, 0, -100), 50)
                                .setMaterial(new Material().setKa(new Double3(0.4))),
                        // up left
                        new Triangle(new Point(-100, 0, -100), new Point(0, 100, -100), new Point(-100, 100, -100)) //
                                //.setEmission(new Color(GREEN))
                                .setMaterial(new Material().setKa(new Double3(0, 0.8, 0))),
                        // down left
                        new Triangle(new Point(-100, 0, -100), new Point(0, -100, -100), new Point(-100, -100, -100)) //
                                //.setEmission(new Color(RED))
                                .setMaterial(new Material().setKa(new Double3(0.8, 0, 0))),
                        // down right
                        new Triangle(new Point(100, 0, -100), new Point(0, -100, -100), new Point(100, -100, -100)) //
                                //.setEmission(new Color(BLUE))
                                .setMaterial(new Material().setKa(new Double3(0, 0, 0.8))));

        camera //
                .setRayTracer(scene, RayTracerType.SIMPLE) //
                .setResolution(1000, 1000) //
                .build() //
                .renderImage() //
                .printGrid(100, new Color(WHITE)) //
                .writeToImage("color render test");
    }

    /**
     * Bonus test for rendering a scene described using an XML file.
     * <p>
     * Currently this method creates an empty scene called "Using XML".
     * The XML parsing logic should be added in the appropriate package (not here).
     * </p>
     * The rendered result is saved as "xml render test".
     */
    @Test
    void basicRenderXml() {
        Scene scene = new Scene("Using XML");
        // enter XML file name and parse from XML file into scene object instead of the
        // new Scene above,
        // Use the code you added in appropriate packages
        // ...
        // NB: unit tests is not the correct place to put XML parsing code

        camera //
                .setRayTracer(scene, RayTracerType.SIMPLE) //
                .setResolution(1000, 1000) //
                .build() //
                .renderImage() //
                .printGrid(100, new Color(YELLOW)) //
                .writeToImage("xml render test");
    }
}
