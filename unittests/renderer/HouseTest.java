package renderer;

import geometries.*;
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
import java.util.Random;
import java.util.ArrayList;
import java.util.Scanner;

import static java.awt.Color.BLACK;
import static java.awt.Color.WHITE;

public class HouseTest {
    Scene scene = new Scene("House")
            .setAmbientLight(new AmbientLight(Color.BLACK))
            .setBackground(new Color(0, 0, 0));

    private final Camera.Builder cameraBuilder = Camera.getBuilder()
            .setRayTracer(scene, RayTracerType.SIMPLE);

    /**
     * Part 7 bonus, more than 10 shapes, and all types of lighting
     */
    @Test
    void house() {
        Point moonCenter = new Point(700,700,250);
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

                // קיר תחתון (מתחת לחלון)
                new Polygon(
                        new Point(-200, 0, 300),
                        new Point(-200, 0, -300),
                        new Point(-200, 50, -300),
                        new Point(-200, 50, 300)
                ).setEmission(new Color(160, 82, 45))
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setKT(0).setKR(0)),

// קיר עליון (מעל החלון)
                new Polygon(
                        new Point(-200, 150, 300),
                        new Point(-200, 150, -300),
                        new Point(-200, 200, -300),
                        new Point(-200, 200, 300)
                ).setEmission(new Color(160, 82, 45))
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setKT(0).setKR(0)),

// קיר שמאלי של החלון
                new Polygon(
                        new Point(-200, 50, -300),
                        new Point(-200, 150, -300),
                        new Point(-200, 150, -100),
                        new Point(-200, 50, -100)
                ).setEmission(new Color(160, 82, 45))
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setKT(0).setKR(0)),

// קיר ימני של החלון
                new Polygon(
                        new Point(-200, 50, 100),
                        new Point(-200, 150, 100),
                        new Point(-200, 150, 300),
                        new Point(-200, 50, 300)
                ).setEmission(new Color(160, 82, 45))
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setKT(0).setKR(0)),

                new Polygon(
                        new Point(-200, 50, -100),
                        new Point(-200, 150, -100),
                        new Point(-200, 150, 100),
                        new Point(-200, 50, 100)
                ) // צבע שונה - כחול פלדה (Steel Blue)
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setKT(1).setKR(0).setBlurriness(1)),



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
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setKT(0).setKR(0)),

                new Sphere(
                        moonCenter,100
                ).setEmission(new Color(WHITE))
                        .setMaterial(new Material().setKD(1).setKS(0.5).setKT(0)),

                new Sphere(
                        new Point(635,700,250),100
                ).setEmission(Color.BLACK)
                        .setMaterial(new Material().setKD(0).setKS(0).setKT(0)),

                new Polygon(
                        new Point(600, 0, 200),
                        new Point(600, 300, 200),
                        new Point(700, 300, 200),
                        new Point(700, 0, 200)
                ).setEmission(new Color(139, 69, 19))
                        .setMaterial(new Material().setKD(0.5).setKT(0.5).setKT(0)),

                new Polygon(
                        new Point(600, 0, 200),
                        new Point(600, 300, 200),
                        new Point(600, 300, 100),
                        new Point(600, 0, 100)
                ).setEmission(new Color(139, 69, 19))
                        .setMaterial(new Material().setKD(0.5).setKT(0.5).setKT(0)),

                new Polygon(
                        new Point(700, 0, 100),
                        new Point(700, 300, 100),
                        new Point(600, 300, 100),
                        new Point(600, 0, 100)
                ).setEmission(new Color(139, 69, 19))
                        .setMaterial(new Material().setKD(0.5).setKT(0.5).setKT(0)),

                new Polygon(
                        new Point(700, 0, 200),
                        new Point(700, 300, 200),
                        new Point(700, 300, 100),
                        new Point(700, 0, 100)
                ).setEmission(new Color(139, 69, 19))
                        .setMaterial(new Material().setKD(0.5).setKT(0.5).setKT(0)),

                new Triangle(
                        new Point(550, 200, 250),
                        new Point(650, 550, 150),
                        new Point(575, 200, 250)
                ).setEmission(new Color(34, 139, 34))
                        .setMaterial(new Material().setKD(0.5).setKT(0.5).setKT(0)),

                new Triangle(
                        new Point(575, 200, 250),
                        new Point(650, 550, 150),
                        new Point(750, 200, 250)
                ).setEmission(new Color(34, 139, 34))
                        .setMaterial(new Material().setKD(0.5).setKT(0.5).setKT(0)),

                new Triangle(
                        new Point(550, 200, 250),
                        new Point(650, 550, 150),
                        new Point(550, 200, 50)
                ).setEmission(new Color(34, 139, 34))
                        .setMaterial(new Material().setKD(0.5).setKT(0.5).setKT(0)),

                new Triangle(
                        new Point(750, 200, 250),
                        new Point(650, 550, 150),
                        new Point(750, 200, 50)
                ).setEmission(new Color(34, 139, 34))
                        .setMaterial(new Material().setKD(0.5).setKT(0.5).setKT(0))


                );
        final Random random = new Random();
        for (int i=0; i<0; i++) {
            var x = random.nextBoolean() ? 800 * random.nextDouble() : -800 * random.nextDouble();
            var z = random.nextBoolean() ? 800 * random.nextDouble() : -800 * random.nextDouble();
            Point p1 = new Point(x, 0, z);
            Point p2 = new Point(x+8, 0, z);
            Point p3 = new Point(x+4, 30, z);

            scene.addGeometry(new Triangle(p1, p2, p3)
                    .setEmission(new Color(144, 238, 144))
                    .setMaterial(new Material().setKD(0.5).setKT(0)));
        }


        Random rand = new Random();
        List<Sphere> stars = new ArrayList<>();
        int numStars = 60;

        for (int i = 0; i < numStars; i++) {
            double x = -788 + rand.nextDouble() * 1200;
            double y = 337 + rand.nextDouble() * 300;   //
            double z = 122 + rand.nextDouble() * 300;   //

            Sphere star = (Sphere) new Sphere(new Point(x, y, z), 1.5)
                    .setEmission(new Color(255, 255, 255))
                    .setMaterial(new Material().setKD(0.2).setKS(0.8).setKT(0));
            stars.add(star);
            scene.addGeometry(star);
        }

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
                       Point.ZERO.subtract(moonCenter)
                )
        ));

        cameraBuilder
                .setLocation(new Vector(-500, 250, 700))
                .setDirection(Point.ZERO)
                .setVpDistance(100).setVpSize(200, 200)
                .setResolution(600, 600)
                .build()
                .renderImage()
                .writeToImage("House");
    }
}
