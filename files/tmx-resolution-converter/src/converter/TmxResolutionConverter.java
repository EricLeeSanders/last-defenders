package converter;


import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.*;

public class TmxResolutionConverter {

    public static void convert(String filePath, String tileWidth, String tileHeight, String mapWidth, String mapHeight, float objectScale) {

        try {
                        
            
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(filePath);
            doc.getDocumentElement().normalize();

            updateMap(doc, mapWidth, mapHeight, tileWidth, tileHeight);
            updateLayers(doc, mapWidth, mapHeight);
            updateTileSets(doc,tileWidth, tileHeight);
            updateObjectGroups(doc, objectScale);

            // save
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filePath));
            transformer.transform(source, result);

            System.out.println("Done. Update tileset attribute manually.");

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    private static void updateTileSets(Document doc, String tileWidth, String tileHeight ) {

        NodeList tilesets = doc.getElementsByTagName("tileset");

        for (int i = 0; i < tilesets.getLength(); i++) {
            NamedNodeMap tilesetNodeMap = tilesets.item(i).getAttributes();
            Node width = tilesetNodeMap.getNamedItem("tilewidth");
            width.setTextContent(tileWidth);
            Node height = tilesetNodeMap.getNamedItem("tileheight");
            height.setTextContent(tileHeight);
        }
    }

    private static void updateLayers(Document doc, String mapWidth, String mapHeight) {

        NodeList layers = doc.getElementsByTagName("layer");

        for (int i = 0; i < layers.getLength(); i++) {
            NamedNodeMap layerNodeMap = layers.item(i).getAttributes();
            Node width = layerNodeMap.getNamedItem("width");
            width.setTextContent(mapWidth);
            Node height = layerNodeMap.getNamedItem("height");
            height.setTextContent(mapHeight);
        }
    }

    private static void updateObjectGroups(Document doc, float objectScale) {

        NodeList objectGroups = doc.getElementsByTagName("objectgroup");

        for (int i = 0; i < objectGroups.getLength(); i++) {
            NamedNodeMap objectGroupNodeMap = objectGroups.item(i).getAttributes();
            Node node = objectGroupNodeMap.getNamedItem("name");
            if (node.getTextContent().equals("Boundary")) {
                updateBoundaries(objectGroups.item(i), objectScale);
            } else if (node.getTextContent().equals("Path")) {
                updatePath(objectGroups.item(i), objectScale);
            }
        }
    }

    private static void updatePath(Node path, float objectScale) {

        NodeList nodeList = path.getChildNodes();

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            String nodeName = node.getNodeName();
            if (nodeName.equals("object")) {

                NamedNodeMap pathNodeMap = node.getAttributes();

                // Update path position
                Node x = pathNodeMap.getNamedItem("x");
                float xVal = Float.valueOf(x.getNodeValue()) * objectScale;
                x.setTextContent(String.valueOf(xVal));

                Node y = pathNodeMap.getNamedItem("y");
                float yVal = Float.valueOf(y.getNodeValue()) * objectScale;
                y.setTextContent(String.valueOf(yVal));

                if (!(node instanceof Element)) {
                    throw new IllegalArgumentException("Node is not an instance of Element");
                }

                Node pathline = ((Element) node).getElementsByTagName("polyline").item(0).getAttributes().item(0);
                updatePathLine(pathline, objectScale);
            }
        }
    }

    private static void updatePathLine(Node pathline, float objectScale) {
        
        String strPoints = pathline.getTextContent();
        System.out.println(strPoints);
        String[] points = strPoints.split("[, ]");
        StringBuffer sb = new StringBuffer();
        int count = 0;
        for (String s : points) {
            float point = Float.valueOf(s);
            point *= objectScale;
            sb.append(point);
            if (count % 2 == 0) {
                sb.append(",");
            } else {
                sb.append(" ");
            }

            count++;
        }
        System.out.println(sb.toString());
        
        pathline.setTextContent(sb.toString());
    }

    private static void updateBoundary(Node boundary, float objectScale) {

        NamedNodeMap boundaryNodeMap = boundary.getAttributes();

        Node width = boundaryNodeMap.getNamedItem("width");
        float widthVal = Float.valueOf(width.getNodeValue()) * objectScale;
        width.setTextContent(String.valueOf(widthVal));

        Node height = boundaryNodeMap.getNamedItem("height");
        float heightVal = Float.valueOf(height.getNodeValue()) * objectScale;
        height.setTextContent(String.valueOf(heightVal));

        Node x = boundaryNodeMap.getNamedItem("x");
        float xVal = Float.valueOf(x.getNodeValue()) * objectScale;
        x.setTextContent(String.valueOf(xVal));

        Node y = boundaryNodeMap.getNamedItem("y");
        float yVal = Float.valueOf(y.getNodeValue()) * objectScale;
        y.setTextContent(String.valueOf(yVal));
    }

    private static void updateBoundaries(Node boundaries, float objectScale) {

        NodeList nodeList = boundaries.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            String nodeName = node.getNodeName();
            if (nodeName.equals("object")) {
                updateBoundary(node, objectScale);
            }
        }

    }

    private static void updateMap(Document doc, String mapWidth, String mapHeight, String tileWidth, String tileHeight) {

        NamedNodeMap mapNodeMap = doc.getElementsByTagName("map").item(0).getAttributes();

        mapNodeMap.getNamedItem("width").setTextContent(mapWidth);
        mapNodeMap.getNamedItem("height").setTextContent(mapHeight);
        
        mapNodeMap.getNamedItem("tilewidth").setTextContent(tileWidth);;
        mapNodeMap.getNamedItem("tileheight").setTextContent(tileHeight);

    }
}
