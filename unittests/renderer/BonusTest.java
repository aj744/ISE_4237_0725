package renderer;

import org.junit.jupiter.api.Test;

import geometries.*;
import lighting.*;
import primitives.*;
import scene.Scene;

import static java.awt.Color.*;

public class BonusTest {
    private final Point          sphereCenter            = new Point(0, 0, -10);
    private static final double  SPHERE_RADIUS           = 50d;
    private final Color          sphereColor             = new Color(BLUE).reduce(2);
    private static final double  KD                      = 0.5;
    private static final double  KS                      = 0.5;
    private static final int     SHININESS               = 301;

    private final Geometry sphere = new Sphere(sphereCenter, SPHERE_RADIUS)
            .setEmission(sphereColor).setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(301));

    private final Scene          scene1                  = new Scene("Test scene");

    private final Color          sphereLightColor        = new Color(800, 500, 0);
    private final Point          sphereLightPosition     = new Point(-25, -25, 25);
    private final Camera.Builder camera1                 = Camera.getBuilder()                                          //
            .setRayTracer(scene1, RayTracerType.SIMPLE)                                                                      //
            .setLocation(new Point(0, 100, 500))                                                                              //
            .setDirection(Point.ZERO, Vector.AXIS_Y)                                                                         //
            .setVpSize(200, 200).setVpDistance(250);
    Material material = new Material().setKD(KD).setKS(KS).setShininess(SHININESS);
    private final Geometry[] objects = {
            new Sphere(new Point(35, 14, 300), 34).setEmission(new Color(BLUE)).setMaterial(
                    new Material().setKD(0.1).setKS(0.8).setShininess(500).setKT(0.7)),
            new Sphere(new Point(-20, 0, 200), 20).setEmission(new Color(ORANGE)).setMaterial(material),
            new Sphere(new Point(15, 6, 150), 26).setEmission(new Color(0, 180, 60)).setMaterial(material),
            new Sphere(new Point(-25, 5, -20), 25).setEmission(new Color(RED)).setMaterial(material),
            new Sphere(new Point(-60, 0, -40), 20).setEmission(new Color(YELLOW)).setMaterial(material),
            new Sphere(new Point(70, -10, -30), 10).setEmission(new Color(CYAN)).setMaterial(material),
            new Sphere(new Point(-120, 0, -90), 20).setEmission(new Color(MAGENTA)).setMaterial(material),
            new Sphere(new Point(50, 0, -100), 20).setEmission(new Color(PINK)).setMaterial(material),
            new Sphere(new Point(120, 0, -250), 20).setEmission(new Color(50, 80, 100)).setMaterial(material),
            new Sphere(new Point(10, 0, -400), 20).setEmission(new Color(75, 0, 130)).setMaterial(material)};


    @Test
    void spherePoint() {

        for (Geometry g : objects)
            scene1.geometries.add(g);
        camera1 //
                .setResolution(500, 500) //
                .build() //
                .renderImage() //
                .writeToImage("testSphere");
    }
}
