package renderer;

import static java.awt.Color.BLUE;

import org.junit.jupiter.api.Test;

import geometries.*;
import lighting.*;
import primitives.*;
import scene.Scene;

/**
 * Test rendering scenes with different lighting types (directional, point, spot) on basic geometries (sphere and triangles).
 * The class uses a scene and camera setup to render images with various light configurations and compare visual results.
 * <p>
 * Each test method sets up the geometry and light sources for the scene,
 * renders the image using a simple ray tracer, and writes it to an image file.
 * </p>
 * <p>Author: Dan Zilberstein</p>
 */
class LightsTests {

    /** Default constructor to satisfy JavaDoc generator */
    LightsTests() { /* to satisfy JavaDoc generator */ }

    /** First scene used for tests with a sphere */
    private final Scene scene1 = new Scene("Test scene");

    /** Second scene used for tests with triangles and ambient light */
    private final Scene scene2 = new Scene("Test scene")
            .setAmbientLight(new AmbientLight(new Color(38, 38, 38)));

    /** Camera builder for scene1 */
    private final Camera.Builder camera1 = Camera.getBuilder()
            .setRayTracer(scene1, RayTracerType.SIMPLE)
            .setLocation(new Point(0, 0, 1000))
            .setDirection(Point.ZERO, Vector.AXIS_Y)
            .setVpSize(150, 150).setVpDistance(1000);

    /** Camera builder for scene2 */
    private final Camera.Builder camera2 = Camera.getBuilder()
            .setRayTracer(scene2, RayTracerType.SIMPLE)
            .setLocation(new Point(0, 0, 1000))
            .setDirection(Point.ZERO, Vector.AXIS_Y)
            .setVpSize(200, 200).setVpDistance(1000);

    /** Shininess value used for materials */
    private static final int SHININESS = 301;

    /** Diffuse reflection coefficient */
    private static final double KD = 0.5;

    /** Diffuse reflection coefficient as Double3 */
    private static final Double3 KD3 = new Double3(0.2, 0.6, 0.4);

    /** Specular reflection coefficient */
    private static final double KS = 0.5;

    /** Specular reflection coefficient as Double3 */
    private static final Double3 KS3 = new Double3(0.2, 0.4, 0.3);

    /** Material used for triangle tests */
    private final Material material = new Material().setKD(KD3).setKS(KS3).setShininess(SHININESS);

    /** Light color used in triangle tests */
    private final Color trianglesLightColor = new Color(100, 100, 250);

    /** Light color used in sphere tests */
    private final Color sphereLightColor = new Color(800, 500, 0);

    /** Emission color for the sphere */
    private final Color sphereColor = new Color(BLUE).reduce(2);

    /** Center point of the sphere */
    private final Point sphereCenter = new Point(0, 0, -50);

    /** Radius of the sphere */
    private static final double SPHERE_RADIUS = 50d;

    /** Vertices used for the two triangles */
    private final Point[] vertices = {
            new Point(-110, -110, -150),
            new Point(95, 100, -150),
            new Point(110, -110, -150),
            new Point(-75, 78, 100)
    };

    /** Position of the light in sphere tests */
    private final Point sphereLightPosition = new Point(-50, -50, 25);

    /** Direction of the light in sphere tests */
    private final Vector sphereLightDirection = new Vector(1, 1, -0.5);

    /** Position of the light in triangle tests */
    private final Point trianglesLightPosition = new Point(30, 10, -100);

    /** Direction of the light in triangle tests */
    private final Vector trianglesLightDirection = new Vector(-2, -2, -2);

    /** Sphere geometry with emission and material for tests */
    private final Geometry sphere = new Sphere(sphereCenter, SPHERE_RADIUS)
            .setEmission(sphereColor).setMaterial(new Material().setKD(KD).setKS(KS).setShininess(SHININESS));

    /** First triangle for triangle tests */
    private final Geometry triangle1 = new Triangle(vertices[0], vertices[1], vertices[2])
            .setMaterial(material);

    /** Second triangle for triangle tests */
    private final Geometry triangle2 = new Triangle(vertices[0], vertices[1], vertices[3])
            .setMaterial(material);

    /**
     * Test rendering a sphere with a directional light source.
     */
    @Test
    void sphereDirectional() {
        scene1.geometries.add(sphere);
        scene1.lights.add(new DirectionalLight(sphereLightColor, sphereLightDirection));
        camera1.setResolution(500, 500).build().renderImage().writeToImage("lightSphereDirectional");
    }

    /**
     * Test rendering a sphere with a point light source.
     */
    @Test
    void spherePoint() {
        scene1.geometries.add(sphere);
        scene1.lights.add(new PointLight(sphereLightPosition, sphereLightColor)
                .setKl(0.005).setKq(0.0002));
        camera1.setResolution(500, 500).build().renderImage().writeToImage("lightSpherePoint");
    }

    /**
     * Test rendering a sphere with a spotlight source.
     */
    @Test
    void sphereSpot() {
        scene1.geometries.add(sphere);
        scene1.lights.add(new SpotLight(sphereLightColor, sphereLightPosition, sphereLightDirection)
                .setKl(0.001).setKq(0.0001));
        camera1.setResolution(500, 500).build().renderImage().writeToImage("lightSphereSpot");
    }

    /**
     * Test rendering two triangles with a directional light source.
     */
    @Test
    void trianglesDirectional() {
        scene2.geometries.add(triangle1, triangle2);
        scene2.lights.add(new DirectionalLight(trianglesLightColor, trianglesLightDirection));
        camera2.setResolution(500, 500).build().renderImage().writeToImage("lightTrianglesDirectional");
    }

    /**
     * Test rendering two triangles with a point light source.
     */
    @Test
    void trianglesPoint() {
        scene2.geometries.add(triangle1, triangle2);
        scene2.lights.add(new PointLight(trianglesLightPosition, trianglesLightColor)
                .setKl(0.001).setKq(0.0002));
        camera2.setResolution(500, 500).build().renderImage().writeToImage("lightTrianglesPoint");
    }

    /**
     * Test rendering two triangles with a spotlight source.
     */
    @Test
    void trianglesSpot() {
        scene2.geometries.add(triangle1, triangle2);
        scene2.lights.add(new SpotLight(trianglesLightColor, trianglesLightPosition, trianglesLightDirection)
                .setKl(0.001).setKq(0.0001));
        camera2.setResolution(500, 500).build().renderImage().writeToImage("lightTrianglesSpot");
    }

    /**
     * Test rendering a sphere with a narrow spotlight (sharp beam).
     */
    @Test
    void sphereSpotSharp() {
        scene1.geometries.add(sphere);
        scene1.lights.add(new SpotLight(sphereLightColor, sphereLightPosition, new Vector(1, 1, -0.5))
                .setKl(0.001).setKq(0.00004)); //.setNarrowBeam(10)
        camera1.setResolution(500, 500).build().renderImage().writeToImage("lightSphereSpotSharp");
    }

    /**
     * Test rendering two triangles with a narrow spotlight (sharp beam).
     */
    @Test
    void trianglesSpotSharp() {
        scene2.geometries.add(triangle1, triangle2);
        scene2.lights.add(new SpotLight(trianglesLightColor, trianglesLightPosition, trianglesLightDirection)
                .setKl(0.001).setKq(0.00002).setNarrowBeam(1));
        camera2.setResolution(500, 500).build().renderImage().writeToImage("lightTrianglesSpotSharp");
    }
}
