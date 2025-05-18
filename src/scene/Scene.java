package scene;

import geometries.*;
import lighting.AmbientLight;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import primitives.*;

import javax.naming.InsufficientResourcesException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;

import java.io.*;


public class Scene {
    public String name;
    public Color background;
    public AmbientLight ambientLight = AmbientLight.NONE;
    public Geometries geometries = new Geometries();

    public Scene(String name) {
        this.name = name;
    }

    public Scene setBackground(Color background) {
        this.background = background;
        return this;
    }
    public Scene setAmbientLight(AmbientLight ambientLight) {
        this.ambientLight = ambientLight;
        return this;
    }
    public Scene addGeometry(Geometry geometry) {
        geometries.add(geometry);
        return this;
    }

    public Scene addGeometriesFromXml(String fileName) {
        try {
            File xmlFile = new File(fileName + ".xml");
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
            }
            else {
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
                                    throw new InsufficientResourcesException("Triangle should have 3 attributes");
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
                                } else if (attributes.getLength() == 2) {

                                }
                                else {
                                    throw new InsufficientResourcesException("Triangle should have 3 attributes");
                                }
                            }
                        }


                        System.out.println();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    private Double3 getDouble3FromString(String s) {
        String[] colorArray = s.split(" ");
        if (colorArray.length != 3) {
            throw new IllegalArgumentException("Expected 3 numbers for background color");
        }

        int[] rgb = new int[3];
        for(int i = 0; i < colorArray.length; i++) {
            rgb[i] = Integer.parseInt(colorArray[i]);
        }
        return new Double3(rgb[0], rgb[1], rgb[2]);
    }

    private Color getColorFromString(String string) {
        Double3 color = getDouble3FromString(string);
        if (color.d1() < 0 || color.d1() > 255 || color.d2() < 0 || color.d2() > 255|| color.d3() < 0
                || color.d3() > 255) {
            throw new IllegalArgumentException("Expected 0 <= color <= 255");
        }

        return new Color(color.d1(), color.d2(), color.d3());
    }

    private Point getPointFromString(String string) {
        return new Point(getDouble3FromString(string));
    }

    private Vector getVectorFromString(String string) {
        return new Vector(getDouble3FromString(string));
    }
}

