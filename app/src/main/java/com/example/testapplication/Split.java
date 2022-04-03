package com.example.testapplication;

import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public class Split {

    double averageSpeed;
    double distance;
    String startTime;
    String endTime;
    float interval;

    Split(){
        averageSpeed = 0.0;
        distance = 0.0;
        startTime = "";
        endTime = "";
        interval = 0;
    }

    Split(double averageSpeed, double distance, String startTime, String endTime, float interval){
        this.averageSpeed = averageSpeed;
        this.distance = distance;
        this.startTime = startTime;
        this.endTime = endTime;
        this.interval = interval;
    }

    public double getAverageSpeed() {
        return averageSpeed;
    }

    public void setAverageSpeed(double averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /**
     *
     * @param file XML file with each splits data generated from GPX
     * @return Split list
     * @author  Lin
     */
    List<Split> decodeSplit(File file){
        List<Split> list = new ArrayList<>();
        if (file.length() != 0){
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            try {
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                FileInputStream fileInputStream = new FileInputStream(file);
                Document document = documentBuilder.parse(fileInputStream);
                Element elementRoot = document.getDocumentElement();

                NodeList nodelist_split = elementRoot.getElementsByTagName("split");
                if (nodelist_split.getLength() != 0){
                    for (int i = 0; i < nodelist_split.getLength(); i++) {

                        Node node = nodelist_split.item(i);
                        NodeList childNotes = node.getChildNodes();

                        String newAvgSpeed = searchNodeListForTarget(childNotes, "avgSpeed");
                        String newDistance = searchNodeListForTarget(childNotes, "distance");
                        String startTime = searchNodeListForTarget(childNotes, "startTime");
                        String endTime = searchNodeListForTarget(childNotes, "endTime");

                        double newAvgSpeedD = Double.parseDouble(newAvgSpeed);
                        double newDistanceD = Double.parseDouble(newDistance);
                        long newInterval = calculateInterval(startTime, endTime);
                        float minInterval = newInterval / 60000;

                        Split newSplit = new Split(newAvgSpeedD, newDistanceD, startTime, endTime, minInterval);
                        list.add(newSplit);

                    }
                }
              else {
                    Log.e("testApplication", "file without split data");
                    Split nullSplit = new Split();
                    list.add(nullSplit);
                }

                fileInputStream.close();
            } catch (ParserConfigurationException e) {
                Log.e("testApplication", "parse fileInputStream error", e);
            } catch (SAXException e) {
                Log.e("testApplication", "SAX error", e);
            } catch (FileNotFoundException e) {
                Log.e("testApplication", "file not found", e);
            } catch (IOException e) {
                Log.e("testApplication", "failed IO operation", e);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return list;

        }
        else {
            Log.e("testApplication", "file is empty");
        }
        return null;
    }


    /**
     *
     * @param src nodeList in element of document parse from xml file
     * @param target the tag name of xml file
     * @return the split value as needed
     * @author Lin
     */
    String searchNodeListForTarget(NodeList src, String target){

        for(int i = 0; i < src.getLength(); i++){
            Node n = src.item(i);
            if (n.getNodeName().equals(target)){
                return n.getFirstChild().getNodeValue();
            }
        }
        return "";
    }

    Long calculateInterval(String startTime, String endTime) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date1 = sdf.parse(startTime);
        Date date2 = sdf.parse(endTime);
        return date2.getTime() - date1.getTime();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Split split = (Split) o;
        return averageSpeed == split.averageSpeed && distance == split.distance &&
                Objects.equals(startTime, split.startTime) && Objects.equals(endTime,split.endTime);
    }

    String getSplitInfo(){
        return "Average Speed : " + averageSpeed + "km/h\n"
                + "Distance : " + distance + "km\n"
                + "Interval : " + interval + "min\n";
    }

}
