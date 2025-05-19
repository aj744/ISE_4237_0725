package scene;

import geometries.*;
import lighting.AmbientLight;
import org.w3c.dom.*;
import primitives.*;

import javax.naming.InsufficientResourcesException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a 3D scene including background color, ambient light, and a collection of geometries.
 */
public class Scene {
    /** Scene name */
    public String name;

    /** Background color of the scene */
    public Color background;

    /** Ambient light in the scene */
    public AmbientLight ambientLight = AmbientLight.NONE;

    /** Collection of geometries in the scene */
    public Geometries geometries = new Geometries();

    /**
     * Constructor to initialize a scene with a given name.
     * @param name Name of the scene.
     */
    public Scene(String name) {
        this.name = name;
    }

    /**
     * Sets the background color of the scene.
     * @param background The background color to set.
     * @return The current scene (for chaining).
     */
    public Scene setBackground(Color background) {
        this.background = background;
        return this;
    }

    /**
     * Sets the ambient light of the scene.
     * @param ambientLight The ambient light to set.
     * @return The current scene (for chaining).
     */
    public Scene setAmbientLight(AmbientLight ambientLight) {
        this.ambientLight = ambientLight;
        return this;
    }

    /**
     * Adds a geometry object to the scene.
     * @param geometry The geometry to add.
     * @return The current scene (for chaining).
     */
    public Scene addGeometry(Geometry geometry) {
        geometries.add(geometry);
        return this;
    }

    /**
     * Loads and parses geometries and lighting from an XML file and adds them to the scene.
     * Expected tags: background-color, ambient-light, geometries, triangle, plane, sphere.
     * @param fileName The name of the XML file (without the ".xml" extension).
     * @return The current scene (for chaining).
     */
    public Scene addGeometriesFromXml(String fileName) {
        try {
            File xmlFile = new File("XMLFiles/" + fileName + ".xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = builder.parse(xmlFile);
            document.getDocumentElement().normalize();

            Element xmlScene = (Element) document.getDocumentElement();

            background = getColorFromString(xmlScene.getAttribute("background-color"));

            // ambient-light
            NodeList ambientList = xmlScene.getElementsByTagName("ambient-light");
            if (ambientList.getLength() > 0) {
                Element ambient = (Element) ambientList.item(0);
                ambientLight = new AmbientLight(getColorFromString(ambient.getAttribute("color")));
            } else {
                throw new IllegalArgumentException("No ambient light found");
            }

            // geometries
            NodeList geometriesList = xmlScene.getElementsByTagName("geometries");
            if (geometriesList.getLength() > 0) {
                Element geometries = (Element) geometriesList.item(0);
                NodeList shapes = geometries.getChildNodes();

                for (int i = 0; i < shapes.getLength(); i++) {
                    Node shapeNode = shapes.item(i);
                    if (shapeNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element shape = (Element) shapeNode;
                        String tagName = shape.getTagName();
                        System.out.print(" - " + tagName);

                        NamedNodeMap attributes = shape.getAttributes();
                        switch (tagName) {
                            case "triangle" -> {
                                if (attributes.getLength() != 3) {
                                    throw new InsufficientResourcesException("Triangle should have 3 attributes");
                                } else {
                                    List<Point> points = new ArrayList<>();
                                    for (int j = 0; j < attributes.getLength(); j++) {
                                        Node attr = attributes.item(j);
                                        points.add(getPointFromString(attr.getNodeValue()));
                                        System.out.print(" | " + attr.getNodeName() + ": " + attr.getNodeValue());
                                    }
                                    this.geometries.add(new Triangle(points.getFirst(), points.get(1), points.get(2)));
                                }
                            }
                            case "plane" -> {
                                if (attributes.getLength() != 2) {
                                    throw new InsufficientResourcesException("Plane should have 2 attributes");
                                } else {
                                    this.geometries.add(new Plane(
                                            getPointFromString(attributes.item(0).getNodeValue()),
                                            getVectorFromString(attributes.item(1).getNodeValue())
                                    ));
                                }
                            }
                            case "sphere" -> {
                                if (attributes.getLength() == 2) {
                                    this.geometries.add(new Sphere(
                                            Double.parseDouble(attributes.item(1).getNodeValue()),
                                            getPointFromString(attributes.item(0).getNodeValue())
                                    ));
                                } else {
                                    throw new InsufficientResourcesException("Sphere should have 2 attributes");
                                }
                            }
                        }
                        System.out.println();
                    }
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return this;
    }

    /**
     * Parses a string of the form "x y z" into a {@link Double3} object.
     * @param s The string to parse.
     * @return A Double3 with the parsed components.
     * @throws IllegalArgumentException If the input does not contain exactly 3 numbers.
     */
    private Double3 getDouble3FromString(String s) {
        String[] colorArray = s.split(" ");
        if (colorArray.length != 3) {
            throw new IllegalArgumentException("Expected 3 numbers for background color");
        }

        int[] rgb = new int[3];
        for (int i = 0; i < colorArray.length; i++) {
            rgb[i] = Integer.parseInt(colorArray[i]);
        }
        return new Double3(rgb[0], rgb[1], rgb[2]);
    }

    /**
     * Converts a string of RGB values into a {@link Color} object.
     * @param string A string like "255 255 255".
     * @return A Color object with the corresponding values.
     * @throws IllegalArgumentException If any value is out of the 0–255 range.
     */
    private Color getColorFromString(String string) {
        Double3 color = getDouble3FromString(string);
        if (color.d1() < 0 || color.d1() > 255 ||
                color.d2() < 0 || color.d2() > 255 ||
                color.d3() < 0 || color.d3() > 255) {
            throw new IllegalArgumentException("Expected 0 <= color <= 255");
        }

        return new Color(color.d1(), color.d2(), color.d3());
    }

    /**
     * Parses a string into a {@link Point} object.
     * @param string The string representation.
     * @return A Point object.
     */
    private Point getPointFromString(String string) {
        return new Point(getDouble3FromString(string));
    }

    /**
     * Parses a string into a {@link Vector} object.
     * @param string The string representation.
     * @return A Vector object.
     */
    private Vector getVectorFromString(String string) {
        return new Vector(getDouble3FromString(string));
    }
}
