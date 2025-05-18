package scene;

import geometries.Geometries;
import geometries.Geometry;
import lighting.AmbientLight;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import primitives.Color;
import primitives.Point;
import primitives.Ray;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.geom.GeneralPath;
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
            File xmlFile = new File(fileName);
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

                System.out.println("Geometries:");
                for (int i = 0; i < shapes.getLength(); i++) {
                    Node shapeNode = shapes.item(i);
                    if (shapeNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element shape = (Element) shapeNode;
                        String tagName = shape.getTagName();
                        System.out.print(" - " + tagName);

                        NamedNodeMap attributes = shape.getAttributes();
                        for (int j = 0; j < attributes.getLength(); j++) {
                            Node attr = attributes.item(j);
                            System.out.print(" | " + attr.getNodeName() + ": " + attr.getNodeValue());
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

    public Color getColorFromString(String s) {
        String[] colorArray = s.split(" ");
        if (colorArray.length != 3) {
            throw new IllegalArgumentException("Expected 3 numbers for background color");
        }

        int[] rgb = new int[3];
        for(int i = 0; i < colorArray.length; i++) {
            int color = Integer.parseInt(colorArray[i]);
            if (color < 0 || color > 255) {
                throw new IllegalArgumentException("Expected 0 <= color <= 255");
            }
            rgb[i] = color;
        }
        return new Color(rgb[0], rgb[1], rgb[2]);
    }
}

