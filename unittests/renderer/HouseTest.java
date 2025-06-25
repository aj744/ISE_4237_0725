package renderer;

import geometries.Plane;
import geometries.Polygon;
import geometries.Sphere;
import geometries.Triangle;
import lighting.AmbientLight;
import lighting.DirectionalLight;
import lighting.PointLight;
import lighting.SpotLight;
import org.junit.jupiter.api.Test;
import primitives.Color;
import primitives.Material;
import primitives.Point;
import primitives.Vector;
import scene.Scene;

import java.util.List;

import static java.awt.Color.BLACK;
import static java.awt.Color.WHITE;

public class HouseTest {
    Scene scene = new Scene("House")
            .setAmbientLight(new AmbientLight(Color.BLACK))
            .setBackground(new Color(176, 224, 230));

    private final Camera.Builder cameraBuilder = Camera.getBuilder()     //
            .setRayTracer(scene, RayTracerType.SIMPLE);
    /**
     * Part 7 bonus, more than 10 shapes, and all types of lighting
     */
    @Test
    void house() {
        scene.geometries.add(
                new Plane(Point.ZERO, Vector.AXIS_Y)
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setKa(0.5).setKT(0).setKR(0))
                        .setEmission(new Color(46, 125, 50)),

                new Sphere(
                        new Point(0, 180, 308),
                        10
                ).setEmission(new Color(BLACK)),

                new Polygon(
                        new Point(200, 0, -300),
                        new Point(-200, 0, -300),
                        new Point(-200, 200, -300),
                        new Point(200, 200, -300)
                ).setEmission(new Color(205, 133, 63))
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setKT(0).setKR(0)),

                new Polygon(
                        new Point(200, 0, 300),
                        new Point(-200, 0, 300),
                        new Point(-200, 200, 300),
                        new Point(200, 200, 300)
                ).setEmission(new Color(205, 133, 63))
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setKT(0).setKR(0)),

                new Polygon(
                        new Point(200, 0, -300),
                        new Point(200, 0, 300),
                        new Point(200, 200, 300),
                        new Point(200, 200, -300)
                ).setEmission(new Color(160, 82, 45))
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setKT(0).setKR(0)),

                new Polygon(
                        new Point(-200, 0, 300),
                        new Point(-200, 0, -300),
                        new Point(-200, 200, -300),
                        new Point(-200, 200, 300)
                ).setEmission(new Color(160, 82, 45))
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setKT(0).setKR(0)),

                new Polygon(
                        new Point(-200, 200, -300),
                        new Point(-200, 200, 300),
                        new Point(0, 350, 300),
                        new Point(0, 350, -300)
                ).setEmission(new Color(255, 0, 0))
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setKT(0).setKR(0).setShininess(200)),

                new Polygon(
                        new Point(200, 200, 300),
                        new Point(200, 200, -300),
                        new Point(0, 350, -300),
                        new Point(0, 350, 300)
                ).setEmission(new Color(255, 0, 0))
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setKT(0).setKR(0).setShininess(200)),

                new Polygon(
                        new Point(30, 0, 301),
                        new Point(-30, 0, 301),
                        new Point(-30, 120, 301),
                        new Point(30, 120, 301)
                ).setEmission(new Color(139, 69, 19))
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setKT(0).setKR(0).setShininess(200)),

                new Triangle(
                        new Point(-200, 200, 300),
                        new Point(200, 200, 300),
                        new Point(0, 350, 300)
                ).setEmission(new Color(255, 50, 20))
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setKT(0).setKR(0)),

                new Triangle(
                        new Point(-200, 200, -300),
                        new Point(200, 200, -300),
                        new Point(0, 350, -300)
                ).setEmission(new Color(255, 50, 20))
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setKT(0).setKR(0))

        );
        scene.lights.addAll(List.of(
                new PointLight(
                        new Color(255, 228, 181),
                        new Point(0, 200, 0)
                ).setKl(0.0001).setKq(0.0001),

                new SpotLight(
                        new Color(245, 222, 179),
                        new Point(0, 170, 308),
                        Vector.AXIS_Y.scale(-1)
                ),

                new DirectionalLight(
                        new Color(WHITE).reduce(10),
                        Vector.AXIS_Y.scale(-1).add(Vector.AXIS_Z).add(Vector.AXIS_X)
                )
        ));

        cameraBuilder
                .setLocation(new Vector(-500, 250, 700))
                .setDirection(Point.ZERO)
                .setVpDistance(100).setVpSize(200, 200)
                .setResolution(600, 600) //
                .build() //
                .renderImage() //
                .writeToImage("House");

        cameraBuilder
                .moveCamera(new Vector(100, 300, -100))
                .build()
                .renderImage()
                .writeToImage("House moved");

        cameraBuilder
                .rotateCamera(70)
                .build()
                .renderImage()
                .writeToImage("House rotated");
    }
}
